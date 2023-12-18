package com.my.hotel.entity.key;

import com.my.hotel.entity.Convenience;
import com.my.hotel.entity.RoomClassification;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PriceRoomClassificationId implements Serializable {

    private RoomClassification roomClassification;
    private Date appliedDate;

}
