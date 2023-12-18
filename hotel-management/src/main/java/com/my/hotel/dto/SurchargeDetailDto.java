package com.my.hotel.dto;

import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.Surcharge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.SurchargeDetail}
 */
@Data
@Getter   
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SurchargeDetailDto implements Serializable {
    private SurchargeDto surcharge;
    private RentalSlipDetailDto rentalSlipDetail;
    private Integer quantity;
    private Float price;
    private Integer status;
}