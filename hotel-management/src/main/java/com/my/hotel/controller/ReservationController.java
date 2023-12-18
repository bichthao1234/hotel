package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.ReservationDetailDto;
import com.my.hotel.dto.ReservationDto;
import com.my.hotel.dto.request.BookRoomRequestDto;
import com.my.hotel.dto.request.ChangeDateReservationRequestDto;
import com.my.hotel.dto.request.CheckRoomRequestDto;
import com.my.hotel.dto.request.GetReservationListRequestDto;
import com.my.hotel.dto.request.PriceRoomClassRequestDto;
import com.my.hotel.dto.response.PriceRoomClassResponseDto;
import com.my.hotel.service.ReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_RESERVATION)
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/book")
    public ResponseEntity<?> bookRoom(@RequestBody BookRoomRequestDto requestDto) throws Throwable {
        log.info("API bookRoom STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = reservationService.bookRoom(requestDto);
            response.put("result", result);
            log.info("API bookRoom END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API bookRoom END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/check")
    public ResponseEntity<?> checkRoom(@RequestBody CheckRoomRequestDto requestDto) throws Throwable {
        log.info("API checkRoom STARTED with INPUT requestDto={}", requestDto);
        List<Map<String, Object>> response = reservationService.checkRoom(requestDto);
        log.info("API checkRoom END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/price")
    public ResponseEntity<?> getRoomClassPrice(@RequestBody PriceRoomClassRequestDto requestDto) {
        log.info("API getRoomClassPrice STARTED with INPUT request={}", requestDto);
        PriceRoomClassResponseDto response = reservationService.getRoomClassPrice(requestDto);
        log.info("API getRoomClassPrice END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/image/{roomClassId}")
    public ResponseEntity<?> getImagePathById(@PathVariable Integer roomClassId) {
        log.info("API getImagePathById STARTED with INPUT roomClassId={}", roomClassId);
        Map<String, String> response = new HashMap<>();
        try {
            String imagePath = reservationService.getImagePathById(roomClassId);
            response.put("result", imagePath);
            log.info("API getImagePathById END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getImagePathById END with OUTPUT response={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/list")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getReservationList(@RequestBody GetReservationListRequestDto requestDto) throws Throwable {
        log.info("API getReservationList STARTED with INPUT request={}", requestDto);
        List<ReservationDto> response = reservationService.getReservationList(requestDto);
        log.info("API getReservationList END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/list/detail/{reservationId}")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getReservationDetail(@PathVariable("reservationId") Integer reservationId) throws Throwable {
        try {
            log.info("API getReservationDetail STARTED with INPUT reservationId={}", reservationId);
            List<ReservationDetailDto> response = reservationService.getReservationDetail(reservationId);
            log.info("API getReservationDetail END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list/rental/{reservationId}")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getRentalSlipId(@PathVariable("reservationId") Integer reservationId) throws Throwable {
        try {
            Map<String, Object> response = new HashMap<>();
            log.info("API getRentalSlipId STARTED with INPUT reservationId={}", reservationId);
            response.put("response", reservationService.getRentalSlipId(reservationId));
            log.info("API getRentalSlipId END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/list/cancel")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> cancelReservation(@RequestParam("reservationId") Integer reservationId,
                                               @RequestParam("employeeId") String employeeId) throws Throwable {
        log.info("API cancelReservation STARTED with INPUT reservationId={}", reservationId);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = reservationService.cancelReservation(reservationId, employeeId);
            response.put("result", result);
            log.info("API cancelReservation END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API cancelReservation END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/list/update-date")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> changeDateRangeReservation(@RequestBody ChangeDateReservationRequestDto requestDto) throws Throwable {
        log.info("API changeDateRangeReservation STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = reservationService.changeDateRangeReservation(requestDto);
            response.put("result", result);
            log.info("API changeDateRangeReservation END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API cancelReservation END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
