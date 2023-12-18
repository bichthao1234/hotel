package com.my.hotel.service;

import com.my.hotel.dto.ChangeRoomStatusDto;
import com.my.hotel.dto.NewRoomReqDto;
import com.my.hotel.dto.request.GetRoomsRequestDto;
import com.my.hotel.dto.RoomDto;

import java.util.List;
import java.util.Map;

public interface RoomService {

    List<RoomDto> getAllRooms(GetRoomsRequestDto request) throws Throwable;

    List<RoomDto> getAllAvailableRooms(String roomClassId) throws Throwable;

    List<Map<String, Object>> getRentalSlipDetailByRoom(String roomId) throws Throwable;

    List<Map<String, Object>> getGuestManifestByRoom(String roomId) throws Throwable;

    List<Map<String, Object>> getServiceByRoom(String roomId) throws Throwable;

    List<Map<String, Object>> getSurchargeByRoom(String roomId) throws Throwable;

    boolean changeRoomStatus(ChangeRoomStatusDto requestDto) throws Exception;

    boolean createRoom(NewRoomReqDto requestDto);

    boolean modifyRoom(NewRoomReqDto requestDto);
}
