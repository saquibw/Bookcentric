package com.bookcentric.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties("app")
public class AppConfig {
	
	private String emailRecipient;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	/*@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}*/
}
