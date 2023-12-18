package com.my.hotel.service;

import com.my.hotel.common.JsonUtils;
import com.my.hotel.configuration.SystemConfig;
import com.my.hotel.dto.EmployeeDto;
import com.my.hotel.dto.JwtWrapperDto;
import com.my.hotel.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private SystemConfig systemConfig;

	@Autowired
	private EmployeeService employeeService;
	
	public JwtWrapperDto login(String username, String password) {
		try {
			log.info("login:: username {} - password {}", username, password);

			Authentication authentication = this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));

			String issuer = systemConfig.getJwtIssuer();
			String subject = systemConfig.getJwtSubject();
			String key = systemConfig.getJwtKey();
			Integer expiration = systemConfig.getJwtExpiration();

			String authorities = populateAuthorities(authentication.getAuthorities());
			String jwtToken = JwtUtils.generateTokenFromUsername(issuer, subject, username, authorities,
					key, expiration);

			List<String> permissions = authentication.getAuthorities().isEmpty() ? new ArrayList<>() :
					authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
							.collect(Collectors.toList());

			EmployeeDto employeeInfo = employeeService.getEmployeeInfo(username);
			String fullName = employeeInfo.getFirstName() + " " + employeeInfo.getLastName();

			String refreshToken = "";
			JwtWrapperDto response = new JwtWrapperDto(username, employeeInfo.getId(), fullName, employeeInfo.getIsManager(), jwtToken, refreshToken, permissions);

			log.info("login:: username {} - response {}", username, JsonUtils.convertToString(response));
			return response;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}
	
	private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
}
