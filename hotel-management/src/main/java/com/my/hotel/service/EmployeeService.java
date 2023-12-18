package com.my.hotel.service;

import com.my.hotel.dto.EmployeeDto;

import javax.transaction.Transactional;
import java.util.List;

public interface EmployeeService {

	List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeInfo(String username);

    @Transactional
    boolean createEmployee(EmployeeDto dto);
}
