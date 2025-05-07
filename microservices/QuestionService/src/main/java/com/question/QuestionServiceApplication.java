package com.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QuestionServiceApplication {

	private static final Logger log = LoggerFactory.getLogger(QuestionServiceApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(QuestionServiceApplication.class, args);
		log.info("this is question service ....");
	}

}
