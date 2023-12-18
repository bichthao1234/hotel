package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.RentalSlipDetailDto;
import com.my.hotel.dto.RentalSlipDetailSaveDto;
import com.my.hotel.dto.request.CheckOutByRoomRequestDto;
import com.my.hotel.dto.request.ConvertReservationRequestDto;
import com.my.hotel.dto.request.GetListRentalDetailRequestDto;
import com.my.hotel.dto.request.GetRevenueRequestDto;
import com.my.hotel.dto.request.QuickCheckInRequestDto;
import com.my.hotel.dto.response.RentalSlipDetailResponseDto;
import com.my.hotel.service.RentalSlipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_RENTAL_SLIP)
public class RentalSlipController {

    @Autowired
    private RentalSlipService rentalSlipService;

    @PostMapping("/convert")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> convertFromReservation(@RequestBody ConvertReservationRequestDto requestDto) throws Throwable {
        log.info("API convertFromReservation STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = rentalSlipService.convertFromReservation(requestDto);
            response.put("result", result);
            log.info("API convertFromReservation END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API convertFromReservation END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/getRentalSlipDetailsWithReservation/{reservationId}")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getRentalSlipDetailWithReservation(@PathVariable Integer reservationId) throws Throwable {
        log.info("API getRentalSlipDetailWithReservation STARTED with INPUT reservationId={}", reservationId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<RentalSlipDetailDto> result = rentalSlipService.getRentalSlipDetailWithReservation(reservationId);
            log.info("API getRentalSlipDetailWithReservation END with OUTPUT response={}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getRentalSlipDetailWithReservation END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/admin/all")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getAllRentalSlip(@RequestBody Map<String, String> customerId) throws Throwable {
        log.info("API getAllRentalSlip STARTED");
        Map<String, Object> response = new HashMap<>();
        try {
            List<?> result = rentalSlipService.getRentalSlipList(customerId.get("customerId"));
            log.info("API getAllRentalSlip END");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getAllRentalSlip END");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/admin/invoice")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getRentalSlipListByCustomerId(@RequestParam("customerId") String customerId) throws Throwable {
        log.info("API getRentalSlipListByCustomerId STARTED with INPUT customerId={}", customerId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<?> result = rentalSlipService.getRentalSlipListByCustomerId(customerId);
            log.info("API getRentalSlipListByCustomerId END with OUTPUT result={}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getAllRentalSlip END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/admin/detail-list/{rentalSlipId}")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getRentalSlipDetailList(@PathVariable("rentalSlipId") Integer rentalSlipId) throws Throwable {
        log.info("API getRentalSlipDetailList STARTED with INPUT rentalSlipId={}", rentalSlipId);
        Map<String, Object> response = new HashMap<>();
        try {
            List<?> result = rentalSlipService.getRentalSlipDetailList(rentalSlipId);
            log.info("API getRentalSlipDetailList END with OUTPUT result={}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getRentalSlipDetailList END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/admin/detail")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getRentalSlipDetail(@RequestBody GetListRentalDetailRequestDto requestDto) throws Throwable {
        log.info("API getRentalSlipDetail STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            List<RentalSlipDetailResponseDto> result = rentalSlipService.getRentalSlipDetail(requestDto);
            log.info("API getRentalSlipDetail END with OUTPUT response={}", response);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getRentalSlipDetail END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkOutByRoom(@RequestBody CheckOutByRoomRequestDto requestDto) throws Throwable {
        log.info("API checkOutByRoom STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = rentalSlipService.checkOutByRoom(requestDto);
            response.put("result", result);
            log.info("API checkOutByRoom END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API checkOutByRoom END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/check-in/quick")
    public ResponseEntity<?> quickCheckIn(@RequestBody QuickCheckInRequestDto requestDto) throws Throwable {
        log.info("API quickCheckIn STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = rentalSlipService.quickCheckIn(requestDto);
            response.put("result", result);
            log.info("API quickCheckIn END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API quickCheckIn END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }

    }

    @PostMapping("/save-rental-slip-detail")
    public ResponseEntity<?> saveRentalSlipDetail(@RequestBody List<RentalSlipDetailSaveDto> requestDto) throws Throwable {
        log.info("API quickCheckIn STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = rentalSlipService.saveRentalSlipDetail(requestDto);
            log.info("API quickCheckIn END with OUTPUT response={}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API quickCheckIn END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/report")
    public ResponseEntity<?> getRevenue(@RequestBody GetRevenueRequestDto requestDto) {
        log.info("API getRevenue STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            response = rentalSlipService.getRevenue(requestDto);
            log.info("API getRevenue END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getRevenue END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
