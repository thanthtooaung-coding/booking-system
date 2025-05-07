package com.vinn.bookingSystem.features.classschedule.repository;

import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {
    List<ClassSchedule> findByClassEntity_Country_Code(String countryCode);
}
