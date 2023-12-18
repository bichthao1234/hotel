package com.my.hotel.service;

import com.my.hotel.dto.ConvenienceDetailDto;
import com.my.hotel.entity.ConvenienceDetail;

import java.util.List;

public interface ConvenienceDetailService {
    void saveAndFlush(ConvenienceDetailDto convenienceDetail) throws Exception;

    List<ConvenienceDetailDto> getConvenienceDetails(String id);

    void update(ConvenienceDetailDto convenienceDetailDto);

    void remove(ConvenienceDetailDto convenienceDetailDto);
}
