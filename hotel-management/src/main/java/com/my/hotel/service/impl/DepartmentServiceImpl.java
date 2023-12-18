package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.DepartmentDto;
import com.my.hotel.dto.ManagementDto;
import com.my.hotel.entity.Department;
import com.my.hotel.repo.DepartmentRepo;
import com.my.hotel.service.DepartmentService;
import com.my.hotel.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepo repo;

	@Override
	public List<DepartmentDto> getAllDepartments() {
		List<DepartmentDto> ret = new ArrayList<>();
		List<Department> departments = this.repo.findAll();
		if (!departments.isEmpty()) {
			for (Department department : departments) {
				DepartmentDto departmentDto = ObjectMapperUtils.map(department, DepartmentDto.class);
				if (Utilities.nonEmptyList(department.getManagements())) {
					List<ManagementDto> managers = ObjectMapperUtils.mapAll(department.getManagements(), ManagementDto.class);
					departmentDto.setManagers(managers);
					ret.add(departmentDto);
				}
			}
		}
		return ret;
	}

	@Override
	@Transactional
	public boolean createDepartment(DepartmentDto dto) {
		try {
			Department customer = ObjectMapperUtils.map(dto, Department.class);
			this.repo.save(customer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
