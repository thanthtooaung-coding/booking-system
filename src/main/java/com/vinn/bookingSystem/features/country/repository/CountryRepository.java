package com.vinn.bookingSystem.features.country.repository;

import com.vinn.bookingSystem.features.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByCode(String code);

    List<Country> findByActiveTrue();
}
