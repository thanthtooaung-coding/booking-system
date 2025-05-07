package com.vinn.bookingSystem.features.booking.repository;

import com.vinn.bookingSystem.features.booking.entity.Booking;
import com.vinn.bookingSystem.features.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    boolean existsByUserAndScheduleStartTimeBeforeAndScheduleEndTimeAfter(User user, LocalDateTime endTime, LocalDateTime startTime);
    List<Booking> findByScheduleId(Long scheduleId);
}
