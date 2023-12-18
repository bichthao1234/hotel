package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.PriceServiceDto;
import com.my.hotel.dto.RequestNewServiceDto;
import com.my.hotel.dto.ServiceDetailDto;
import com.my.hotel.dto.ServiceDto;
import com.my.hotel.dto.SurchargeDto;
import com.my.hotel.dto.request.AlterRentalDetailRequestDto;
import com.my.hotel.dto.request.CreateNewServicePriceRequestDto;
import com.my.hotel.entity.PriceService;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.ServiceDetail;
import com.my.hotel.entity.Surcharge;
import com.my.hotel.entity.SurchargeDetail;
import com.my.hotel.repo.PriceServiceRepo;
import com.my.hotel.repo.RentalSlipDetailRepo;
import com.my.hotel.repo.ServiceDetailRepo;
import com.my.hotel.repo.ServicesRepo;
import com.my.hotel.repo.SurchargeDetailRepo;
import com.my.hotel.repo.SurchargeRepo;
import com.my.hotel.service.ServiceService;
import com.my.hotel.utils.Utilities;
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
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServicesRepo servicesRepo;

    @Autowired
    private ServiceDetailRepo serviceDetailRepo;

    @Autowired
    private SurchargeRepo surchargeRepo;

    @Autowired
    private SurchargeDetailRepo surchargeDetailRepo;

    @Autowired
    private RentalSlipDetailRepo rentalSlipDetailRepo;

    @Autowired
    private PriceServiceRepo priceServiceRepo;

    @Override
    public List<ServiceDto> getAllServices() {
        List<com.my.hotel.entity.Service> services = this.servicesRepo.findAll();
        return services.stream().map(service -> {
            ServiceDto serviceDto = ObjectMapperUtils.map(service, ServiceDto.class);
            serviceDto.setPriceService(service.getPriceServices().stream()
                    .filter(dto -> !dto.getAppliedDate().after(new Date())).max(Comparator.comparing(PriceService::getAppliedDate)).orElse(null).getPrice());
            serviceDto.setPriceServiceDtos(ObjectMapperUtils.mapAll(service.getPriceServices(), PriceServiceDto.class));
            serviceDto.setCanDelete(service.canDelete());
            return serviceDto;
        }).collect(Collectors.toList());
    }

    @Override
    public ServiceDto getById(String id) {
        com.my.hotel.entity.Service service = this.servicesRepo.findById(id).orElse(null);
        ServiceDto serviceDto = ObjectMapperUtils.map(service, ServiceDto.class);
        serviceDto.setPriceService(Objects.requireNonNull(Objects.requireNonNull(service).getPriceServices().stream()
                        .filter(dto -> !dto.getAppliedDate().after(new Date())).max(Comparator.comparing(PriceService::getAppliedDate)).orElse(null))
                .getPrice());
        serviceDto.setPriceServiceDtos(ObjectMapperUtils.mapAll(service.getPriceServices(), PriceServiceDto.class));
        return serviceDto;
    }

    @Override
    @Transactional
    public boolean createNewPrice(CreateNewServicePriceRequestDto requestDto) {
        try {
            this.priceServiceRepo.savePriceService(requestDto.getServiceId(), requestDto.getAppliedDate(),
                    requestDto.getPrice(), requestDto.getCreatedBy());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean payForService(Integer rentalSlipDetailId, String serviceId) {
        try {
            ServiceDetail serviceDetail = servicesRepo.getServiceDetailByKey(rentalSlipDetailId, serviceId);
            if (serviceDetail.getStatus().equals(0)) {
                serviceDetail.setStatus(1);
                serviceDetailRepo.updateStatusServiceDetail(rentalSlipDetailId, serviceId);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean addServiceToRentalSlip(AlterRentalDetailRequestDto requestDto) {
        try {
            RentalSlipDetail rentalSlipDetail = rentalSlipDetailRepo.getRentalSlipDetailWithId(requestDto.getRentalSlipDetailId());
            // Service
            List<String> listServiceId = rentalSlipDetail.getServiceDetails().stream().map(ServiceDetail::getService)
                    .map(com.my.hotel.entity.Service::getId).collect(Collectors.toList());
            rentalSlipDetail.getServiceDetails().clear();
            for (ServiceDto serviceDto : requestDto.getServiceList()) {
                if (listServiceId.contains(serviceDto.getId())) {
                    ServiceDetail serviceDetailByKey = servicesRepo.getServiceDetailByKey(requestDto.getRentalSlipDetailId(), serviceDto.getId());
                    serviceDetailByKey.setQuantity(serviceDto.getQuantity());
                    serviceDetailByKey.setPrice(Utilities.parseFloat(serviceDto.getPriceService()));
                    serviceDetailByKey.setNote(serviceDto.getNote());
                    serviceDetailByKey.setStatus(serviceDto.getStatus() ? 1 : 0);
                } else {
                    ServiceDetail serviceDetail = new ServiceDetail();
                    serviceDetail.setQuantity(serviceDto.getQuantity());
                    serviceDetail.setPrice(Utilities.parseFloat(serviceDto.getPriceService()));
                    serviceDetail.setStatus(serviceDto.getStatus() ? 1 : 0);
                    serviceDetail.setUsingDate(new Date());
                    com.my.hotel.entity.Service service = servicesRepo.findById(serviceDto.getId()).orElseThrow();
                    service.addServiceDetails(serviceDetail);
                    rentalSlipDetail.addServiceDetails(serviceDetail);
                    serviceDetailRepo.saveServiceDetail(requestDto.getRentalSlipDetailId(),
                            serviceDto.getId(), new Date(), serviceDto.getNote(),
                            Utilities.parseFloat(serviceDto.getPriceService()), serviceDto.getQuantity(), serviceDto.getStatus() ? 1 : 0);
                }
            }
            serviceDetailRepo.deleteServiceDetail(requestDto.getRentalSlipDetailId(),
                    requestDto.getServiceList().stream().map(ServiceDto::getId).collect(Collectors.toList()));
            // Surcharge
            List<String> listSurcharge = rentalSlipDetail.getSurchargeDetails().stream().map(SurchargeDetail::getSurcharge)
                    .map(com.my.hotel.entity.Surcharge::getId).collect(Collectors.toList());
            rentalSlipDetail.getSurchargeDetails().clear();
            for (SurchargeDto surchargeDto : requestDto.getSurchargeList()) {
                if (listSurcharge.contains(surchargeDto.getId())) {
                    SurchargeDetail surchargeDetailByKey = surchargeDetailRepo.getSurchargeDetailByKey(requestDto.getRentalSlipDetailId(), surchargeDto.getId());
                    surchargeDetailByKey.setQuantity(surchargeDto.getQuantity());
                    surchargeDetailByKey.setPrice(Utilities.parseFloat(surchargeDto.getPriceSurcharge()));
                    surchargeDetailByKey.setStatus(surchargeDto.getStatus() ? 1 : 0);
                } else {
                    SurchargeDetail surchargeDetail = new SurchargeDetail();
                    surchargeDetail.setQuantity(surchargeDto.getQuantity());
                    surchargeDetail.setPrice(Utilities.parseFloat(surchargeDto.getPriceSurcharge()));
                    surchargeDetail.setStatus(surchargeDto.getStatus() ? 1 : 0);
                    Surcharge surcharge = surchargeRepo.findById(surchargeDto.getId()).orElseThrow();
                    surcharge.addSurchargeDetails(surchargeDetail);
                    rentalSlipDetail.addSurchargeDetails(surchargeDetail);
                    surchargeDetailRepo.saveSurchargeDetail(requestDto.getRentalSlipDetailId(), surchargeDto.getId(),
                            Utilities.parseFloat(surchargeDto.getPriceSurcharge()), surchargeDto.getQuantity(), surchargeDto.getStatus() ? 1 : 0);
                }
            }
            surchargeDetailRepo.deleteSurchargeDetail(requestDto.getRentalSlipDetailId(),
                    requestDto.getServiceList().stream().map(ServiceDto::getId).collect(Collectors.toList()));

            rentalSlipDetailRepo.save(rentalSlipDetail);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean saveWithRentalSlip(ServiceDetailDto serviceDetailDto) throws Exception {
        try {
            Integer serviceDetailExist = serviceDetailRepo.checkId(serviceDetailDto.getRentalSlipDetail().getId(),
                    serviceDetailDto.getService().getId());
            if (serviceDetailExist.equals(1)) {
                this.serviceDetailRepo.updateServiceDetail(serviceDetailDto.getRentalSlipDetail().getId(),
                        serviceDetailDto.getService().getId(), serviceDetailDto.getUsingDate(),
                        serviceDetailDto.getNote(), serviceDetailDto.getPrice(), serviceDetailDto.getQuantity(),
                        serviceDetailDto.getStatus());
            } else {
                this.serviceDetailRepo.saveServiceDetail(serviceDetailDto.getRentalSlipDetail().getId(),
                        serviceDetailDto.getService().getId(), serviceDetailDto.getUsingDate(),
                        serviceDetailDto.getNote(), serviceDetailDto.getPrice(), serviceDetailDto.getQuantity(),
                        serviceDetailDto.getStatus());
            }
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public String createNewService(RequestNewServiceDto requestDto) {
        try {
            com.my.hotel.entity.Service service = new com.my.hotel.entity.Service();
            service.setName(requestDto.getName());
            service.setUnit(requestDto.getUnit());
            service.setDescription(requestDto.getDescription());
            com.my.hotel.entity.Service resultRoomClassification = this.servicesRepo.saveAndFlush(service);
            this.priceServiceRepo.savePriceService(resultRoomClassification.getId(), new Date(),
                    requestDto.getPriceService(), requestDto.getCreatedBy());
            return resultRoomClassification.getId();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean save(ServiceDto serviceDto) {
        try {
            com.my.hotel.entity.Service service = ObjectMapperUtils.map(serviceDto, com.my.hotel.entity.Service.class);
            this.servicesRepo.save(service);
            return true;
        } catch (DataIntegrityViolationException dE) {
            throw new RuntimeException("Name of service: \"" +serviceDto.getName() + "\" is exits in the system!");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        try {
            com.my.hotel.entity.Service service = this.servicesRepo.findById(id).orElseThrow();
            if (service.canDelete()) {
                this.priceServiceRepo.deletePriceService(id);
                this.servicesRepo.deleteService(Objects.requireNonNull(service).getId());
                return true;
            } else {
                throw new DataIntegrityViolationException("Không thể xóa dịch vụ này");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
