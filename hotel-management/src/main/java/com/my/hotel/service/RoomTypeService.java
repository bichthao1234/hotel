package com.my.hotel.service;

import com.my.hotel.dto.RoomTypeDto;

import javax.transaction.Transactional;
import java.util.List;

public interface RoomTypeService {

    List<RoomTypeDto> getAllRoomType();

    List<RoomTypeDto> findByName(String name);

    @Transactional
    boolean save(RoomTypeDto roomTypeDto);

}
