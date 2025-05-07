package com.rishabh.User.Router;

import com.rishabh.User.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class UserRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UserHandler handler) {
        return RouterFunctions.route()
                .GET("/users", handler::getAll)
                .GET("/stream/events",handler::streamEvents)
                .GET("/users/{id}", handler::getById)
                .POST("/users", handler::create)
                .DELETE("/users/{id}", handler::delete)
                .POST("/saveUsers",handler::createKafkaEvent)
                .build();
    }
}

