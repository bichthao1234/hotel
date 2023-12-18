package com.my.hotel.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CustomErrorMessage> handleCustomException(CustomException ex) {
		CustomErrorMessage errorResponse = new CustomErrorMessage(
				ex.getStatus(), ex.getCode(), ex.getMessage(), LocalDateTime.now());
		return ResponseEntity.status(ex.getStatus()).body(errorResponse);
	}
}
