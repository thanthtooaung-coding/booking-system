package com.vinn.bookingSystem.features.booking.service;

import com.vinn.bookingSystem.features.booking.entity.BookingResult;
import com.vinn.bookingSystem.features.classschedule.dto.ClassScheduleDTO;
import java.util.List;

public interface BookingService {
    List<ClassScheduleDTO> getClassSchedulesByCountry(String countryCode);
    BookingResult bookClass(Long classScheduleId, Long userId);
    boolean cancelBooking(Long bookingId);
}
