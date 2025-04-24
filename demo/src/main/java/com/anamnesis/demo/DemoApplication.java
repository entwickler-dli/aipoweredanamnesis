package com.anamnesis.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class DemoApplication {

	public static final Logger log = Logger.getLogger(DemoApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
