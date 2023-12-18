package com.my.hotel.entity.key;

import com.my.hotel.entity.Department;
import com.my.hotel.entity.Permission;
import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentPermissionId implements Serializable {

    private Permission permission;

    private Department department;

}
