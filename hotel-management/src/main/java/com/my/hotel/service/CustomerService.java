package com.my.hotel.service;

import com.my.hotel.dto.CustomerDto;
import com.my.hotel.dto.EmployeeDto;

import javax.transaction.Transactional;
import java.util.List;

public interface CustomerService {

	List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(String id);

    @Transactional
    boolean saveCustomers(List<CustomerDto> customerDtos);

    @Transactional
    boolean createCustomer(CustomerDto dto);
}
