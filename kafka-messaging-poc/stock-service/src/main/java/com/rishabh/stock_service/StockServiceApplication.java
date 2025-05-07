package com.rishabh.stock_service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class StockServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(StockServiceApplication.class, args);

		log.info("this is stock service ...");
	}

}
