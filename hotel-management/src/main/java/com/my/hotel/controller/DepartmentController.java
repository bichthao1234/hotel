package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.DepartmentDto;
import com.my.hotel.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_DEPARTMENT)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> createDepartment(@RequestBody DepartmentDto department) {
        log.info("API createDepartment STARTED with INPUT department={}", department);
        boolean result = departmentService.createDepartment(department);
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        log.info("API createDepartment END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
