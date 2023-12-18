package com.my.hotel.service;

import com.my.hotel.dto.DepartmentDto;

import javax.transaction.Transactional;
import java.util.List;

public interface DepartmentService {

	List<DepartmentDto> getAllDepartments();

    @Transactional
    boolean createDepartment(DepartmentDto dto);
}
