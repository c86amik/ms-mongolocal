package com.gokoders.registration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@OpenAPIDefinition(info =
	@Info(title = "Flight Registration Service API", version = "1.0", description = "Documentation Flight Registration Service API v1.0")
)
public class RegistrationServiceApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RegistrationServiceApplication.class, args);
		LOGGER.info("UserRegistration Microservice Started");
	}

}
