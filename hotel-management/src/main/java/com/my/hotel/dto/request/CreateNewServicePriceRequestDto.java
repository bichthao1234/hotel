package com.my.hotel.dto.request;

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
public class CreateNewServicePriceRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String serviceId;
    private Double price;
    private Date appliedDate;
    private String createdBy;

}
