package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.RoomStatusDto;
import com.my.hotel.entity.RoomStatus;
import com.my.hotel.repo.RoomStatusRepo;
import com.my.hotel.service.RoomStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomStatusServiceImpl implements RoomStatusService {
    @Autowired
    private RoomStatusRepo repo;

    @Override
    public List<RoomStatusDto> getAllRoomStatus() {
        return this.repo.findAll().stream().sorted(Comparator.comparing(RoomStatus::getId).reversed()).map(roomType ->
                ObjectMapperUtils.map(roomType, RoomStatusDto.class)).collect(Collectors.toList());
    }
}
