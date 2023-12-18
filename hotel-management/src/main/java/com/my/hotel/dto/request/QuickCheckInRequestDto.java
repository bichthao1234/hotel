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
public class QuickCheckInRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String employeeId;
    private Date startDate;
    private Date endDate;
    private Integer roomClassId;
    private String roomId;
    private Float price;
    private String mainCustomerId;
    private String customerIdString;
    private List<String> customerIdList;
}
