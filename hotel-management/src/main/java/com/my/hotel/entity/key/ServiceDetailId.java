package com.my.hotel.entity.key;

import com.my.hotel.entity.Convenience;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.RoomClassification;
import com.my.hotel.entity.Service;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceDetailId implements Serializable {

    private Service service;
    private RentalSlipDetail rentalSlipDetail;

}
