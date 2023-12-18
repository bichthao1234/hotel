package com.my.hotel.service;

import com.my.hotel.dto.RequestNewSurchargeDto;
import com.my.hotel.dto.SurchargeDetailDto;
import com.my.hotel.dto.SurchargeDto;
import com.my.hotel.dto.request.CreateNewSurchargePriceRequestDto;

import java.util.List;

public interface SurchargeService {
    List<SurchargeDto> getAll();

    SurchargeDto getById(String id);

    boolean saveWithRentalSlip(SurchargeDetailDto surchargeDetailDto) throws Exception;

    boolean createNewPrice(CreateNewSurchargePriceRequestDto requestDto);

    String createNewSurcharge(RequestNewSurchargeDto requestDto);

    boolean save(SurchargeDto surchargeDto);

    boolean delete(String id);
}
