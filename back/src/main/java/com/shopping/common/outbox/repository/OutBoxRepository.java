package com.shopping.common.outbox.repository;

import com.shopping.common.outbox.entity.Outbox;
import com.shopping.common.outbox.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<Outbox, Long> {

    List<Outbox> findTop100ByStatusOrderByIdAsc(OutboxStatus status);

}
