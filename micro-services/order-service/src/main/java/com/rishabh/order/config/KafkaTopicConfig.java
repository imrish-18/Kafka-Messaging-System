package com.rishabh.order.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.topic.order-events}")
    private String topicName;

    @Bean
    public NewTopic topic() {
        return TopicBuilder
                .name(topicName).build();
    }


    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name("payment-status").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic inventoryTopic() {
        return TopicBuilder.name("inventory-status").partitions(3).replicas(1).build();
    }
//    @Bean
//    public List<NewTopic> topics() {
//        return List.of(
//                TopicBuilder.name("order_topics").partitions(3).replicas(1).build(),
//                TopicBuilder.name("payment_topics").partitions(3).replicas(1).build(),
//                TopicBuilder.name("inventory_topics").partitions(3).replicas(1).build()
//        );
//    }

    //        @Bean
//    public NewTopic orderTopic() {
//        return TopicBuilder.name("order_topics").partitions(3).replicas(1).build();
//    }
}
