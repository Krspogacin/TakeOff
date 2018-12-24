package org.isa.takeoff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TakeOffApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakeOffApplication.class, args);
	}
}
