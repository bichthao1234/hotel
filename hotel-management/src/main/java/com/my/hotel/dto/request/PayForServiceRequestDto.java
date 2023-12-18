package com.my.hotel.dto.request;

import com.my.hotel.dto.ServiceDto;
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
public class PayForServiceRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer rentalSlipDetailId;

    private String serviceId;

}
