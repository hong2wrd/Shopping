package com.shopping.payment.repository;

import com.shopping.payment.entity.Payment;
import com.shopping.payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("""
        SELECT p FROM Payment p
        WHERE p.attemptId = :attemptId
          AND p.status IN :statuses
          AND p.order.member.id = :memId
    """)
    Optional<Payment> findByAttemptIdAndStatusIn(@Param("attemptId") String attemptId,
                                                 @Param("statuses") Collection<PaymentStatus> statuses,
                                                 @Param("memId") String memId);
}
