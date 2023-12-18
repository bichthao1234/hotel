package com.my.hotel.configuration;

import com.my.hotel.common.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.my.hotel")
public class SystemPropertiesConfig {

	@Value("${application.environment}")
	private String environment;

	@Value("${core.security.jwt.issuer}")
	private String jwtIssuer;
	
	@Value("${core.security.jwt.subject}")
	private String jwtSubject;
	
	@Value("${core.security.jwt.key}")
	private String jwtKey;
	
	@Value("${core.security.jwt.expiration}")
	private String jwtExpiration;
	
	@Bean
	public SystemConfig customProperties() {
		Constants.JWT_KEY = jwtKey;
		
		SystemConfig ret = SystemConfig.builder()
				.applicationEnvironment(environment)
				.jwtIssuer(jwtIssuer)
				.jwtSubject(jwtSubject)
				.jwtKey(jwtKey)
				.jwtExpiration(Integer.parseInt(jwtExpiration))
			.build();
		return ret;
	}

}
