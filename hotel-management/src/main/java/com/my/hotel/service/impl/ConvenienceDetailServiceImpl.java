package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.ConvenienceDetailDto;
import com.my.hotel.entity.Convenience;
import com.my.hotel.entity.ConvenienceDetail;
import com.my.hotel.entity.RoomClassification;
import com.my.hotel.repo.ConvenienceDetailRepo;
import com.my.hotel.repo.ConvenienceRepo;
import com.my.hotel.repo.RoomClassificationRepo;
import com.my.hotel.service.ConvenienceDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConvenienceDetailServiceImpl implements ConvenienceDetailService {
    @Autowired
    private ConvenienceDetailRepo repo;
    @Autowired
    private ConvenienceRepo convenienceRepo;
    @Autowired
    private RoomClassificationRepo roomClassificationRepo;

    @Override
    @Transactional
    public void saveAndFlush(ConvenienceDetailDto convenienceDetailDto) throws Exception {
        try {
            Convenience convenience = convenienceRepo.findById(convenienceDetailDto.getConvenience().getId()).orElse(null);
            RoomClassification roomClassification = roomClassificationRepo.findById(convenienceDetailDto.getRoomClassification().getId()).orElse(null);
            ConvenienceDetail convenienceDetail = new ConvenienceDetail();
            convenienceDetail.setConvenience(convenience);
            convenienceDetail.setRoomClassification(roomClassification);
            convenienceDetail.setDescription(convenienceDetailDto.getDescription());
            convenienceDetail.setQuantity(convenienceDetailDto.getQuantity());
            repo.saveWithQuery(convenienceDetail.getConvenience().getId(),
                    Integer.parseInt(convenienceDetail.getRoomClassification().getId()),
                    convenienceDetail.getQuantity(),
                    convenienceDetail.getDescription());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<ConvenienceDetailDto> getConvenienceDetails(String id) {
        List<ConvenienceDetailDto> convenienceDetailDtos = this.repo.findAll().stream()
                .map(convenienceDetail -> ObjectMapperUtils.map(convenienceDetail, ConvenienceDetailDto.class))
                .filter(convenienceDetailDto ->
                        convenienceDetailDto.getRoomClassification() != null && convenienceDetailDto.getRoomClassification().getId().equals(id)
                ).collect(Collectors.toList());
        return convenienceDetailDtos;
    }

    @Override
    public void update(ConvenienceDetailDto convenienceDetailDto) {
        this.repo.update(convenienceDetailDto.getConvenience().getId(),
                Integer.parseInt(convenienceDetailDto.getRoomClassification().getId()),
                convenienceDetailDto.getDescription(),
                convenienceDetailDto.getQuantity());
    }

    @Override
    @Transactional
    public void remove(ConvenienceDetailDto convenienceDetailDto) {
        try {
            this.repo.deleteByIds(convenienceDetailDto.getConvenience().getId(),
                    Integer.parseInt(convenienceDetailDto.getRoomClassification().getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
