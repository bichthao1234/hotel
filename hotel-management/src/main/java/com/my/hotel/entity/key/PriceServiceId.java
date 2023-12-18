package com.my.hotel.entity.key;

import com.my.hotel.entity.RoomClassification;
import com.my.hotel.entity.Service;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PriceServiceId implements Serializable {

    private Service service;
    private Date appliedDate;

}
