package com.project.MediFlow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MediFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediFlowApplication.class, args);
	}

}
