package com.my.hotel.entity.key;

import com.my.hotel.entity.Convenience;
import com.my.hotel.entity.Department;
import com.my.hotel.entity.RoomClassification;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ConvenienceDetailId implements Serializable {

    private Convenience convenience;
    private RoomClassification roomClassification;

}
