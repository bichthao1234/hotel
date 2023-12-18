package com.my.hotel.service;

import com.my.hotel.dto.ConvenienceDto;

import javax.transaction.Transactional;
import java.util.List;

public interface ConvenienceService {
    List<ConvenienceDto> getAllConvenience();

    List<ConvenienceDto> findByName(String name);

    @Transactional
    boolean save(ConvenienceDto ConvenienceDto);
}
