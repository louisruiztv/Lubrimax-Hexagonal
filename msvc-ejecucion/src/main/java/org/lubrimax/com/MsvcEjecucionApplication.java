package org.lubrimax.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsvcEjecucionApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvcEjecucionApplication.class, args);
	}

}
