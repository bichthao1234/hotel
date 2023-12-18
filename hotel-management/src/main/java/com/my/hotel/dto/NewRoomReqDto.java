package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link com.my.hotel.entity.Room}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewRoomReqDto implements Serializable {
    private String id;
    private Integer floor;
    private String roomClassificationId;
    private String roomStatusId;
}