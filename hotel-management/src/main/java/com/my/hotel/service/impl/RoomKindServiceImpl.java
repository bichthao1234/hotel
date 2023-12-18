package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.RoomKindDto;
import com.my.hotel.entity.RoomKind;
import com.my.hotel.entity.RoomType;
import com.my.hotel.repo.RoomKindRepo;
import com.my.hotel.service.RoomKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomKindServiceImpl implements RoomKindService {
    @Autowired
    private RoomKindRepo repo;

    @Override
    public List<RoomKindDto> getAllRoomType() {
        return this.repo.findAll().stream().sorted(Comparator.comparing(RoomKind::getId).reversed()).map(roomKind -> {
            RoomKindDto roomKindDto = ObjectMapperUtils.map(roomKind, RoomKindDto.class);
            return roomKindDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<RoomKindDto> findByName(String name) {
        return this.repo.findByName(name).stream().map(roomKind -> ObjectMapperUtils.map(roomKind, RoomKindDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addNew(RoomKindDto roomKindDto) {
        try {
            RoomKind roomKind = ObjectMapperUtils.map(roomKindDto, RoomKind.class);
            this.repo.save(roomKind);
            return true;
        } catch (DataIntegrityViolationException dE) {
            throw new RuntimeException("Name of room kind: \"" +roomKindDto.getName() + "\" is exits in the system!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean save(RoomKindDto roomKindDto) {
        try {
            RoomKind roomType = ObjectMapperUtils.map(roomKindDto, RoomKind.class);
            this.repo.save(roomType);
            return true;
        } catch (DataIntegrityViolationException dE) {
            throw new RuntimeException("Name of room type: \"" +roomKindDto.getName() + "\" is exits in the system!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
