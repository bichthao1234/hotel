package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.my.hotel.entity.PriceSurcharge}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PriceSurchargeDto implements Serializable {
    private String surchargeId;
    private Date appliedDate;
    private Double price;
    private EmployeeDto createdBy;
}