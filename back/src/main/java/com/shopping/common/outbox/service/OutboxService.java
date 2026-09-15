package com.shopping.common.outbox.service;

import com.shopping.common.outbox.dto.OutboxMessage;
import com.shopping.common.outbox.entity.OutboxStatus;
import com.shopping.common.outbox.repository.OutBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OutboxService {

    private final OutBoxRepository outBoxRepository;

    public List<OutboxMessage> findPendingOutboxesTop100() {
        return outBoxRepository.findAllByStatusPending(OutboxStatus.PENDING);
    }
}
