package com.quiz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuizServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(QuizServiceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(QuizServiceApplication.class, args);

		log.info("this is the quiz service ...");
	}

}
