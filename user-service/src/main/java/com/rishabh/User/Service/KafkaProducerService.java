package com.rishabh.User.Service;

import com.rishabh.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "user-events";

    public void publishUserCreatedEvent(User user) {
        String message = "User created: " + user.getUserId() + ", " + user.getUser_email();
        kafkaTemplate.send(TOPIC, message);
    }
}

