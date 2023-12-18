package com.my.hotel.security;

import com.my.hotel.dto.EmployeeDto;
import com.my.hotel.dto.PermissionDto;
import com.my.hotel.entity.Employee;
import com.my.hotel.repo.EmployeeRepo;
import com.my.hotel.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private EmployeeRepo employeeRepo;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String pwd = authentication.getCredentials().toString();
		Employee employee = this.employeeRepo.findByUsername(username);
		if (employee != null) {
			if (this.passwordEncoder.matches(pwd, employee.getPassword())) {
				EmployeeDto employeeInfo = this.employeeService.getEmployeeInfo(username);
				List<PermissionDto> permissions = employeeInfo.getPermissionList();

				Set<GrantedAuthority> authorities = permissions == null ? new HashSet<>() :
						convertToGrantedAuthorities(permissions);

				return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
			} else {
				throw new BadCredentialsException("Invalid password!");
			}
		} else {
			throw new BadCredentialsException("Unrecognized username or password!");
		}
	}

	private Set<GrantedAuthority> convertToGrantedAuthorities(List<PermissionDto> permissions) {
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (PermissionDto authority : permissions) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getId()));
		}
		return grantedAuthorities;
	}

	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}
}
