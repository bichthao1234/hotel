package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.ConvenienceDto;
import com.my.hotel.entity.Convenience;
import com.my.hotel.repo.ConvenienceRepo;
import com.my.hotel.service.ConvenienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConvenienceServiceImpl implements ConvenienceService {
    @Autowired
    private ConvenienceRepo repo;

    @Override
    public List<ConvenienceDto> getAllConvenience() {
        return this.repo.findAll().stream().sorted(Comparator.comparing(Convenience::getId).reversed()).map(Convenience -> {
            ConvenienceDto ConvenienceDto = ObjectMapperUtils.map(Convenience, ConvenienceDto.class);
            return ConvenienceDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ConvenienceDto> findByName(String name) {
        return this.repo.findByName(name).stream().map(Convenience -> {
            return ObjectMapperUtils.map(Convenience, ConvenienceDto.class);
        }).collect(Collectors.toList());
    }

    @Override
    public boolean save(ConvenienceDto ConvenienceDto) {
        try {
            Convenience Convenience = ObjectMapperUtils.map(ConvenienceDto, Convenience.class);
            this.repo.save(Convenience);
            return true;
        } catch (DataIntegrityViolationException dE) {
            throw new RuntimeException("Name of room type: \"" +ConvenienceDto.getName() + "\" is exits in the system!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
