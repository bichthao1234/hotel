package com.my.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link com.my.hotel.entity.Invoice}
 */
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto implements Serializable {
    private String id;
    private Date createdDate;
    private EmployeeDto createdBy;
}