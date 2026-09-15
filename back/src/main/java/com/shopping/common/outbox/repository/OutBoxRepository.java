package com.shopping.common.outbox.repository;

import com.shopping.common.outbox.dto.OutboxMessage;
import com.shopping.common.outbox.entity.Outbox;
import com.shopping.common.outbox.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<Outbox, Long> {

    @Query("""
        SELECT new com.shopping.common.outbox.dto.OutboxMessage(
            o.topic,
            o.key,
            o.payload)
        FROM Outbox o
        WHERE o.status = OutboxStatus.PENDING
        LIMIT 100
        ORDER BY o.createdAt ASC
    """)
    List<OutboxMessage> findAllByStatusPending(OutboxStatus outboxStatus);
}
