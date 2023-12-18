package com.my.hotel.service;

import com.my.hotel.dto.RequestNewServiceDto;
import com.my.hotel.dto.ServiceDetailDto;
import com.my.hotel.dto.ServiceDto;
import com.my.hotel.dto.request.AlterRentalDetailRequestDto;
import com.my.hotel.dto.request.CreateNewServicePriceRequestDto;

import javax.transaction.Transactional;
import java.util.List;

public interface ServiceService {
    List<ServiceDto> getAllServices();

    ServiceDto getById(String id);

    @org.springframework.transaction.annotation.Transactional
    boolean createNewPrice(CreateNewServicePriceRequestDto requestDto);

    @Transactional
    boolean payForService(Integer rentalSlipDetailId, String serviceId);

    @Transactional
    boolean addServiceToRentalSlip(AlterRentalDetailRequestDto requestDto);

    boolean saveWithRentalSlip(ServiceDetailDto serviceDetailDto) throws Exception;

    String createNewService(RequestNewServiceDto requestDto);

    boolean save(ServiceDto serviceDto);

    boolean delete(String id);
}
