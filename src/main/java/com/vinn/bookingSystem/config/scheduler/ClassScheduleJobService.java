package com.vinn.bookingSystem.config.scheduler;

import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import org.quartz.SchedulerException;

public interface ClassScheduleJobService {
    void scheduleCheckInJob(ClassSchedule schedule) throws SchedulerException;
    void scheduleWaitlistRefundJob(ClassSchedule schedule) throws SchedulerException;
}
