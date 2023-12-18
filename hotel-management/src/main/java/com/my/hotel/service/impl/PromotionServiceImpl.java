package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.PromotionDetailDto;
import com.my.hotel.dto.PromotionDto;
import com.my.hotel.dto.RequestNewPromotionDto;
import com.my.hotel.entity.Promotion;
import com.my.hotel.entity.PromotionDetail;
import com.my.hotel.repo.PromotionDetailRepo;
import com.my.hotel.repo.PromotionRepo;
import com.my.hotel.service.PromotionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepo promotionRepo;

    @Autowired
    private PromotionDetailRepo promotionDetailRepo;

    @Override
    public List<PromotionDto> getAllPromotions() {
        List<Promotion> promotions = this.promotionRepo.findAll();
        return promotions.stream().map(promotion -> {
            PromotionDto promotionDto = ObjectMapperUtils.map(promotion, PromotionDto.class);
            promotionDto.setCanDelete(promotion.canDelete());
            return promotionDto;
        }).collect(Collectors.toList());
    }

    @Override
    public PromotionDto getById(String id) {
        Promotion promotion = this.promotionRepo.findById(id).orElseThrow();
        PromotionDto promotionDto = ObjectMapperUtils.map(promotion, PromotionDto.class);
        promotionDto.setPromotionDetails(new ArrayList<>());
        for (PromotionDetail promotionDetail : promotion.getPromotionDetails()) {
            PromotionDetailDto promotionDetailDto = ObjectMapperUtils.map(promotionDetail, PromotionDetailDto.class);
            promotionDetailDto.setPromotionId(promotionDetail.getPromotion().getId());
            promotionDto.getPromotionDetails().add(promotionDetailDto);
        }
        return promotionDto;
    }

    @Override
    @Transactional
    public String createNewPromotion(RequestNewPromotionDto requestDto) {
        try {
            Promotion promotion = new Promotion();
            promotion.setDescription(requestDto.getDescription());
            promotion.setStartDate(requestDto.getStartDate());
            promotion.setEndDate(requestDto.getEndDate());
            Promotion resultPromotion = this.promotionRepo.saveAndFlush(promotion);
            return resultPromotion.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean save(PromotionDto promotionDto) {
        try {
            Promotion promotion = ObjectMapperUtils.map(promotionDto, Promotion.class);
            this.promotionRepo.save(promotion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean addPromotionDetail(PromotionDetailDto requestDto) {
        try {
            this.promotionDetailRepo.savePromotionDetail(requestDto.getPromotionId(), requestDto.getRoomClassId(), requestDto.getPercent());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try {
            Promotion promotion = this.promotionRepo.findById(id).orElseThrow();
            if (promotion.canDelete()) {
                this.promotionRepo.deletePromotion(id);
                return true;
            } else {
                throw new DataIntegrityViolationException("Không thể xóa Khuyến mãi này");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
