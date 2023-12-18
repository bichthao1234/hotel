package com.my.hotel.repo;

import com.my.hotel.entity.DepartmentPermission;
import com.my.hotel.entity.key.DepartmentPermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentPermissionRepo extends JpaRepository<DepartmentPermission, DepartmentPermissionId> {

}
