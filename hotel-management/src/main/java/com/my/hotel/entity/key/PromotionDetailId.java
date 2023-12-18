package com.my.hotel.entity.key;

import com.my.hotel.entity.Promotion;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.RoomClassification;
import com.my.hotel.entity.Service;
import lombok.Data;

import java.io.Serializable;

@Data
public class PromotionDetailId implements Serializable {

    private Promotion promotion;
    private RoomClassification roomClassification;

}
