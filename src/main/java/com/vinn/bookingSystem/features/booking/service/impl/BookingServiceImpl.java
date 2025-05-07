package com.vinn.bookingSystem.features.booking.service.impl;

import com.vinn.bookingSystem.config.cache.CacheService;
import com.vinn.bookingSystem.config.scheduler.ClassScheduleJobService;
import com.vinn.bookingSystem.config.utils.EntityUtil;
import com.vinn.bookingSystem.features.booking.entity.Booking;
import com.vinn.bookingSystem.features.booking.entity.BookingResult;
import com.vinn.bookingSystem.features.booking.entity.BookingStatus;
import com.vinn.bookingSystem.features.booking.repository.BookingRepository;
import com.vinn.bookingSystem.features.booking.service.BookingService;
import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import com.vinn.bookingSystem.features.classschedule.dto.ClassScheduleDTO;
import com.vinn.bookingSystem.features.classschedule.mapper.ClassScheduleMapper;
import com.vinn.bookingSystem.features.classschedule.repository.ClassScheduleRepository;
import com.vinn.bookingSystem.features.user.entity.User;
import com.vinn.bookingSystem.features.user.repository.UserRepository;
import com.vinn.bookingSystem.features.userpackages.entity.UserPackage;
import com.vinn.bookingSystem.features.userpackages.repository.UserPackageRepository;
import com.vinn.bookingSystem.features.waitlist.entity.Waitlist;
import com.vinn.bookingSystem.features.waitlist.entity.WaitlistStatus;
import com.vinn.bookingSystem.features.waitlist.repository.WaitlistRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final ClassScheduleRepository classScheduleRepository;
    private final CacheService cacheService;
    private final UserRepository userRepository;
    private final ClassScheduleMapper classScheduleMapper;
    private final UserPackageRepository userPackageRepository;
    private final BookingRepository bookingRepository;
    private final WaitlistRepository waitlistRepository;
    private final ClassScheduleJobService classScheduleJobService;
    private final RedissonClient redissonClient;

    @Override
    @Transactional(readOnly = true)
    public List<ClassScheduleDTO> getClassSchedulesByCountry(String countryCode) {
        @SuppressWarnings("unchecked")
        List<ClassScheduleDTO> cachedSchedules = (List<ClassScheduleDTO>) cacheService.getCachedValue("classSchedulesByCountry", countryCode);

        if (cachedSchedules != null) {
            return cachedSchedules;
        }

        List<ClassScheduleDTO> classSchedules = classScheduleRepository.findByClassEntity_Country_Code(countryCode)
                .stream()
                .map(classScheduleMapper::toDTO)
                .collect(Collectors.toList());

        this.cacheService.putCachedValue("classSchedulesByCountry", countryCode, classSchedules);

        return classSchedules;
    }

    @Override
    @Transactional
    public BookingResult bookClass(final Long classScheduleId, final Long userId) {
        RLock lock = redissonClient.getLock("lock:booking:" + classScheduleId);

        boolean isLocked = false;
        try {
            isLocked = lock.tryLock(10, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                return BookingResult.ERROR;
            }

            final ClassSchedule classSchedule = EntityUtil.getEntityById(this.classScheduleRepository, classScheduleId);

            final User user = EntityUtil.getEntityById(this.userRepository, userId);

            final UserPackage userPackage = this.userPackageRepository
                    .findTopByUserIdAndRemainingCreditsGreaterThanOrderByPurchaseDateAsc(userId, 0)
                    .orElse(null);

            if (classSchedule.getAvailableSlots() <= 0) {
                final Waitlist waitlist = Waitlist.builder()
                        .user(user)
                        .schedule(classSchedule)
                        .userPackage(userPackage)
                        .creditsReserved(1)
                        .addedTime(LocalDateTime.now())
                        .status(WaitlistStatus.WAITING.getValue())
                        .build();
                this.waitlistRepository.save(waitlist);
                log.info("User added to waitlist for classScheduleId={}", classScheduleId);
                return BookingResult.FULL;
            }

            if (userPackage == null) {
                return BookingResult.NO_PACKAGE;
            }

            boolean isOverlapping = this.bookingRepository.existsByUserAndScheduleStartTimeBeforeAndScheduleEndTimeAfter(
                    user, classSchedule.getEndTime(), classSchedule.getStartTime());

            if (isOverlapping) {
                return BookingResult.OVERLAPPING;
            }

            if (!userPackage.getPackageEntity().getCountry().getId().equals(classSchedule.getClassEntity().getCountry().getId())) {
                log.warn("User package country does not match class schedule country");
                return BookingResult.COUNTRY_MISMATCH;
            }

            userPackage.setRemainingCredits(userPackage.getRemainingCredits() - 1);
            this.userPackageRepository.save(userPackage);

            classSchedule.setAvailableSlots(classSchedule.getAvailableSlots() - 1);
            this.classScheduleRepository.save(classSchedule);

            final Booking booking = Booking.builder()
                    .user(user)
                    .schedule(classSchedule)
                    .creditsUsed(1)
                    .bookingTime(LocalDateTime.now())
                    .status(BookingStatus.BOOKED.getValue())
                    .checkedIn(false)
                    .checkInTime(null)
                    .cancellationTime(null)
                    .creditRefunded(false)
                    .build();
            this.bookingRepository.save(booking);

            this.classScheduleJobService.scheduleWaitlistRefundJob(classSchedule);

            this.cacheService.evictCachedValue("classSchedulesByCountry", classSchedule.getClassEntity().getCountry().getCode());
            return BookingResult.SUCCESS;
        } catch (Exception e) {
            log.error("Error while booking class", e);
            return BookingResult.ERROR;
        } finally {
            if (isLocked) {
                lock.unlock();
            }
        }
    }

    @Override
    @Transactional
    public boolean cancelBooking(Long bookingId) {
        Booking booking = EntityUtil.getEntityById(this.bookingRepository, bookingId);

        if (!Objects.equals(booking.getStatus(), BookingStatus.BOOKED.getValue())) {
            log.warn("Booking is not active: {}", bookingId);
            return false;
        }

        ClassSchedule schedule = booking.getSchedule();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime classStartTime = schedule.getStartTime();

        boolean eligibleForRefund = now.isBefore(classStartTime.minusHours(4));

        // Refund logic
        if (eligibleForRefund) {
            UserPackage refundablePackage = userPackageRepository
                    .findTopByUserIdAndPurchaseDateLessThanEqualOrderByPurchaseDateDesc(
                            booking.getUser().getId(),
                            booking.getBookingTime()
                    )
                    .orElse(null);

            if (refundablePackage != null) {
                refundablePackage.setRemainingCredits(refundablePackage.getRemainingCredits() + booking.getCreditsUsed());
                userPackageRepository.save(refundablePackage);
                booking.setCreditRefunded(true);
            }
        }

        booking.setStatus(BookingStatus.CANCELLED.getValue());
        booking.setCancellationTime(now);
        bookingRepository.save(booking);

        // Increase available slots
        schedule.setAvailableSlots(schedule.getAvailableSlots() + 1);
        classScheduleRepository.save(schedule);

        // Promote waitlist user (FIFO)
        List<Waitlist> waitlist = waitlistRepository
                .findByScheduleIdAndStatusOrderByAddedTimeAsc(schedule.getId(), WaitlistStatus.WAITING.getValue());

        if (!waitlist.isEmpty()) {
            Waitlist nextInLine = waitlist.getFirst();

            Booking promotedBooking = Booking.builder()
                    .user(nextInLine.getUser())
                    .schedule(schedule)
                    .creditsUsed(1)
                    .bookingTime(LocalDateTime.now())
                    .status(BookingStatus.BOOKED.getValue())
                    .checkedIn(false)
                    .creditRefunded(false)
                    .build();
            bookingRepository.save(promotedBooking);

            schedule.setAvailableSlots(schedule.getAvailableSlots() - 1);
            classScheduleRepository.save(schedule);

            nextInLine.setStatus(WaitlistStatus.BOOKED.getValue());
            waitlistRepository.save(nextInLine);

            UserPackage wp = nextInLine.getUserPackage();
            wp.setRemainingCredits(wp.getRemainingCredits() - 1);
            userPackageRepository.save(wp);
        } else {
            List<Waitlist> remainingWaitlist = waitlistRepository
                    .findByScheduleIdAndStatusOrderByAddedTimeAsc(schedule.getId(), WaitlistStatus.WAITING.getValue());

            for (Waitlist w : remainingWaitlist) {
                w.setStatus(WaitlistStatus.CANCELLED.getValue());
                waitlistRepository.save(w);
            }
        }

        cacheService.evictCachedValue("classSchedulesByCountry", schedule.getClassEntity().getCountry().getCode());

        log.info("Cancelled booking {} and updated waitlist if applicable.", bookingId);
        return true;
    }
}
