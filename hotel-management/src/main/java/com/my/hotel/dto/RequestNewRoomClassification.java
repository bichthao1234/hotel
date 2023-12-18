package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RequestNewRoomClassification implements Serializable {
    private String id;
    private Integer guestNum;
    private String description;
    private RoomTypeDto roomTypeDto;
    private RoomKindDto roomKindDto;
    private List<ConvenienceDetailDto> convenienceIds;
    private Double priceRoomClassifications;
    private EmployeeDto employeeDto;
}