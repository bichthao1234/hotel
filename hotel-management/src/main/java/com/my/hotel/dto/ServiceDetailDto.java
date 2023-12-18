package com.my.hotel.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.my.hotel.entity.ServiceDetail}
 */
@Data
@Getter   
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ServiceDetailDto implements Serializable {
    private ServiceDto service;
    private RentalSlipDetailDto rentalSlipDetail;
    private Integer quantity;
    private Float price;
    private Integer status;
    private String note;
    private Date usingDate;
}