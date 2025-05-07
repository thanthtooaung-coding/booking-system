package com.vinn.bookingSystem.features.classcategory.repository;

import com.vinn.bookingSystem.features.classcategory.entity.ClassCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassCategoryRepository extends JpaRepository<ClassCategory, Long> {
    Optional<ClassCategory> findByName(final String name);
}
