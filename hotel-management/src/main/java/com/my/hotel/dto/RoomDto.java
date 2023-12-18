package com.my.hotel.dto;

import com.my.hotel.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.Room}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto implements Serializable {
    private String id;
    private Integer floor;
    private String roomClassId;
    private String roomStatusName;
    private String roomStatusId;
    private RoomStatusDto roomStatus;
    private String roomKind;
    private String roomType;
    private Float price;
    private Float priceAfterPromotion;

    public RoomDto(Object[] objects) {
        this.id = Utilities.parseString(objects[0]);
        this.floor = Utilities.parseInt(objects[1]);
        this.roomClassId = Utilities.parseString(objects[2]);
        this.roomStatusId = Utilities.parseString(objects[3]);
        this.roomStatusName = Utilities.parseString(objects[4]);
        this.roomKind = Utilities.parseString(objects[5]);
        this.roomType = Utilities.parseString(objects[6]);
        this.price = Utilities.parseFloat(objects[8]);
        this.priceAfterPromotion = Utilities.parseFloat(objects[9]);
    }
}