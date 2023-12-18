package com.my.hotel.service.impl;

import com.my.hotel.entity.PriceRoomClassification;
import com.my.hotel.repo.PriceRoomClassificationRepo;
import com.my.hotel.service.PriceRoomClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceRoomClassificationServiceImpl implements PriceRoomClassificationService {
    @Autowired
    private PriceRoomClassificationRepo repo;

    @Override
    @Transactional
    public void save(PriceRoomClassification priceRoomClassification) throws Exception {
        try {
            repo.saveWithQuery(Integer.parseInt(priceRoomClassification.getRoomClassification().getId()),
                    priceRoomClassification.getAppliedDate(),
                    priceRoomClassification.getCreatedDate(),
                    priceRoomClassification.getPrice(),
                    priceRoomClassification.getCreatedBy().getId());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
