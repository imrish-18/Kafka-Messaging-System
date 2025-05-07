package com.redisCache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class RedisCacheApplication {

	public static void main(String[] args) {

		SpringApplication.run(RedisCacheApplication.class, args);
		System.out.println("Hello This is Redis Cache Impl application...");
	}

}
