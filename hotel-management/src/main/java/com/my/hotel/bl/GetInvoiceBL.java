package com.my.hotel.bl;

import com.my.hotel.dto.ServiceDetailDto;
import com.my.hotel.dto.SurchargeDetailDto;
import com.my.hotel.dto.response.InvoiceResponseDto;
import com.my.hotel.dto.response.RentalSlipDetailResponseDto;
import com.my.hotel.entity.Invoice;
import com.my.hotel.entity.PriceRoomClassification;
import com.my.hotel.entity.PriceService;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.entity.Reservation;
import com.my.hotel.entity.ReservationDetail;
import com.my.hotel.entity.ServiceDetail;
import com.my.hotel.repo.InvoiceRepo;
import com.my.hotel.repo.RentalSlipDetailRepo;
import com.my.hotel.service.RentalSlipService;
import com.my.hotel.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GetInvoiceBL {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private RentalSlipDetailRepo rentalSlipDetailRepo;

    @Autowired
    private RentalSlipService rentalSlipService;

    public List<InvoiceResponseDto> getAll() {
        List<Invoice> invoices = invoiceRepo.findAll();
        return invoices.stream().map(invoice -> {
            return this.getInvoiceByRentalDetailId(invoice.getId());
        }).collect(Collectors.toList());
    }

    public InvoiceResponseDto getInvoiceByRentalDetailId(Integer invoiceId) {
        InvoiceResponseDto response = new InvoiceResponseDto();
        Invoice invoice = invoiceRepo.findById(invoiceId).orElseThrow();
        response.setInvoiceId(invoice.getId());
        response.setCreatedDate(invoice.getCreatedDate());
        response.setEmployeeName(invoice.getCreatedBy().getFirstName() + " " + invoice.getCreatedBy().getLastName());
        response.setCustomer(InvoiceResponseDto.Customer.builder()
                .id(invoice.getRentalSlip().getCustomer().getId())
                .name(invoice.getRentalSlip().getCustomer().getFirstName() + " " + invoice.getRentalSlip().getCustomer().getLastName())
                .phone(invoice.getRentalSlip().getCustomer().getPhoneNumber())
                .build());
        response.setReservation(InvoiceResponseDto.Reservation.builder()
                .startDate(invoice.getRentalSlip().getReservation().getStartDate())
                .endDate(invoice.getRentalSlip().getReservation().getEndDate())
                .createdDate(invoice.getRentalSlip().getReservation().getCreatedDate())
                .build());
        response.setRentalSlipId(invoice.getRentalSlip().getId());
        List<RentalSlipDetail> rentalSlipDetailList = invoice.getRentalSlipDetails();
        List<RentalSlipDetailResponseDto> list = new ArrayList<>();
        for (RentalSlipDetail rentalSlipDetail : rentalSlipDetailList) {
            list.add(rentalSlipService.getRentalSlipDetailResponseDto(rentalSlipDetail));
        }
        for (RentalSlipDetailResponseDto rentalSlipDetailResponseDto : list) {
            response.getRoomList().add(InvoiceResponseDto.Room.builder()
                    .id(rentalSlipDetailResponseDto.getRoomId())
                    .name(rentalSlipDetailResponseDto.getRoomName())
                    .price(rentalSlipDetailResponseDto.getRoomPrice())
                    .arrivalDate(rentalSlipDetailResponseDto.getArrivalDate())
                    .departureDate(rentalSlipDetailResponseDto.getDepartureDate())
                    .stayingDay(rentalSlipDetailResponseDto.getStayingDay())
                    .build());
            addSurcharge(rentalSlipDetailResponseDto, response.getSurchargeList());
            addService(rentalSlipDetailResponseDto, response.getServiceList());
        }
        response.setTotalInvoice(list.stream().mapToDouble(RentalSlipDetailResponseDto::getTotal).sum());
        response.setDeposit(invoice.getRentalSlip().getReservation().getDeposit() /
                invoice.getRentalSlip().getRentalSlipDetails().size() * invoice.getRentalSlipDetails().size());
        response.setPromotion(getPromotion(invoice));
        return response;
    }

    private Double getPromotion(Invoice invoice) {
        double ret = 0d;
        for (RentalSlipDetail rentalSlipDetail : invoice.getRentalSlip().getRentalSlipDetails()) {
            if (Utilities.nonNull(rentalSlipDetail.getInvoice())) {
                // Get original price of room classification
                PriceRoomClassification priceRoomClassification = rentalSlipDetail.getRoom().getRoomClassification().getPriceRoomClassifications().stream()
                        .filter(dto -> !dto.getAppliedDate().after(rentalSlipDetail.getRentalSlip().getReservation().getCreatedDate()))
                        .max(Comparator.comparing(PriceRoomClassification::getAppliedDate)).orElseThrow();
                Double priceOriginal = priceRoomClassification.getPrice();
                // Get price in the reservation
                Double priceAfterPromotion;
                if (Utilities.nonNull(rentalSlipDetail.getPrice()) && rentalSlipDetail.getPrice() != 0) {
                    priceAfterPromotion = Double.valueOf(rentalSlipDetail.getPrice());
                } else {
                    priceAfterPromotion = rentalSlipDetail.getRentalSlip().getReservation().getReservationDetails()
                            .stream()
                            .filter(reservationDetail -> Objects.equals(reservationDetail.getRoomClassification().getId(),
                                    rentalSlipDetail.getRoom().getRoomClassification().getId()))
                            .map(ReservationDetail::getPrice)
                            .findFirst()
                            .orElse(0d);
                }
                ret += priceOriginal - priceAfterPromotion;
            }
        }
        return ret;
    }

    private void addSurcharge(RentalSlipDetailResponseDto rentalSlipDetailResponseDto, List<InvoiceResponseDto.Surcharge> surchargeList) {
        for (SurchargeDetailDto surchargeDetailDto : rentalSlipDetailResponseDto.getSurcharge()) {
            String surchargeId = surchargeDetailDto.getSurcharge().getId();
            List<String> collect = surchargeList.stream().map(InvoiceResponseDto.Surcharge::getId).collect(Collectors.toList());
            if (collect.contains(surchargeId)) {
                InvoiceResponseDto.Surcharge surcharge = surchargeList.stream()
                        .filter(item -> item.getId().equals(surchargeId)).findFirst().orElseThrow();
                surcharge.setQuantity(surcharge.getQuantity() + surchargeDetailDto.getQuantity());
            } else {
                surchargeList.add(InvoiceResponseDto.Surcharge.builder()
                        .id(surchargeDetailDto.getSurcharge().getId())
                        .name(surchargeDetailDto.getSurcharge().getName())
                        .description(surchargeDetailDto.getSurcharge().getDescription())
                        .price(surchargeDetailDto.getPrice())
                        .quantity(surchargeDetailDto.getQuantity())
                        .status(surchargeDetailDto.getStatus())
                        .build());
            }
        }
    }

    private void addService(RentalSlipDetailResponseDto rentalSlipDetailResponseDto, List<InvoiceResponseDto.Service> serviceList) {
        for (ServiceDetailDto serviceDetailDto : rentalSlipDetailResponseDto.getService()) {
            String serviceId = serviceDetailDto.getService().getId();
            List<String> collect = serviceList.stream().map(InvoiceResponseDto.Service::getId).collect(Collectors.toList());
            if (collect.contains(serviceId)) {
                InvoiceResponseDto.Service surcharge = serviceList.stream()
                        .filter(item -> item.getId().equals(serviceId)).findFirst().orElseThrow();
                surcharge.setQuantity(surcharge.getQuantity() + serviceDetailDto.getQuantity());
            } else {
                serviceList.add(InvoiceResponseDto.Service.builder()
                        .id(serviceDetailDto.getService().getId())
                        .name(serviceDetailDto.getService().getName())
                        .description(serviceDetailDto.getService().getDescription())
                        .price(serviceDetailDto.getPrice())
                        .quantity(serviceDetailDto.getQuantity())
                        .status(serviceDetailDto.getStatus())
                        .build());
            }
        }
    }
}
