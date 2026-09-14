package com.shopping.payment.repository;

import com.shopping.payment.entity.Payment;
import com.shopping.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByAttemptIdAndStatusIn(String attemptId, Collection<PaymentStatus> statuses);
}
