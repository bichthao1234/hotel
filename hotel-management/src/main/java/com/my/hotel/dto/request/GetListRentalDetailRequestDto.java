package com.my.hotel.dto.request;

import com.my.hotel.dto.ServiceDto;
import com.my.hotel.dto.SurchargeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetListRentalDetailRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> rentalSlipDetailIdList;
    private Boolean isExportInvoice;
    private Boolean isForPayment;

}
