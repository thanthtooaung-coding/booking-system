package com.vinn.bookingSystem.features.packages.repository;

import com.vinn.bookingSystem.features.packages.entity.PackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Long> {
    List<PackageEntity> findByCountryIdAndActiveTrue(final Long countryId);

    @Query("SELECT p FROM PackageEntity p JOIN p.country c WHERE c.code = :countryCode AND p.active = true")
    List<PackageEntity> findActivePackagesByCountryCode(@Param("countryCode") String countryCode);

    List<PackageEntity> findByActiveTrue();
}
