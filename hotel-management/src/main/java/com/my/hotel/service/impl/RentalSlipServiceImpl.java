package com.my.hotel.service.impl;

import com.my.hotel.common.ObjectMapperUtils;
import com.my.hotel.dto.RentalSlipDetailDto;
import com.my.hotel.dto.RentalSlipDetailSaveDto;
import com.my.hotel.dto.RoomDto;
import com.my.hotel.dto.RoomStatusDto;
import com.my.hotel.dto.ServiceDetailDto;
import com.my.hotel.dto.SurchargeDetailDto;
import com.my.hotel.dto.request.CheckOutByRoomRequestDto;
import com.my.hotel.dto.request.ConvertReservationRequestDto;
import com.my.hotel.dto.request.GetListRentalDetailRequestDto;
import com.my.hotel.dto.request.GetRevenueRequestDto;
import com.my.hotel.dto.request.QuickCheckInRequestDto;
import com.my.hotel.dto.response.RentalSlipDetailResponseDto;
import com.my.hotel.entity.RentalSlip;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.ReservationDetail;
import com.my.hotel.entity.ServiceDetail;
import com.my.hotel.entity.SurchargeDetail;
import com.my.hotel.repo.RentalSlipDetailRepo;
import com.my.hotel.repo.RentalSlipRepo;
import com.my.hotel.repo.ServiceDetailRepo;
import com.my.hotel.repo.SurchargeDetailRepo;
import com.my.hotel.service.RentalSlipService;
import com.my.hotel.service.ServiceService;
import com.my.hotel.service.SurchargeService;
import com.my.hotel.utils.Utilities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalSlipServiceImpl implements RentalSlipService {

    final private RentalSlipRepo rentalSlipRepo;
    final private RentalSlipDetailRepo rentalSlipDetailRepo;
    final private ServiceService serviceService;
    final private SurchargeService surchargeService;
    final private ServiceDetailRepo serviceDetailRepo;
    final private SurchargeDetailRepo surchargeDetailRepo;

    @Override
    public boolean convertFromReservation(ConvertReservationRequestDto requestDto) throws Throwable {
        try {
            requestDto.getOrderedRooms().forEach(ConvertReservationRequestDto.OrderedRoom::formatForSql);
            requestDto.setOrderedRoomsString(Utilities.convertJson(requestDto.getOrderedRooms()));
            List<Object[]> objects = rentalSlipRepo.convertFromReservation(requestDto.getReservationId(), requestDto.getEmployeeId(),
                    requestDto.getArrivalDate(), requestDto.getOrderedRoomsString());
            return true;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    public List<RentalSlipDetailDto> getRentalSlipDetailWithReservation(Integer reservationId) throws Throwable {
        try {
            List<RentalSlipDetail> rentalSlipDetails = rentalSlipRepo.getRentalSlipDetailWithReservation(reservationId);
            return rentalSlipDetails.stream().map(rentalSlipDetail ->
                    ObjectMapperUtils.map(rentalSlipDetail, RentalSlipDetailDto.class)).collect(Collectors.toList());
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getRentalSlipList(String customerId) throws Throwable {
        try {
            return this.rentalSlipRepo.findAll().stream()
                    .filter(rentalSlip -> Objects.isNull(customerId)
                            || (Objects.nonNull(rentalSlip.getReservation())
                                        && Objects.nonNull(rentalSlip.getReservation().getCustomer())
                                        && rentalSlip.getReservation().getCustomer().getId().equals(customerId)))
                    .map(rentalSlip -> convert(rentalSlip, false))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    public List<Map<String, Object>> getRentalSlipListByCustomerId(String customerId) throws Throwable {
        try {
            List<Map<String, Object>> ret = new ArrayList<>();
            List<RentalSlip> allRentalSlip;
            if (Utilities.isNull(customerId) || "null".equals(customerId)) {
                allRentalSlip = this.rentalSlipRepo.findAll();
            } else {
                allRentalSlip = this.rentalSlipRepo.findAllByCustomerId(customerId);
            }
            for (RentalSlip rentalSlip : allRentalSlip) {
                if (rentalSlip.getRentalSlipDetails().stream().allMatch(item -> item.getPaymentStatus().equals(1))) {
                    // Check for any detail have not been export invoice, then add to response
                    for (RentalSlipDetail rentalSlipDetail : rentalSlip.getRentalSlipDetails()) {
                        if (Utilities.isNull(rentalSlipDetail.getInvoice())) {
                            ret.add(convert(rentalSlip, true));
                            break;
                        }
                    }
                }
            }
            return ret;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    private Map<String, Object> convert(RentalSlip rentalSlip, boolean isExportInvoice) {
        Map<String, Object> ret = new HashMap<>();
        boolean isAllPaid = rentalSlip.getRentalSlipDetails().stream().allMatch(item -> item.getPaymentStatus().equals(1));
        ret.put("id", rentalSlip.getId());
        if (Objects.nonNull(rentalSlip.getCustomer())) {
            String customerName = rentalSlip.getCustomer().getFirstName() + " " + rentalSlip.getCustomer().getLastName();
            ret.put("customerName", customerName);
        } else {
            ret.put("customerName", "");
        }
        ret.put("createDate", rentalSlip.getCreatedDate());
        ret.put("startDate", isAllPaid ?
                Collections.min(rentalSlip.getRentalSlipDetails().stream().map(RentalSlipDetail::getArrivalDate)
                        .collect(Collectors.toList()))
                : rentalSlip.getReservation().getStartDate());
        ret.put("endDate", isAllPaid ?
                Collections.max(rentalSlip.getRentalSlipDetails().stream().map(RentalSlipDetail::getDepartureDate)
                        .collect(Collectors.toList()))
                : rentalSlip.getReservation().getEndDate());
        ret.put("deposit", rentalSlip.getReservation().getDeposit());
        double sum = 0d;
        List<RoomDto> rooms = new ArrayList<>();
        for (RentalSlipDetail rentalSlipDetail : rentalSlip.getRentalSlipDetails()) {
            // Get total price of all rooms
            long dayDiff = Utilities.getDayDiff(rentalSlipDetail.getArrivalDate(), rentalSlipDetail.getDepartureDate());
            if (dayDiff == 0) dayDiff = 1;
            sum += rentalSlipDetail.getPrice() * dayDiff;
            // Get total price of service used
            if (Utilities.nonEmptyList(rentalSlipDetail.getServiceDetails())) {
                sum += rentalSlipDetail.getServiceDetails().stream().mapToDouble(ServiceDetail::getPrice).sum();
            }
            // Get total price of surcharge
            if (Utilities.nonEmptyList(rentalSlipDetail.getSurchargeDetails())) {
                sum += rentalSlipDetail.getSurchargeDetails().stream().mapToDouble(SurchargeDetail::getPrice).sum();
            }
            rooms.add(ObjectMapperUtils.map(rentalSlipDetail.getRoom(), RoomDto.class));
        }
        ret.put("total", sum);
        if (isExportInvoice) {
            List<RentalSlipDetail> collect = rentalSlip.getRentalSlipDetails().stream()
                    .filter(item -> Utilities.isNull(item.getInvoice())).collect(Collectors.toList());
            ret.put("rooms", ObjectMapperUtils.mapAll(collect.stream()
                    .map(RentalSlipDetail::getRoom).collect(Collectors.toList()), RoomDto.class));
            ret.put("detailIdList", collect.stream().map(RentalSlipDetail::getId).collect(Collectors.toList()));
        } else {
            ret.put("rooms", rooms);
            ret.put("detailIdList", rentalSlip.getRentalSlipDetails().stream().map(RentalSlipDetail::getId).collect(Collectors.toList()));
        }
        ret.put("status", rentalSlip.getRentalSlipDetails().stream().allMatch(detail -> detail.getPaymentStatus().equals(1)));
        return ret;
    }

    @Override
    public List<RentalSlipDetailResponseDto> getRentalSlipDetailList(Integer rentalSlipId) throws Throwable {
        try {
            List<RentalSlipDetailResponseDto> ret = new ArrayList<>();
            Optional<RentalSlip> byId = rentalSlipRepo.findById(rentalSlipId);
            if (byId.isPresent()) {
                RentalSlip rentalSlip = byId.get();
                List<RentalSlipDetail> rentalSlipDetails = rentalSlip.getRentalSlipDetails();
                for (RentalSlipDetail detail : rentalSlipDetails) {
                    ret.add(getRentalSlipDetailResponseDto(detail));
                }
            }
            return ret;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    public List<RentalSlipDetailResponseDto> getRentalSlipDetail(GetListRentalDetailRequestDto requestDto) throws Throwable {
        List<RentalSlipDetailResponseDto> ret = new ArrayList<>();
        try {
            for (Integer rentalSlipDetailId : requestDto.getRentalSlipDetailIdList()) {
                RentalSlipDetail rentalSlipDetail = rentalSlipDetailRepo.findById(rentalSlipDetailId).orElseThrow();
                // For payment
                if (requestDto.getIsForPayment() && rentalSlipDetail.getPaymentStatus() == 0) {
                    ret.add(getRentalSlipDetailResponseDto(rentalSlipDetail));
                }
                // For export invoice
                if (requestDto.getIsExportInvoice() && rentalSlipDetail.getPaymentStatus() == 1) {
                    if (Utilities.isNull(rentalSlipDetail.getInvoice())) {
                        ret.add(getRentalSlipDetailResponseDto(rentalSlipDetail));
                    }
                }
            }
            return ret;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    public RentalSlipDetailResponseDto getRentalSlipDetailResponseDto(RentalSlipDetail detail) {
        RentalSlipDetailResponseDto item = new RentalSlipDetailResponseDto();
        item.setRentalSlipDetailId(detail.getId());
        item.setRoomId(detail.getRoom().getId());
        String roomName = detail.getRoom().getRoomClassification().getRoomKind().getName()
                + " " + detail.getRoom().getRoomClassification().getRoomType().getName();
        item.setRoomName(roomName);
        item.setRoomStatus(ObjectMapperUtils.map(detail.getRoom().getRoomStatus(), RoomStatusDto.class));
        item.setArrivalDate(detail.getRentalSlip().getReservation().getStartDate());
        item.setCheckInDate(detail.getArrivalDate());
        item.setDepartureDate(detail.getRentalSlip().getReservation().getEndDate());
        item.setCheckOutDate(detail.getPaymentStatus().equals(1) ? detail.getDepartureDate() : new Date());
        item.setRoomPrice(Utilities.nonNull(detail.getPrice()) && (detail.getPrice() != 0) ?
                Double.valueOf(detail.getPrice()) :
                detail.getRentalSlip().getReservation().getReservationDetails()
                        .stream()
                        .filter(reservationDetail -> Objects.equals(reservationDetail.getRoomClassification().getId(),
                                detail.getRoom().getRoomClassification().getId()))
                        .map(ReservationDetail::getPrice)
                        .findFirst()
                        .orElse(0d));
        long dayDiff = Utilities.getDayDiff(item.getCheckInDate(), item.getCheckOutDate());
        item.setStayingDay(dayDiff == 0L ? 1L : dayDiff);
        int totalRooms = detail.getRentalSlip().getReservation().getReservationDetails().stream()
                .mapToInt(ReservationDetail::getNumberOfRooms).sum();
        item.setDeposit(Utilities.nonNull(detail.getRentalSlip().getReservation().getDeposit()) ?
                (detail.getRentalSlip().getReservation().getDeposit() / totalRooms) : 0f);
        item.setTotalRoomPrice(item.getRoomPrice() * item.getStayingDay());
        item.setTotal(item.getTotalRoomPrice() + getTotal(detail));
        item.setPaidAmount(getPaidAmount(detail));
        item.setSurcharge(ObjectMapperUtils.mapAll(detail.getSurchargeDetails(), SurchargeDetailDto.class, "rentalSlipDetail"));
        item.setService(ObjectMapperUtils.mapAll(detail.getServiceDetails(), ServiceDetailDto.class, "rentalSlipDetail"));
        item.setStatus(detail.getPaymentStatus().equals(1));
        return item;
    }

    private double getTotal(RentalSlipDetail detail) {
        double total = 0d;
        if (Utilities.nonEmptyList(detail.getServiceDetails())) {
            total += detail.getServiceDetails().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        }
        if (Utilities.nonEmptyList(detail.getSurchargeDetails())) {
            total += detail.getSurchargeDetails().stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        }
        return total;
    }

    private double getPaidAmount(RentalSlipDetail detail) {
        double ret = 0d;
        if (Utilities.nonEmptyList(detail.getServiceDetails())) {
            for (ServiceDetail serviceDetail : detail.getServiceDetails()) {
                if (serviceDetail.getStatus() == 1) {
                    ret += serviceDetail.getPrice() * serviceDetail.getQuantity();
                }
            }
        }
        if (Utilities.nonEmptyList(detail.getSurchargeDetails())) {
            for (SurchargeDetail surchargeDetail : detail.getSurchargeDetails()) {
                if (surchargeDetail.getStatus() == 1) {
                    ret += surchargeDetail.getPrice() * surchargeDetail.getQuantity();
                }
            }
        }
        return ret;
    }

    @Override
    @Transactional
    public boolean checkOutByRoom(CheckOutByRoomRequestDto requestDto) throws Throwable {
        try {
            requestDto.setRentalSlipDetailIdString(Utilities.listToCsv(requestDto.getRentalSlipDetailIdList()));
            rentalSlipRepo.checkOutByRoom(requestDto.getRentalSlipDetailIdString(), requestDto.getEmployeeId());
            return true;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }

    @Override
    @Transactional
    public boolean quickCheckIn(QuickCheckInRequestDto requestDto) throws Throwable {
        boolean result = false;
        try {
            requestDto.setCustomerIdString(Utilities.listToCsv(requestDto.getCustomerIdList()));
            rentalSlipRepo.quickCheckIn(requestDto.getEmployeeId(), requestDto.getStartDate(), requestDto.getEndDate(),
                    requestDto.getRoomClassId(), requestDto.getRoomId(), requestDto.getPrice(),
                    requestDto.getCustomerIdString(), requestDto.getMainCustomerId());
            result = true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
        return result;
    }

    @Override
    public boolean saveRentalSlipDetail(List<RentalSlipDetailSaveDto> requestDto) throws Exception {
        try {
            for (RentalSlipDetailSaveDto rentalSlipDetailSaveDto : requestDto) {
                removeServiceDetail(rentalSlipDetailSaveDto);
                removeSurchargeDetail(rentalSlipDetailSaveDto);
                for (ServiceDetailDto serviceDetailDto : rentalSlipDetailSaveDto.getServiceDetailDtos()) {
                    serviceService.saveWithRentalSlip(serviceDetailDto);
                }
                for (SurchargeDetailDto surchargeDetailDto : rentalSlipDetailSaveDto.getSurchargeDetailDtos()) {
                    surchargeService.saveWithRentalSlip(surchargeDetailDto);
                }
            }
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void removeSurchargeDetail(RentalSlipDetailSaveDto rentalSlipDetailSaveDto) throws Exception {
        try {
            List<SurchargeDetailDto> surchargeDetailDtoAll = surchargeDetailRepo.findAll().stream().map(surchargeDetail -> {
                        return ObjectMapperUtils.map(surchargeDetail, SurchargeDetailDto.class);
                    }).filter(serviceDetailDto -> serviceDetailDto.getStatus().equals(0))
                    .collect(Collectors.toList());
            List<String> surchargeIdInRentalSlipDetail = surchargeDetailDtoAll.stream()
                    .filter(surchargeDetailDto -> surchargeDetailDto.getRentalSlipDetail()
                            .getId().equals(rentalSlipDetailSaveDto.getRentalSlipDetailId())).map(surchargeDetailDto -> {
                        return surchargeDetailDto.getSurcharge().getId();
                    }).collect(Collectors.toList());
            List<String> surchargeIds = rentalSlipDetailSaveDto.getSurchargeDetailDtos().stream().map(
                    serviceDetailDto -> {
                        return serviceDetailDto.getSurcharge().getId();
                    }
            ).collect(Collectors.toList());
            for (String surchargeDetailId : surchargeIdInRentalSlipDetail) {
                if (!surchargeIds.contains(surchargeDetailId)) {
                    this.surchargeDetailRepo.removeSurchargeDetail(surchargeDetailId, rentalSlipDetailSaveDto.getRentalSlipDetailId());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void removeServiceDetail(RentalSlipDetailSaveDto rentalSlipDetailSaveDto) throws Exception {
        try {
            List<ServiceDetailDto> serviceDetailDtoAll = serviceDetailRepo.findAll().stream().map(serviceDetail -> {
                        return ObjectMapperUtils.map(serviceDetail, ServiceDetailDto.class);
                    }).filter(serviceDetailDto -> serviceDetailDto.getStatus().equals(0))
                    .collect(Collectors.toList());
            List<String> serviceIdInRentalSlipDetail = serviceDetailDtoAll.stream()
                    .filter(serviceDetailDto -> serviceDetailDto.getRentalSlipDetail()
                            .getId().equals(rentalSlipDetailSaveDto.getRentalSlipDetailId())).map(serviceDetailDto -> {
                        return serviceDetailDto.getService().getId();
                    }).collect(Collectors.toList());
            List<String> serviceIds = rentalSlipDetailSaveDto.getServiceDetailDtos().stream().map(
                    serviceDetailDto -> {
                        return serviceDetailDto.getService().getId();
                    }
            ).collect(Collectors.toList());
            for (String serviceDetailId : serviceIdInRentalSlipDetail) {
                if (!serviceIds.contains(serviceDetailId)) {
                    this.serviceDetailRepo.removeServiceDetail(serviceDetailId, rentalSlipDetailSaveDto.getRentalSlipDetailId());
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getRevenue(GetRevenueRequestDto requestDto) {
        List<Object[]> objectsRevenue = this.rentalSlipRepo.getRevenue(requestDto.getDateFrom(), requestDto.getDateTo());
        Map<String, Object> ret = new HashMap<>();
        ret.put("chartRevenue", mapToChartDataRevenue(objectsRevenue));
        ret.put("dataRevenue", objectsRevenue);
        List<Object[]> objectsRoomRate = this.rentalSlipRepo.getRoomBookingRate(requestDto.getDateFrom(), requestDto.getDateTo());
        ret.put("chartRoomRate", mapToChartDataRoomBookingRate(objectsRoomRate));
        ret.put("dataRoomRate", objectsRoomRate);
        return ret;
    }

    private static Map<String, Object> mapToChartDataRevenue(List<Object[]> resultList) {
        // Assuming you have a Map object to represent the JavaScript structure
        Map<String, Object> chartData = new HashMap<>();

        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        for (Object[] result : resultList) {
            labels.add(Utilities.parseString(result[0]) + "-" + Utilities.parseString(result[1]));
            data.add(Utilities.parseBigDecimal(result[2]));
        }

        // Set the labels and data in the chartData map
        chartData.put("labels", labels);

        // Assuming you have multiple datasets, create a list to hold them
        List<Map<String, Object>> datasets = new ArrayList<>();
        Map<String, Object> dataset = new HashMap<>();

        // Set the data and label for the dataset
        dataset.put("data", data);
        dataset.put("label", "Doanh thu");

        // Add the dataset to the list
        datasets.add(dataset);

        // Set the datasets in the chartData map
        chartData.put("datasets", datasets);

        return chartData;
    }

    private static Map<String, Object> mapToChartDataRoomBookingRate(List<Object[]> resultList) {
        // Assuming you have a Map object to represent the JavaScript structure
        Map<String, Object> chartData = new HashMap<>();

        List<String> labels = new ArrayList<>();
        List<Integer> data = new ArrayList<>();

        for (Object[] result : resultList) {
            if (Utilities.parseInt(result[3]) != 0) {
                labels.add(Utilities.parseString(result[2]) + " - " + Utilities.parseString(result[1]));
                data.add(Utilities.parseInt(result[4]));
            }
        }

        // Set the labels and data in the chartData map
        chartData.put("labels", labels);

        // Assuming you have multiple datasets, create a list to hold them
        List<Map<String, Object>> datasets = new ArrayList<>();
        Map<String, Object> dataset = new HashMap<>();

        // Set the data and label for the dataset
        dataset.put("data", data);

        // Add the dataset to the list
        datasets.add(dataset);

        // Set the datasets in the chartData map
        chartData.put("datasets", datasets);

        return chartData;
    }
}
