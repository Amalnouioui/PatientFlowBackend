package com.core.Parameterization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ParameterizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParameterizationApplication.class, args);
	}

}
