package com.my.hotel.service;

import com.my.hotel.dto.ReservationDetailDto;
import com.my.hotel.dto.ReservationDto;
import com.my.hotel.dto.request.BookRoomRequestDto;
import com.my.hotel.dto.request.ChangeDateReservationRequestDto;
import com.my.hotel.dto.request.CheckRoomRequestDto;
import com.my.hotel.dto.request.GetReservationListRequestDto;
import com.my.hotel.dto.request.PriceRoomClassRequestDto;
import com.my.hotel.dto.response.PriceRoomClassResponseDto;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReservationService {

	@Transactional
	boolean bookRoom(BookRoomRequestDto requestDto) throws Throwable;

    List<Map<String, Object>> checkRoom(CheckRoomRequestDto requestDto) throws Throwable;

    Double getRoomClassPrice(Integer roomClassId, Date startDate);

    PriceRoomClassResponseDto getRoomClassPrice(PriceRoomClassRequestDto requestDto);

    String getImagePathById(Integer roomClassId);

    List<ReservationDto> getReservationList(GetReservationListRequestDto requestDto) throws Throwable;

    List<ReservationDetailDto> getReservationDetail(Integer reservationId) throws Throwable;

    Integer getRentalSlipId(Integer reservationId) throws Throwable;

    @Transactional
    boolean cancelReservation(Integer reservationId, String employeeId) throws Throwable;

    @Transactional
    boolean changeDateRangeReservation(ChangeDateReservationRequestDto requestDto) throws Throwable;
}
