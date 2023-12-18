package com.my.hotel.service;

import com.my.hotel.dto.RoomStatusDto;
import com.my.hotel.dto.RoomTypeDto;

import javax.transaction.Transactional;
import java.util.List;

public interface RoomStatusService {

    List<RoomStatusDto> getAllRoomStatus();

}
