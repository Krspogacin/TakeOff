package org.isa.takeoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableAsync
public class TakeOffApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakeOffApplication.class, args);
	}
}
