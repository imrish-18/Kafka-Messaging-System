package com.rishabh.User.handler;

import com.rishabh.User.Respository.EventRepository;
import com.rishabh.User.Respository.UserRepository;
import com.rishabh.User.Service.KafkaProducerService;
import com.rishabh.User.model.Event;
import com.rishabh.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private KafkaTemplate<String, Event> eventKafkaTemplate;


    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(userRepository.findAll(), User.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        String id = request.pathVariable("id");
        return userRepository.findById(id)
                .flatMap(user -> ServerResponse.ok().bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userRepository::save)
                .flatMap(saved -> ServerResponse.ok().bodyValue(saved));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return userRepository.deleteById(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> createKafkaEvent(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userRepository::save)
                .doOnNext(kafkaProducerService::publishUserCreatedEvent)
                .flatMap(saved -> ServerResponse.ok().bodyValue(saved));
    }

//    public Flux<Integer> streamNumbers() {
//        return Flux.range(1, 1000)
//                .delayElements(Duration.ofMillis(100))  // simulate streaming
//                .doOnNext(i -> System.out.println("Server emitted: " + i))
//                .onBackpressureBuffer(50,
//                        dropped -> System.out.println("Dropped: " + dropped));
//    }

    public Mono<ServerResponse> streamEvents(ServerRequest request) {
        Flux<Event> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new Event(UUID.randomUUID().toString(), Instant.now()))
                .onBackpressureDrop() // Apply backpressure strategy
                .doOnNext(event -> {
                    // Kafka publishing
                    eventKafkaTemplate.send("event-topic", event);

                    // MongoDB persistence
                    eventRepository.save(event).subscribe();
                });

        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(eventFlux, Event.class);
    }
}
