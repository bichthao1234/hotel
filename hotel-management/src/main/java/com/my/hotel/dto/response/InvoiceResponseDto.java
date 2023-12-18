package com.my.hotel.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String employeeName;
    private Customer customer;
    private Reservation reservation;
    private Integer rentalSlipId;
    private Integer invoiceId;
    private Date createdDate;
    private List<Room> roomList = new ArrayList<>();
    private List<Surcharge> surchargeList = new ArrayList<>();
    private List<Service> serviceList = new ArrayList<>();
    private Double totalInvoice;
    private Float deposit;
    private Double promotion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {
        private String id;
        private String name;
        private String phone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reservation {
        private Date startDate;
        private Date endDate;
        private Date createdDate;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Room {
        private String id;
        private String name;
        private Double price;
        private Date arrivalDate;
        private Date departureDate;
        private Long stayingDay;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Service {
        private String id;
        private String name;
        private String description;
        private Float price;
        private Integer quantity;
        private Integer status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Surcharge {
        private String id;
        private String name;
        private String description;
        private Float price;
        private Integer quantity;
        private Integer status;
    }
}
