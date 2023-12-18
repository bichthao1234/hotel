package com.my.hotel.repo;

import com.my.hotel.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, String> {

    Employee findByUsername(String username);

}
