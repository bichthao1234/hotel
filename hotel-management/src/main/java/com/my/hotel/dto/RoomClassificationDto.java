package com.my.hotel.dto;

import com.my.hotel.entity.ConvenienceDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.my.hotel.entity.RoomClassification}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomClassificationDto implements Serializable {
    private String id;
    private Integer guestNum;
    private String description;
    private Double price;
    private RoomTypeDto roomType;
    private RoomKindDto roomKind;
    private List<ConvenienceDetailDto> convenienceDetailDtos;
    private List<PriceRoomClassificationDto> priceRoomClassificationDtos;
}