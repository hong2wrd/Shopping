package com.shopping.common.outbox.publisher;

import com.shopping.common.outbox.dto.OutboxMessage;
import com.shopping.common.outbox.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxService outboxService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelay = 1000)
    public void handle() {
        try {
            for (OutboxMessage outboxMessage : outboxService.findPendingOutboxesTop100()) {
                String topic = outboxMessage.topic();
                String key = outboxMessage.key();
                String payload = outboxMessage.payload();

                kafkaTemplate.send(topic, key, payload).get(10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Error while publishing outbox message", e);
        }
    }
}
