package com.my.hotel.service;

import com.my.hotel.dto.RentalSlipDetailDto;
import com.my.hotel.dto.RentalSlipDetailSaveDto;
import com.my.hotel.dto.request.CheckOutByRoomRequestDto;
import com.my.hotel.dto.request.ConvertReservationRequestDto;
import com.my.hotel.dto.request.GetListRentalDetailRequestDto;
import com.my.hotel.dto.request.GetRevenueRequestDto;
import com.my.hotel.dto.request.QuickCheckInRequestDto;
import com.my.hotel.dto.response.RentalSlipDetailResponseDto;
import com.my.hotel.entity.RentalSlipDetail;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface RentalSlipService {


    boolean convertFromReservation(ConvertReservationRequestDto requestDto) throws Throwable;

    List<RentalSlipDetailDto> getRentalSlipDetailWithReservation(Integer reservationId) throws Throwable;

    List<Map<String, Object>> getRentalSlipList(String customerId) throws Throwable;

    List<Map<String, Object>> getRentalSlipListByCustomerId(String customerId) throws Throwable;

    List<RentalSlipDetailResponseDto> getRentalSlipDetailList(Integer rentalSlipId) throws Throwable;

    List<RentalSlipDetailResponseDto> getRentalSlipDetail(GetListRentalDetailRequestDto requestDto) throws Throwable;

    RentalSlipDetailResponseDto getRentalSlipDetailResponseDto(RentalSlipDetail detail);

    @Transactional
    boolean checkOutByRoom(CheckOutByRoomRequestDto requestDto) throws Throwable;

    @Transactional
    boolean quickCheckIn(QuickCheckInRequestDto requestDto) throws Throwable;

    boolean saveRentalSlipDetail(List<RentalSlipDetailSaveDto> requestDto) throws Exception;

    Map<String, Object> getRevenue(GetRevenueRequestDto requestDto);

}
