package com.my.hotel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class EmployeeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthday;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String image;
    private String departmentId;
    private Boolean isManager;
    private List<PermissionDto> permissionList;

}
