package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.RoomType}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeDto implements Serializable {
    String id;
    String name;
}