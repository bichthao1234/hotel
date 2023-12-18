package com.my.hotel.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SystemConfig {

	private String applicationEnvironment;

	private String jwtIssuer;

	private String jwtSubject;

	private String jwtKey;

	private Integer jwtExpiration;

}
