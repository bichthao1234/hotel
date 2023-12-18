package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.ConvenienceDto;
import com.my.hotel.service.ConvenienceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_CONVENIENCE)
//@PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
public class ConvenienceController {
    @Autowired
    private ConvenienceService convenienceService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllConveniences() {
        List<ConvenienceDto> convenienceDtos = this.convenienceService.getAllConvenience()
                .stream().sorted(Comparator.comparing(ConvenienceDto::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(convenienceDtos, HttpStatus.OK);
    }

    @PostMapping("/getByName")
    public ResponseEntity<?> geByName(@RequestBody Map<String, String> name) {
        List<ConvenienceDto> convenienceDtos = this.convenienceService.findByName(name.get("name"));
        return new ResponseEntity<>(convenienceDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewConvenience(@RequestBody ConvenienceDto convenienceDto) {
        log.info("API addNewConvenience STARTED with INPUT convenienceDto={}", convenienceDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = convenienceService.save(convenienceDto);
            response.put("result", result);
            log.info("API addNewConvenience END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API addNewConvenience END with an Error: ",  e.getMessage());
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editConvenience(@RequestBody ConvenienceDto convenienceDto) {
        log.info("API editConvenience STARTED with INPUT convenienceDto={}", convenienceDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = convenienceService.save(convenienceDto);
            response.put("result", result);
            log.info("API editConvenience END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API editConvenience END with an Error: ",  e.getMessage());
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
}
