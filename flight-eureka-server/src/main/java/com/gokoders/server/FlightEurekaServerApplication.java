package com.gokoders.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class FlightEurekaServerApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightEurekaServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(FlightEurekaServerApplication.class, args);
		LOGGER.info("Flight Eureka Server application started");
	}

}
