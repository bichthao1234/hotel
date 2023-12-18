package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.ImageRoomClassification}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ImageRoomClassificationDto implements Serializable {
    private String id;
    private String url;
    private RoomClassificationDto roomClassificationDto;
}