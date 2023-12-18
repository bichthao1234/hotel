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
public class GetReservationListRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date startDateFrom;
    private String startDateFromString;
    private Date startDateTo;
    private String startDateToString;
    private Date createdDateFrom;
    private String createdDateFromString;
    private Date createdDateTo;
    private String createdDateToString;
    private String customerId;
    private String status;

    public void formatDate() {
        this.startDateFromString = Utilities.formatDate(startDateFrom, Constants.DATE_FORMAT.FORMAT_1);
        this.startDateToString = Utilities.formatDate(startDateTo, Constants.DATE_FORMAT.FORMAT_1);
        this.createdDateFromString = Utilities.formatDate(createdDateFrom, Constants.DATE_FORMAT.FORMAT_1);
        this.createdDateToString = Utilities.formatDate(createdDateTo, Constants.DATE_FORMAT.FORMAT_1);
    }

}
