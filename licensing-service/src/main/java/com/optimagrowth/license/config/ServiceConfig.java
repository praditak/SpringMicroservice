package com.optimagrowth.license.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
/*
 * custom properties can be injected using the @ConfigurationProperties
 * pulls all the example properties from the Spring Cloud Configuration Server
 *@ConfigurationProperties works best with hierarchical properties that all have the same prefix; 
 */
@ConfigurationProperties(prefix = "example")
@Getter
@Setter
public class ServiceConfig {

	private String property;

	public String exampleProperty;
}
