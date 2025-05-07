package com.vinn.bookingSystem.features.userpackages.repository;

import com.vinn.bookingSystem.features.userpackages.entity.UserPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPackageRepository extends JpaRepository<UserPackage, Long> {
    List<UserPackage> findByUserId(Long userId);

    Optional<UserPackage> findTopByUserIdAndRemainingCreditsGreaterThanOrderByPurchaseDateAsc(Long userId, int i);

    Optional<UserPackage> findTopByUserIdAndPurchaseDateLessThanEqualOrderByPurchaseDateDesc(Long userId, LocalDateTime bookingTime);
}
