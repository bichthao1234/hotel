package com.my.hotel.dto;

import com.my.hotel.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

/**
 * DTO for {@link com.my.hotel.entity.RentalSlipDetail}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalSlipDetailDto implements Serializable {
    private Integer id;
    private Time arrivalTime;
    private Date arrivalDate;
    private Date departureDate;
    private String paymentStatus;
    private RentalSlipDto rentalSlip;
    private InvoiceDto invoice;
    private RoomDto room;

    public RentalSlipDetailDto(Object[] obj) {
        this.id = Utilities.parseInt(obj[0]);
        this.arrivalTime = Utilities.parseTime(obj[1]);
        this.arrivalDate = Utilities.parseDate(obj[2]);
        this.departureDate = Utilities.parseDate(obj[3]);
        this.paymentStatus = Utilities.parseString(obj[4]);
        this.rentalSlip = new RentalSlipDto();
        this.rentalSlip.setId(Utilities.parseInt(obj[5]));
        this.invoice = new InvoiceDto();
        this.invoice.setId(Utilities.parseString(obj[6]));
        this.room = new RoomDto();
        this.room.setId(Utilities.parseString(obj[7]));
    }
}