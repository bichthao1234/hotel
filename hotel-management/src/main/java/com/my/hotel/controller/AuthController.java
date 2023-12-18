package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.JwtWrapperDto;
import com.my.hotel.dto.LoginRequestDto;
import com.my.hotel.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_AUTH)
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
		StopWatch watch = new StopWatch();
		JwtWrapperDto response = new JwtWrapperDto();
		watch.start();
		try	{
			response = this.authService.login(loginRequest.getUsername(), loginRequest.getPassword());
			watch.stop();
			log.info("login API took {} ms ~= {} s ~= ", watch.getTotalTimeMillis(), watch.getTotalTimeSeconds());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			watch.stop();
			log.info("login API took {} ms ~= {} s ~= ", watch.getTotalTimeMillis(), watch.getTotalTimeSeconds());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
