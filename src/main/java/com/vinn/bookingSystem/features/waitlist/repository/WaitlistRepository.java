package com.vinn.bookingSystem.features.waitlist.repository;

import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import com.vinn.bookingSystem.features.waitlist.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    List<Waitlist> findByScheduleIdAndStatusOrderByAddedTimeAsc(Long scheduleId, Integer status);

    List<Waitlist> findAllBySchedule_EndTimeBeforeAndStatus(LocalDateTime scheduleEndTime, Integer status);
}
