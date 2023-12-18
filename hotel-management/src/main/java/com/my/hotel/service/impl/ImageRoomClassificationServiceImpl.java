package com.my.hotel.service.impl;

import com.my.hotel.dto.ImageRoomClassificationDto;
import com.my.hotel.entity.ImageRoomClassification;
import com.my.hotel.entity.RoomClassification;
import com.my.hotel.repo.ImageRoomClassificationRepo;
import com.my.hotel.repo.RoomClassificationRepo;
import com.my.hotel.service.ImageRoomClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ImageRoomClassificationServiceImpl implements ImageRoomClassificationService {
    @Autowired
    private ImageRoomClassificationRepo repo;
    @Autowired
    private RoomClassificationRepo roomClassificationRepo;

    @Override
    public void save(ImageRoomClassificationDto imageRoomClassificationDto) throws Exception {
        try {
            RoomClassification roomClassification = roomClassificationRepo.findById(imageRoomClassificationDto.getRoomClassificationDto().getId()).orElse(null);
            ImageRoomClassification imageRoomClassification = this.repo.findByRoomClassificationId(roomClassification.getId());
            if (Objects.isNull(imageRoomClassification)) {
                imageRoomClassification = new ImageRoomClassification(imageRoomClassificationDto.getUrl(), roomClassification);
            } else {
                imageRoomClassification.setUrl(imageRoomClassificationDto.getUrl());
            }
            repo.saveAndFlush(imageRoomClassification);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
