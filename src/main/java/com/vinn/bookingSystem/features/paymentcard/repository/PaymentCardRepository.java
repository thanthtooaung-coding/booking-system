package com.vinn.bookingSystem.features.paymentcard.repository;

import com.vinn.bookingSystem.features.paymentcard.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {
}
