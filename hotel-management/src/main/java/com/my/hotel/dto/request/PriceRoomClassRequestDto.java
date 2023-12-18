package com.my.hotel.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRoomClassRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer roomClassId;
    private Date startDate;
}
