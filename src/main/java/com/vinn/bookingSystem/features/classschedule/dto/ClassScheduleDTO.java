package com.vinn.bookingSystem.features.classschedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleDTO {
    private Long id;
    private String className;
    private String instructorName;
    private String startTime;
    private String endTime;
    private Integer totalSlots;
    private Integer availableSlots;
    private String location;
    private String status;
}
