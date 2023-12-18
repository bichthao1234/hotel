package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.CustomerDto;
import com.my.hotel.entity.Customer;
import com.my.hotel.repo.CustomerRepo;
import com.my.hotel.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepo repo;

	@Override
	public List<CustomerDto> getAllCustomers() {
		List<CustomerDto> ret = new ArrayList<>();
		List<Customer> customers = this.repo.findAll();
		if (!customers.isEmpty()) {
			for (Customer customer : customers) {
				CustomerDto customerDto = ObjectMapperUtils.map(customer, CustomerDto.class);
				ret.add(customerDto);
			}
		}
		return ret;
	}

	@Override
	public CustomerDto getCustomerById(String id) {
		CustomerDto customerDto = null;
		Optional<Customer> customerOptional = this.repo.findById(id);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			customerDto = ObjectMapperUtils.map(customer, CustomerDto.class);
		}
		return customerDto;
	}

	@Override
	@Transactional
	public boolean saveCustomers(List<CustomerDto> customerDtos) {
		try {
			List<Customer> customers = ObjectMapperUtils.mapAll(customerDtos, Customer.class);
			this.repo.saveAll(customers);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public boolean createCustomer(CustomerDto dto) {
		try {
			Customer customer = ObjectMapperUtils.map(dto, Customer.class);
			this.repo.save(customer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
