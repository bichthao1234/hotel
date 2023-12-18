package com.my.hotel.entity.key;

import com.my.hotel.entity.Department;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ManagementId implements Serializable {

    private Department department;
    private Date managementStartDate;

}
