package com.my.hotel.service;

import com.my.hotel.dto.PromotionDetailDto;
import com.my.hotel.dto.PromotionDto;
import com.my.hotel.dto.RequestNewPromotionDto;

import java.util.List;

public interface PromotionService {
    List<PromotionDto> getAllPromotions();

    PromotionDto getById(String id);

    String createNewPromotion(RequestNewPromotionDto requestDto);

    boolean save(PromotionDto promotionDto);

    boolean addPromotionDetail(PromotionDetailDto requestDto);

    boolean delete(String id);
}
