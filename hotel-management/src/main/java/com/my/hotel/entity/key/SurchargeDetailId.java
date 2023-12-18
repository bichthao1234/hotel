package com.my.hotel.entity.key;

import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.Service;
import com.my.hotel.entity.Surcharge;
import lombok.Data;

import java.io.Serializable;

@Data
public class SurchargeDetailId implements Serializable {

    private Surcharge surcharge;
    private RentalSlipDetail rentalSlipDetail;

}
