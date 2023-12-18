package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.CustomerDto;
import com.my.hotel.dto.DepartmentDto;
import com.my.hotel.dto.EmployeeDto;
import com.my.hotel.entity.Room;
import com.my.hotel.repo.ReservationRepo;
import com.my.hotel.repo.RoomRepo;
import com.my.hotel.service.CustomerService;
import com.my.hotel.service.DepartmentService;
import com.my.hotel.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_PUBLIC)
public class PublicController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private RoomRepo roomRepo;

	@Autowired
	private ReservationRepo reservationRepo;

	@GetMapping("/live")
	public String live() {
		log.info("live");
		return "ok test";
	}

	@GetMapping("/test")
	public List<EmployeeDto> test() {
		return employeeService.getAllEmployees();
	}

	@GetMapping("/test1")
	public List<DepartmentDto> test1() {
		return departmentService.getAllDepartments();
	}

	@GetMapping("/test2")
	public List<CustomerDto> test2() {
		return customerService.getAllCustomers();
	}

	@GetMapping("/test3")
	public void test3() {
		List<Room> all = roomRepo.findAll();
		System.out.println("done");
	}
}
