package com.suygecu.teamly_service;

import kotlin.jvm.Throws;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class TeamlyServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TeamlyServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}