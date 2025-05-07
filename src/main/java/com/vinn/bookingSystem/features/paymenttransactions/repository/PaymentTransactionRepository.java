package com.vinn.bookingSystem.features.paymenttransactions.repository;

import com.vinn.bookingSystem.features.paymenttransactions.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
}
