package com.my.hotel.dto;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.entity.Management;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private EmployeeDto manager;
    private Date managementStartDate;

}
