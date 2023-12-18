package com.my.hotel.dto;

import com.my.hotel.entity.Employee;
import com.my.hotel.entity.RoomClassification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.my.hotel.entity.PriceRoomClassification}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRoomClassificationDto implements Serializable {
    private RoomClassificationDto roomClassification;
    private Date appliedDate;
    private Date createdDate;
    private Double price;
    private EmployeeDto createdBy;
}