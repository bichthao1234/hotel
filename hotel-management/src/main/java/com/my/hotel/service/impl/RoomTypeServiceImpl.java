package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.RoomTypeDto;
import com.my.hotel.entity.RoomType;
import com.my.hotel.repo.RoomTypeRepo;
import com.my.hotel.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {
    @Autowired
    private RoomTypeRepo repo;

    @Override
    public List<RoomTypeDto> getAllRoomType() {
        return this.repo.findAll().stream().sorted(Comparator.comparing(RoomType::getId).reversed()).map(roomType -> {
            RoomTypeDto roomTypeDto = ObjectMapperUtils.map(roomType, RoomTypeDto.class);
            return roomTypeDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoomTypeDto> findByName(String name) {
        return this.repo.findByName(name).stream().map(roomType -> {
            return ObjectMapperUtils.map(roomType, RoomTypeDto.class);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean save(RoomTypeDto roomTypeDto) {
        try {
            RoomType roomType = ObjectMapperUtils.map(roomTypeDto, RoomType.class);
            this.repo.save(roomType);
            return true;
        } catch (DataIntegrityViolationException dE) {
            throw new RuntimeException("Name of room type: \"" +roomTypeDto.getName() + "\" is exits in the system!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
