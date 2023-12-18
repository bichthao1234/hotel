package com.my.hotel.service;

import com.my.hotel.dto.RequestNewRoomClassification;
import com.my.hotel.dto.RoomClassificationDto;
import com.my.hotel.dto.request.CreateNewRoomClassPriceRequestDto;
import com.my.hotel.service.impl.RoomClassificationServiceImpl;

import java.util.List;

public interface RoomClassificationService {
    RoomClassificationDto getById(String id);
    List<RoomClassificationDto> getAll();

    String createNewRoomClassification(RequestNewRoomClassification requestNewRoomClassification) throws Exception;

    String editRoomClassification(RequestNewRoomClassification requestNewRoomClassification) throws Exception;

    List<?> getAllPrice();

    boolean createNewPrice(CreateNewRoomClassPriceRequestDto requestDto);
}
