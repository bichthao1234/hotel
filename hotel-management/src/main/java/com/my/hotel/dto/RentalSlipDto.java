package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO for {@link com.my.hotel.entity.RentalSlip}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalSlipDto implements Serializable {
    private Integer id;
    private Date createdDate;
    private CustomerDto customer;
    private EmployeeDto createdBy;
    private ReservationDto reservation;
    private List<InvoiceDto> invoices;
}