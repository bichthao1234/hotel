package com.my.hotel.service;

import com.my.hotel.dto.RoomKindDto;

import java.util.List;

public interface RoomKindService {
    List<RoomKindDto> getAllRoomType();

    List<RoomKindDto> findByName(String name);

    boolean addNew(RoomKindDto roomKindDto);

    boolean save(RoomKindDto roomKindDto);
}
