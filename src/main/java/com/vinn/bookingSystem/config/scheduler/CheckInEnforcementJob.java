package com.vinn.bookingSystem.config.scheduler;

import com.vinn.bookingSystem.features.booking.entity.Booking;
import com.vinn.bookingSystem.features.booking.entity.BookingStatus;
import com.vinn.bookingSystem.features.booking.repository.BookingRepository;
import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CheckInEnforcementJob implements Job {

    private final BookingRepository bookingRepository;

    @Override
    public void execute(JobExecutionContext context) {
        Long scheduleId = (Long) context.getMergedJobDataMap().get("scheduleId");
        List<Booking> bookings = this.bookingRepository.findByScheduleId(scheduleId);

        for (Booking booking : bookings) {
            if (!booking.isCheckedIn()) {
                booking.setStatus(BookingStatus.NO_SHOW.getValue());
                bookingRepository.save(booking);
            }
        }
    }
}
