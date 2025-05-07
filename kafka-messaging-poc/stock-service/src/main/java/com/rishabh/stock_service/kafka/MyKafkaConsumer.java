package com.rishabh.stock_service.kafka;

import com.rishabh.domain_service.dto.OrderEvent;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@EnableKafka
public class MyKafkaConsumer {

    private final String bootstrapServers = "localhost:9092";
    private final String groupId = "stock";
    private final String topic = "order_topics";

    public void consumeMessages() {
        // Setting up Kafka consumer properties
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class); // Change this to match your message type

        // Creating the KafkaConsumer instance
        Consumer<String, OrderEvent> consumer = new KafkaConsumer<>(consumerProps);

        // Subscribing to topic
        consumer.subscribe(Collections.singletonList(topic));

        // Polling for messages
        while (true) {
            ConsumerRecords<String, OrderEvent> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, OrderEvent> record : records) {
                System.out.println("Consumed message: " + record.value());
                // Your custom logic for handling the consumed message
            }
        }
    }
}

