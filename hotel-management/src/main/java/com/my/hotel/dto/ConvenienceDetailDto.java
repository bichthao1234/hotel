package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.ConvenienceDetail}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvenienceDetailDto implements Serializable {
    ConvenienceDto convenience;
    RoomClassificationDto roomClassification;
    Integer quantity;
    String description;
}