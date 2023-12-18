package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtWrapperDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String employeeId;
	private String fullName;
	private Boolean isManager;
	private String token;

	private String refreshToken;

	private List<String> permissions;

}