package com.my.hotel.dto;

import com.my.hotel.entity.SurchargeDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalSlipDetailSaveDto {
    Integer rentalSlipDetailId;
    List<ServiceDetailDto> serviceDetailDtos;
    List<SurchargeDetailDto> surchargeDetailDtos;
}
