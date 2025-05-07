package com.vinn.bookingSystem.config.scheduler;

import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class ClassScheduleJobServiceImpl implements ClassScheduleJobService {

    private final Scheduler scheduler;

    @Override
    public void scheduleCheckInJob(ClassSchedule schedule) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(CheckInEnforcementJob.class)
                .withIdentity("checkInJob_" + schedule.getId(), "checkin")
                .usingJobData("scheduleId", schedule.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("checkInTrigger_" + schedule.getId(), "checkin")
                .startAt(Timestamp.valueOf(schedule.getStartTime()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    @Override
    public void scheduleWaitlistRefundJob(ClassSchedule schedule) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(WaitlistRefundJob.class)
                .withIdentity("refundJob_" + schedule.getId(), "refund")
                .usingJobData("scheduleId", schedule.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("refundTrigger_" + schedule.getId(), "refund")
                .startAt(Timestamp.valueOf(schedule.getEndTime()))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }
}

