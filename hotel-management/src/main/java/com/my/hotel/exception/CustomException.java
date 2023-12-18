package com.my.hotel.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
	
	private String code;
	private String message;

	public CustomException(HttpStatus status, String code, String message) {
		super(message);
		this.status = status;
		this.code = code;
		this.message = message;
	}
}
