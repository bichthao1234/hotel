package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.ConvenienceDetailDto;
import com.my.hotel.dto.PriceRoomClassificationDto;
import com.my.hotel.dto.RequestNewRoomClassification;
import com.my.hotel.dto.RoomClassificationDto;
import com.my.hotel.dto.request.CreateNewRoomClassPriceRequestDto;
import com.my.hotel.entity.Employee;
import com.my.hotel.entity.PriceRoomClassification;
import com.my.hotel.entity.RoomClassification;
import com.my.hotel.entity.RoomKind;
import com.my.hotel.entity.RoomType;
import com.my.hotel.repo.EmployeeRepo;
import com.my.hotel.repo.PriceRoomClassificationRepo;
import com.my.hotel.repo.RoomClassificationRepo;
import com.my.hotel.service.ConvenienceDetailService;
import com.my.hotel.service.PriceRoomClassificationService;
import com.my.hotel.service.RoomClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoomClassificationServiceImpl implements RoomClassificationService {
    @Autowired
    private RoomClassificationRepo repo;
    @Autowired
    private ConvenienceDetailService convenienceDetailService;
    @Autowired
    private PriceRoomClassificationService roomClassificationService;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private PriceRoomClassificationRepo priceRoomClassificationRepo;

    @Override
    public RoomClassificationDto getById(String id) {
        RoomClassification roomClassification = this.repo.findById(id).orElse(null);
        return getRoomClassificationDto(roomClassification);
    }

    @Override
    public List<RoomClassificationDto> getAll() {
        return repo.findAll().stream().map(this::getRoomClassificationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createNewRoomClassification(RequestNewRoomClassification requestNewRoomClassification) throws Exception {
        RoomClassification roomClassification = setRoomClassification(requestNewRoomClassification);
        try {
            RoomClassification resultRoomClassification = repo.saveAndFlush(roomClassification);
            if (Objects.nonNull(resultRoomClassification)) {
                addConvenience(resultRoomClassification.getId(), requestNewRoomClassification);
                setPriceWithTodayInRoomClassification(resultRoomClassification, requestNewRoomClassification);
                return resultRoomClassification.getId();
            } else {
                throw new Exception("Can not create new room classification");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<?> getAllPrice() {
        List<PriceRoomClassificationDto> ret = new ArrayList<>();
        List<PriceRoomClassificationDto> priceRoomClassificationDtos =
                ObjectMapperUtils.mapAll(priceRoomClassificationRepo.findAll(), PriceRoomClassificationDto.class);
        Map<RoomClassificationDto, List<PriceRoomClassificationDto>> collect =
                priceRoomClassificationDtos.stream().collect(Collectors.groupingBy(PriceRoomClassificationDto::getRoomClassification));
        for (Map.Entry<RoomClassificationDto, List<PriceRoomClassificationDto>> entry : collect.entrySet()) {
            List<PriceRoomClassificationDto> value = entry.getValue();
            ret.add(value.stream().filter(dto -> !dto.getAppliedDate().after(new Date())).max(Comparator.comparing(PriceRoomClassificationDto::getAppliedDate))
                    .orElse(null));
        }
        return ret;
    }

    @Override
    @Transactional
    public boolean createNewPrice(CreateNewRoomClassPriceRequestDto requestDto) {
        try {
            PriceRoomClassification entity = new PriceRoomClassification();
            entity.setPrice(requestDto.getPrice());
            entity.setAppliedDate(requestDto.getAppliedDate());
            entity.setCreatedDate(new Date());
            RoomClassification roomClassification = this.repo.findById(requestDto.getRoomClassId()).orElse(null);
            Objects.requireNonNull(roomClassification).addPriceRoomClassifications(entity);
            Employee employee = employeeRepo.findById(requestDto.getCreatedBy()).orElse(null);
            Objects.requireNonNull(employee).addPriceRoomClassifications(entity);
//            this.priceRoomClassificationRepo.save(entity);

            this.priceRoomClassificationRepo.savePriceRoomClassification(requestDto.getRoomClassId(),
                    requestDto.getAppliedDate(), requestDto.getCreatedDate(), requestDto.getPrice(), requestDto.getCreatedBy());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public String editRoomClassification(RequestNewRoomClassification requestNewRoomClassification) throws Exception {
        try {
            boolean checkRoomTypeAndRoomKind = this.checkRoomTypeAndRoomKind(requestNewRoomClassification);
            if (checkRoomTypeAndRoomKind) {
                throw new Exception("Hạng phòng bị trùng kiểu phòng và loại phòng với hạng phòng khác");
            }
            this.repo.updateRoomClassification(
                    Integer.parseInt(requestNewRoomClassification.getId()),
                    requestNewRoomClassification.getDescription(),
                    requestNewRoomClassification.getGuestNum(),
                    requestNewRoomClassification.getRoomTypeDto().getId(),
                    requestNewRoomClassification.getRoomKindDto().getId());
            this.updateConvenience(requestNewRoomClassification);
            return requestNewRoomClassification.getId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void updateConvenience(RequestNewRoomClassification requestNewRoomClassification) throws Exception {
        try {
            List<ConvenienceDetailDto> convenienceDetailDtos = this.convenienceDetailService.getConvenienceDetails(requestNewRoomClassification.getId());
            List<ConvenienceDetailDto> updateConvenienceDetailDto = requestNewRoomClassification.getConvenienceIds();

            List<ConvenienceDetailDto> newConvenienceDetails = updateConvenienceDetailDto.stream()
                    .filter(updateDto -> convenienceDetailDtos.stream()
                            .noneMatch(existingDto -> existingDto.getConvenience().getId().equals(updateDto.getConvenience().getId()))
                    )
                    .collect(Collectors.toList());

            List<ConvenienceDetailDto> deletedConvenienceDetails = convenienceDetailDtos.stream()
                    .filter(existingDto -> updateConvenienceDetailDto.stream()
                            .noneMatch(updateDto -> updateDto.getConvenience().getId().equals(existingDto.getConvenience().getId()))
                    )
                    .collect(Collectors.toList());

            insertNewConvenienceDetails(newConvenienceDetails);
            deleteConvenienceDetails(deletedConvenienceDetails);

            convenienceDetailDtos.stream()
                    .filter(updateDto -> updateConvenienceDetailDto.stream()
                            .anyMatch(existingDto -> existingDto.getConvenience().getId().equals(updateDto.getConvenience().getId()))
                    )
                    .forEach(updateDto -> {
                        ConvenienceDetailDto existingDto = updateConvenienceDetailDto.stream()
                                .filter(dto -> dto.getConvenience().getId().equals(updateDto.getConvenience().getId()))
                                .findFirst()
                                .orElse(null);

                        if (existingDto != null) {
                            try {
                                updateConvenienceDetail(existingDto);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void updateConvenienceDetail(ConvenienceDetailDto convenienceDetailDto) throws Exception {
        try {
            this.convenienceDetailService.update(convenienceDetailDto);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void deleteConvenienceDetails(List<ConvenienceDetailDto> deletedConvenienceDetails) {
        try {
            for (ConvenienceDetailDto convenienceDetailDto : deletedConvenienceDetails) {
                this.convenienceDetailService.remove(convenienceDetailDto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void insertNewConvenienceDetails(List<ConvenienceDetailDto> newConvenienceDetails) {
        try {
            for (ConvenienceDetailDto convenienceDetailDto : newConvenienceDetails) {
                this.convenienceDetailService.saveAndFlush(convenienceDetailDto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkRoomTypeAndRoomKind(RequestNewRoomClassification requestNewRoomClassification) {
        List<RoomClassification> roomClassifications = this.repo.findAll();
        roomClassifications.removeIf(roomClassification -> roomClassification.getId().equals(requestNewRoomClassification.getId()));
        return roomClassifications.stream()
                .anyMatch(roomClassification ->
                        (roomClassification.getRoomKind().getId().equals(requestNewRoomClassification.getRoomKindDto().getId()) &&
                                roomClassification.getRoomType().getId().equals(requestNewRoomClassification.getRoomTypeDto().getId()))
                );
    }

    private void addConvenience(String roomClassificationId, RequestNewRoomClassification requestNewRoomClassification) throws Exception {
        try {
            for (ConvenienceDetailDto convenienceDetail : requestNewRoomClassification.getConvenienceIds()) {
                RoomClassificationDto roomClassificationDto = new RoomClassificationDto();
                roomClassificationDto.setId(roomClassificationId);
                convenienceDetail.setRoomClassification(roomClassificationDto);
                convenienceDetailService.saveAndFlush(convenienceDetail);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setPriceWithTodayInRoomClassification(RoomClassification roomClassification, RequestNewRoomClassification requestNewRoomClassification) throws Exception {
        try {
            Employee employee = employeeRepo.findById(requestNewRoomClassification.getEmployeeDto().getId()).orElse(null);
            PriceRoomClassification priceRoomClassification = new PriceRoomClassification();
            priceRoomClassification.setRoomClassification(roomClassification);
            priceRoomClassification.setCreatedBy(employee);
            priceRoomClassification.setAppliedDate(new Date());
            priceRoomClassification.setCreatedDate(new Date());
            priceRoomClassification.setPrice(requestNewRoomClassification.getPriceRoomClassifications());
            roomClassificationService.save(priceRoomClassification);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private RoomClassification setRoomClassification(RequestNewRoomClassification requestNewRoomClassification) {
        RoomClassification roomClassification = new RoomClassification();
        roomClassification.setGuestNum(requestNewRoomClassification.getGuestNum());
        roomClassification.setDescription(requestNewRoomClassification.getDescription());
        roomClassification.setRoomKind(ObjectMapperUtils.map(requestNewRoomClassification.getRoomKindDto(), RoomKind.class));
        roomClassification.setRoomType(ObjectMapperUtils.map(requestNewRoomClassification.getRoomTypeDto(), RoomType.class));
        return roomClassification;
    }

    private RoomClassificationDto getRoomClassificationDto(RoomClassification roomClassification) {
        if (Objects.isNull(roomClassification)) {
            return new RoomClassificationDto();
        }
        RoomClassificationDto roomClassificationDto = ObjectMapperUtils.map(roomClassification, RoomClassificationDto.class);
        List<ConvenienceDetailDto> convenienceDetailDtos = roomClassification.getConvenienceDetails()
                .stream().map((element) -> {
                    ConvenienceDetailDto convenienceDetailDto = ObjectMapperUtils.map(element, ConvenienceDetailDto.class);
                    convenienceDetailDto.setRoomClassification(null);
                    return convenienceDetailDto;
                }).collect(Collectors.toList());
        roomClassificationDto.setConvenienceDetailDtos(convenienceDetailDtos);
        roomClassificationDto.setPriceRoomClassificationDtos(ObjectMapperUtils.mapAll(roomClassification.getPriceRoomClassifications()
                , PriceRoomClassificationDto.class));
        roomClassificationDto.setPrice(Objects.requireNonNull(roomClassificationDto.getPriceRoomClassificationDtos()
                .stream().filter(dto -> !dto.getAppliedDate().after(new Date())).max(Comparator.comparing(PriceRoomClassificationDto::getAppliedDate))
                .orElse(null)).getPrice());
        return roomClassificationDto;
    }
}
