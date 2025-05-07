package com.rishabh.order_service.config;



import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.List;

@Configuration
public class KafkaTopicConfig {

//    @Value("${spring.kafka.topic.name}")
//    private String topicName;
//
//    @Bean
//    public NewTopic topic(){
//        return TopicBuilder
//                .name(topicName).build();
//    }
//    @Bean
//    public NewTopic orderTopic() {
//        return TopicBuilder.name("inventory-events").partitions(3).replicas(1).build();
//    }
//
//    @Bean
//    public NewTopic paymentTopic() {
//        return TopicBuilder.name("payment-events").partitions(3).replicas(1).build();
//    }
//
//    @Bean
//    public NewTopic inventoryTopic() {
//        return TopicBuilder.name("payment-responses").partitions(3).replicas(1).build();
//    }
//
//    @Bean
//    public NewTopic orderTopic() {
//        return TopicBuilder.name("inventory-events").partitions(3).replicas(1).build();
//    }
//
//    @Bean
//    public NewTopic paymentTopic() {
//        return TopicBuilder.name("payment-events").partitions(3).replicas(1).build();
//    }
//
//    @Bean
//    public NewTopic inventoryTopic() {
//        return TopicBuilder.name("payment-responses").partitions(3).replicas(1).build();
//    }
    @Bean
    public List<NewTopic> topics() {
        return List.of(
                TopicBuilder.name("payment-responses").partitions(3).replicas(1).build(),
                TopicBuilder.name("payment-events").partitions(3).replicas(1).build(),
                TopicBuilder.name("inventory-events").partitions(3).replicas(1).build(),
                TopicBuilder.name("inventory-responses").partitions(3).replicas(1).build()
        );
    }

}
