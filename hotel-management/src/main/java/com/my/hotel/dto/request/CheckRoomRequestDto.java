package com.my.hotel.dto.request;

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
public class CheckRoomRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date startDate;
    private Date endDate;
    private String roomClass;
    private String numberOfRoom;
    private Integer reservationId;

}
