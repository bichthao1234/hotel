package com.my.hotel.dto.request;

import com.my.hotel.common.Constants;
import com.my.hotel.utils.Utilities;
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
public class ChangeDateReservationRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer reservationId;
    private Date startDate;
    private Date endDate;

}
