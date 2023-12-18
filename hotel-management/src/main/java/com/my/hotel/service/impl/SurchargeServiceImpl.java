package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.PriceSurchargeDto;
import com.my.hotel.dto.RequestNewSurchargeDto;
import com.my.hotel.dto.SurchargeDetailDto;
import com.my.hotel.dto.SurchargeDto;
import com.my.hotel.dto.request.CreateNewSurchargePriceRequestDto;
import com.my.hotel.entity.PriceSurcharge;
import com.my.hotel.entity.Surcharge;
import com.my.hotel.repo.PriceSurchargeRepo;
import com.my.hotel.repo.SurchargeDetailRepo;
import com.my.hotel.repo.SurchargeRepo;
import com.my.hotel.service.SurchargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SurchargeServiceImpl implements SurchargeService {
    @Autowired
    private SurchargeRepo surchargeRepo;
    @Autowired
    private SurchargeDetailRepo surchargeDetailRepo;

    @Autowired
    private PriceSurchargeRepo priceSurchargeRepo;

    @Override
    public List<SurchargeDto> getAll() {
        return surchargeRepo.findAll().stream().map(surcharge -> {
            SurchargeDto surchargeDto = ObjectMapperUtils.map(surcharge, SurchargeDto.class);
            surchargeDto.setPriceSurcharge(surcharge.getPriceSurcharges().stream().filter(
                    dto -> !dto.getAppliedDate().after(new Date())
            ).max(Comparator.comparing(PriceSurcharge::getAppliedDate)).orElse(null).getPrice());
            surchargeDto.setPriceSurchargeDtos(ObjectMapperUtils.mapAll(surcharge.getPriceSurcharges(), PriceSurchargeDto.class));
            surchargeDto.setCanDelete(surcharge.canDelete());
            return surchargeDto;
        }).collect(Collectors.toList());
    }

    @Override
    public SurchargeDto getById(String id) {
        Surcharge surcharge = this.surchargeRepo.findById(id).orElse(null);
        SurchargeDto surchargeDto = ObjectMapperUtils.map(surcharge, SurchargeDto.class);
        surchargeDto.setPriceSurcharge(Objects.requireNonNull(Objects.requireNonNull(surcharge).getPriceSurcharges().stream()
                        .filter(dto -> !dto.getAppliedDate().after(new Date())).max(Comparator.comparing(PriceSurcharge::getAppliedDate)).orElse(null))
                .getPrice());
        surchargeDto.setPriceSurchargeDtos(ObjectMapperUtils.mapAll(surcharge.getPriceSurcharges(), PriceSurchargeDto.class));
        return surchargeDto;
    }

    @Override
    @Transactional
    public boolean saveWithRentalSlip(SurchargeDetailDto surchargeDetailDto) throws Exception {
        try {
            Integer surchargeDetailExist = this.surchargeDetailRepo.checkId(surchargeDetailDto.getRentalSlipDetail().getId(),
                    surchargeDetailDto.getSurcharge().getId());
            if (surchargeDetailExist.equals(1)) {
                this.surchargeDetailRepo.updateSurchargeDetail(surchargeDetailDto.getRentalSlipDetail().getId(),
                        surchargeDetailDto.getSurcharge().getId(), surchargeDetailDto.getPrice(),
                        surchargeDetailDto.getQuantity(), surchargeDetailDto.getStatus());
            } else {
                this.surchargeDetailRepo.saveSurchargeDetail(surchargeDetailDto.getRentalSlipDetail().getId(),
                        surchargeDetailDto.getSurcharge().getId(), surchargeDetailDto.getPrice(),
                        surchargeDetailDto.getQuantity(), surchargeDetailDto.getStatus());

            }
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean createNewPrice(CreateNewSurchargePriceRequestDto requestDto) {
        try {
            this.priceSurchargeRepo.savePriceSurcharge(requestDto.getSurchargeId(), requestDto.getAppliedDate(),
                    requestDto.getPrice(), requestDto.getCreatedBy());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public String createNewSurcharge(RequestNewSurchargeDto requestDto) {
        try {
            Surcharge surcharge = new Surcharge();
            surcharge.setName(requestDto.getName());
            surcharge.setDescription(requestDto.getDescription());
            Surcharge resultSurcharge = this.surchargeRepo.saveAndFlush(surcharge);
            this.priceSurchargeRepo.savePriceSurcharge(resultSurcharge.getId(), new Date(),
                    requestDto.getPriceSurcharge(), requestDto.getCreatedBy());
            return resultSurcharge.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean save(SurchargeDto surchargeDto) {
        try {
            Surcharge surcharge = ObjectMapperUtils.map(surchargeDto, Surcharge.class);
            this.surchargeRepo.save(surcharge);
            return true;
        } catch (DataIntegrityViolationException dE) {
            throw new RuntimeException("Name of service: \"" +surchargeDto.getName() + "\" is exits in the system!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try {
            Surcharge surcharge = this.surchargeRepo.findById(id).orElseThrow();
            if (surcharge.canDelete()) {
                this.priceSurchargeRepo.deletePriceSurcharge(id);
                this.surchargeRepo.deleteSurcharge(Objects.requireNonNull(surcharge).getId());
                return true;
            } else {
                throw new DataIntegrityViolationException("Không thể xóa phụ thu này");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
