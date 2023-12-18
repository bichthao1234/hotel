package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.my.hotel.entity.Surcharge}
 */
@Data
@Getter   
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SurchargeDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private Integer quantity;
    private String unit;
    private Double priceSurcharge;
    private Boolean canDelete;
    private List<PriceSurchargeDto> priceSurchargeDtos;
    private Boolean status;
}