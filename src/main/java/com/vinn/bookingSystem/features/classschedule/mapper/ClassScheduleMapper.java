package com.vinn.bookingSystem.features.classschedule.mapper;

import com.vinn.bookingSystem.features.classschedule.dto.ClassScheduleDTO;
import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import com.vinn.bookingSystem.features.classschedule.entity.ClassScheduleStatus;
import org.springframework.stereotype.Component;

@Component
public class ClassScheduleMapper {

    public ClassScheduleDTO toDTO(final ClassSchedule classSchedule) {
        ClassScheduleDTO dto = new ClassScheduleDTO();
        dto.setId(classSchedule.getId());
        dto.setClassName(classSchedule.getClassEntity().getName());
        dto.setInstructorName(classSchedule.getInstructor().getName());
        dto.setStartTime(classSchedule.getStartTime().toString());
        dto.setEndTime(classSchedule.getEndTime().toString());
        dto.setTotalSlots(classSchedule.getTotalSlots());
        dto.setAvailableSlots(classSchedule.getAvailableSlots());
        dto.setLocation(classSchedule.getLocation());
        dto.setStatus(ClassScheduleStatus.fromInt(classSchedule.getStatus()).getCode());
        return dto;
    }
}
