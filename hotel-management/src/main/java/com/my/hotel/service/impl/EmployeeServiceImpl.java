package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.EmployeeDto;
import com.my.hotel.dto.PermissionDto;
import com.my.hotel.entity.Department;
import com.my.hotel.entity.DepartmentPermission;
import com.my.hotel.entity.Employee;
import com.my.hotel.repo.EmployeeRepo;
import com.my.hotel.service.EmployeeService;
import com.my.hotel.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepo repo;

	@Override
	public List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> ret = new ArrayList<>();
		List<Employee> employees = this.repo.findAll();
		if (!employees.isEmpty()) {
			for (Employee employee : employees) {
				EmployeeDto employeeDto = ObjectMapperUtils.map(employee, EmployeeDto.class);
				employeeDto.setDepartmentId(employee.getDepartment().getId());
				ret.add(employeeDto);
			}
		}
		return ret;
	}

	@Override
	public EmployeeDto getEmployeeInfo(String username) {
		EmployeeDto ret = null;
		Employee employee = this.repo.findByUsername(username);
		if (employee != null) {
			ret = ObjectMapperUtils.map(employee, EmployeeDto.class);
			ret.setIsManager(Utilities.nonEmptyList(employee.getManagements()));
		}
		if (ret != null) {
			Department department = employee.getDepartment();
			if (department != null) {
				List<DepartmentPermission> departmentPermissions = department.getDepartmentPermissions();
				List<PermissionDto> permissionDtos = departmentPermissions.stream().map(DepartmentPermission::getPermission)
						.map(permission -> ObjectMapperUtils.map(permission, PermissionDto.class))
						.collect(Collectors.toList());

				ret.setPermissionList(permissionDtos);
			}
		}
		return ret;
	}

	@Override
	@Transactional
	public boolean createEmployee(EmployeeDto dto) {
		try {
			Employee employee = ObjectMapperUtils.map(dto, Employee.class);
			this.repo.save(employee);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
