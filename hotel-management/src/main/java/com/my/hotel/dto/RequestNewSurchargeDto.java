package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RequestNewSurchargeDto implements Serializable {
    private String id;
    private String name;
    private String description;
    private String unit;
    private Double priceSurcharge;
    private String createdBy;
}