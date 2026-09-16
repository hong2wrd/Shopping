package com.shopping.common.outbox.service;

import com.shopping.common.exception.BusinessException;
import com.shopping.common.exception.ErrorCode;
import com.shopping.common.outbox.entity.Outbox;
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

    @Transactional(readOnly = true)
    public List<Outbox> findPendingOutboxesTop100() {
        return outBoxRepository.findTop100ByStatusOrderByIdAsc(OutboxStatus.PENDING);
    }

    public void markAsPublished(Long outboxId) {
        Outbox outbox = outBoxRepository.findById(outboxId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OUTBOX_NOT_FOUND));

        outbox.published();
    }
}
