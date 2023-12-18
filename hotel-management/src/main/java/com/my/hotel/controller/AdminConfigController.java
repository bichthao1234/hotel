package com.my.hotel.controller;

import com.my.hotel.common.JsonUtils;
import com.my.hotel.common.Routes;
import com.my.hotel.configuration.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_ADMIN_CONFIG)
public class AdminConfigController {

	@Autowired
	public Environment environment;
	
	@Autowired
	private SystemConfig systemConfig;

	@GetMapping("/system-config")
	public SystemConfig systemConfig() {
		return this.systemConfig;
	}
	
	@GetMapping("/env-check")
	public Map<String, Object> envCheck(@RequestParam(required = false) List<String> envs) {
		StopWatch watch = new StopWatch();
		watch.start();
		Map<String, Object> response = this.getEnvironmentConfig(envs);
		watch.stop();
		log.info("envCheck API took {} ms ~= {} s ~= ", watch.getTotalTimeMillis(), watch.getTotalTimeSeconds());
		log.info("envCheck:: response {}", JsonUtils.convertToString(response));
		return response;
	}

	public Map<String, Object> getEnvironmentConfig(List<String> envs) {
		Map<String, Object> ret = new HashMap<>();
		for (String env : envs) {
			String val = null;
			try {
				val = environment.getProperty(env);
			} catch (Exception ex) {
				log.info("getEnvironmentConfig:: env {} - ex {}", env, ex.getMessage());
			}
			ret.put(env, val);
		}
		return ret;
	}
}
