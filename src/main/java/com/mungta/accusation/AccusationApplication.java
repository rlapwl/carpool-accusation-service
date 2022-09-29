package com.mungta.accusation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AccusationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccusationApplication.class, args);
	}

}
