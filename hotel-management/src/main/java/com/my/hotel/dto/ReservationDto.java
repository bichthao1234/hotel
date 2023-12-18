package com.my.hotel.dto;

import com.my.hotel.common.Status;
import com.my.hotel.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.my.hotel.entity.Reservation}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto implements Serializable {
    private String id;
    private Date createdDate;
    private Date startDate;
    private Date endDate;
    private Integer stayingDay;
    private Status.StatusWrapper status;
    private Float deposit;
    private String customerId;
    private String customerDisplayName;

    public ReservationDto(Object[] objects) {
        this.id = Utilities.parseString(objects[0]);
        this.createdDate = Utilities.parseDate(objects[1]);
        this.startDate = Utilities.parseDate(objects[2]);
        this.endDate = Utilities.parseDate(objects[3]);
        this.stayingDay = Utilities.parseInt(objects[4]);
        this.customerId = Utilities.parseString(objects[5]);
        this.customerDisplayName = Utilities.parseString(objects[6]);
        this.deposit = Utilities.parseFloat(objects[7]);
        this.status = Status.getStatusName(Utilities.parseInt(objects[8]));
    }
}