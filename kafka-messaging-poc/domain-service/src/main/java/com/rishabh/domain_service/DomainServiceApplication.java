package com.rishabh.domain_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DomainServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(DomainServiceApplication.class, args);
		log.info("this is base domain service ...");
	}

}
