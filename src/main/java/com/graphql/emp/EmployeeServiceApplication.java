package com.graphql.emp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class EmployeeServiceApplication {

	public static void main(String[] args) {
		log.info("***************Service Started **************");
		SpringApplication.run(EmployeeServiceApplication.class, args);
	}

}
