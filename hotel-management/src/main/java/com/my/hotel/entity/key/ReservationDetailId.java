package com.my.hotel.entity.key;

import com.my.hotel.entity.Reservation;
import com.my.hotel.entity.RoomClassification;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReservationDetailId implements Serializable {

    private Reservation reservation;
    private RoomClassification roomClassification;

}
