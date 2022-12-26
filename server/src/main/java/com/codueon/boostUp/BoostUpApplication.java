package com.codueon.boostUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class BoostUpApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoostUpApplication.class, args);
	}

}
