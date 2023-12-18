package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.CustomerDto;
import com.my.hotel.dto.EmployeeDto;
import com.my.hotel.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_EMPLOYEE)
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

//    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    @GetMapping("/get-employee/{username}")
    public ResponseEntity<?> getUsername(@PathVariable String username) {
        EmployeeDto employeeDto = this.employeeService.getEmployeeInfo(username);
        return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeDto employee) {
        log.info("API createEmployee STARTED with INPUT employee={}", employee);
        boolean result = employeeService.createEmployee(employee);
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        log.info("API createEmployee END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
