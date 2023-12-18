package com.my.hotel.dto.response;

import com.my.hotel.dto.RoomStatusDto;
import com.my.hotel.dto.ServiceDetailDto;
import com.my.hotel.dto.SurchargeDetailDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalSlipDetailResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer rentalSlipDetailId;
    private String roomId;
    private String roomName;
    private RoomStatusDto roomStatus;
    private Date arrivalDate;
    private Date departureDate;
    private Date checkInDate;
    private Date checkOutDate;
    private Long stayingDay;
    private Double roomPrice;
    private Float deposit;
    private Double total;
    private Double totalRoomPrice;
    private Double paidAmount;
    private List<SurchargeDetailDto> surcharge;
    private List<ServiceDetailDto> service;
    private Boolean status;

}
