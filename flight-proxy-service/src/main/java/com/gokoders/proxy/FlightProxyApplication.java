package com.gokoders.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@EnableSwagger2
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class FlightProxyApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightProxyApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(FlightProxyApplication.class, args);
		LOGGER.info("Flight Proxy Service application started");
	}
}
