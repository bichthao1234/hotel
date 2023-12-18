package com.my.hotel.entity.key;

import com.my.hotel.entity.Service;
import com.my.hotel.entity.Surcharge;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PriceSurchargeId implements Serializable {

    private Surcharge surcharge;
    private Date appliedDate;

}
