package com.my.hotel.entity.key;

import com.my.hotel.entity.Customer;
import com.my.hotel.entity.Department;
import com.my.hotel.entity.RentalSlipDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GuestManifestId implements Serializable {

    private Customer customer;
    private RentalSlipDetail rentalSlipDetail;

}
