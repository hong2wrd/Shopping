package com.shopping.common.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic paymentApprovedTopic() {
        return TopicBuilder
                .name("shopping.payment.approved")
                .partitions(2)
                .replicas(1)
                .build();
    }

}
