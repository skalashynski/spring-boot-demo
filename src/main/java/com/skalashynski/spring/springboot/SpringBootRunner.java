package com.skalashynski.spring.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
//@ComponentScan("com.example.demo")
//@EnableAutoConfiguration
public class SpringBootRunner {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootRunner.class, args);
	}
}
