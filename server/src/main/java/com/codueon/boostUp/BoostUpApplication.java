package com.codueon.boostUp;

import lombok.SneakyThrows;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@EnableFeignClients
@EnableBatchProcessing
@SpringBootApplication
public class BoostUpApplication {
	@SneakyThrows
	public static void main(String[] args) {
		Thread.sleep(10000);
		SpringApplication.run(BoostUpApplication.class, args);
	}

}
