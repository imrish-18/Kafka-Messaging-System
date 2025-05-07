package com.rishabh.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import reactor.core.publisher.Flux;

@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
public class UserServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(UserServiceApplication.class, args);
		log.info("this is user service ...");
		Flux<Integer> source = Flux.range(1, 1000)
				.log()
				.onBackpressureBuffer(10, i -> System.out.println("Dropped: " + i));

		source
				.subscribe(
						data -> {
							try {
								Thread.sleep(50); // simulate slow consumer
								System.out.println("Received: " + data);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						},
						err -> System.out.println("Error: " + err),
						() -> System.out.println("Done")
				);

	}

}
