package com.shopping.common.outbox.entity;

import com.shopping.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Outbox extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String eventId;

    @Column(nullable = false, updatable = false)
    private String topic;

    @Column(nullable = false, updatable = false)
    private String key;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.PENDING;

    @Column(nullable = false)
    private String payload;

    public static Outbox pending(String topic, String key, String eventId, String payload) {
        if(topic == null || key == null || eventId == null || payload == null) {
            throw new IllegalArgumentException("Topic and payload and eventId cannot be null");
        }

        return Outbox.builder()
                .topic(topic)
                .key(key)
                .eventId(eventId)
                .payload(payload)
                .build();
    }
}

