package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String code;
    private String name;

    private List<EmployeeDto> employees;
    private List<ManagementDto> managers;

}
