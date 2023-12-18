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
public class CreateNewRoomClassPriceRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String roomClassId;
    private Double price;
    private Date appliedDate;
    private Date createdDate;
    private String createdBy;

}
