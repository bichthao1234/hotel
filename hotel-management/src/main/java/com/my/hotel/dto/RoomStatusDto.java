package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.RoomStatus}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusDto implements Serializable {
    String id;
    String name;
}