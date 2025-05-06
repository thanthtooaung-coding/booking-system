/*
 * @Author : Thant Htoo Aung
 * @Date : 6/5/2025
 * @Time : 01:17 PM (ICT)
 */
package com.vinn.bookingSystem.features.user.repository;

import com.vinn.bookingSystem.features.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String identifier);
}
