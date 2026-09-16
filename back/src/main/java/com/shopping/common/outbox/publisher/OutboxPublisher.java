package com.shopping.common.outbox.publisher;

import com.shopping.common.outbox.entity.Outbox;
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
            for (Outbox outbox : outboxService.findPendingOutboxesTop100()) {
                String topic = outbox.getTopic().getName();
                String key = outbox.getKey();
                String payload = outbox.getPayload();

                kafkaTemplate.send(topic, key, payload).get(10, TimeUnit.SECONDS);

                outboxService.markAsPublished(outbox.getId());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("Error while publishing outbox message", e);
        }
    }
}
