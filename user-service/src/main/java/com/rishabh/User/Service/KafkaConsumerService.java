package com.rishabh.User.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "user-events", groupId = "user-service-group")
    public void listen(String message) {
        log.info("🔔 Received Kafka message: {}", message);

        // You could parse this message and perform any side effects if needed.
        // For example, saving it to an audit log collection.
    }
}

