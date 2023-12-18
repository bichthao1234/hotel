package com.my.hotel.dto;

import com.my.hotel.common.Status;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.ReservationDetail;
import com.my.hotel.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * DTO for {@link com.my.hotel.entity.ReservationDetail}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailDto implements Serializable {
    private String reservationId;
    private String roomClassId;
    private String roomClassName;
    private String roomId;
    private Integer numberOfRooms;
    private Date startDate;
    private Date endDate;
    private Double price;
    private Integer status;

    public ReservationDetailDto(ReservationDetail reservationDetail) {
        this.reservationId = Utilities.parseString(reservationDetail.getReservation().getId());
        this.roomClassName = reservationDetail.getRoomClassification().getRoomKind().getName() + " "
                + reservationDetail.getRoomClassification().getRoomType().getName();
        this.startDate = reservationDetail.getReservation().getStartDate();
        this.endDate = reservationDetail.getReservation().getEndDate();
        this.roomClassId = reservationDetail.getRoomClassification().getId();
        this.numberOfRooms = reservationDetail.getNumberOfRooms();
        this.price = reservationDetail.getPrice();
    }

    public ReservationDetailDto(RentalSlipDetail rentalSlipDetail) {
        this.reservationId = Utilities.parseString(rentalSlipDetail.getRentalSlip().getReservation().getId());
        this.roomClassId = rentalSlipDetail.getRoom().getRoomClassification().getId();
        this.roomId = rentalSlipDetail.getRoom().getId();
        this.roomClassName = rentalSlipDetail.getRoom().getRoomClassification().getRoomKind().getName() + " "
                + rentalSlipDetail.getRoom().getRoomClassification().getRoomType().getName();
        this.startDate = rentalSlipDetail.getRentalSlip().getReservation().getStartDate();
        this.endDate = rentalSlipDetail.getRentalSlip().getReservation().getEndDate();
        this.numberOfRooms = (int) rentalSlipDetail.getRentalSlip().getRentalSlipDetails().stream()
                .filter(item -> item.getRoom().getRoomClassification().getId().equals(this.roomClassId)).count();
        this.price = Double.valueOf(rentalSlipDetail.getPrice());
        this.status = 1;
    }
}