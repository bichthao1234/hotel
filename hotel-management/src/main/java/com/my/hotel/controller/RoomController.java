package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.ChangeRoomStatusDto;
import com.my.hotel.dto.NewRoomReqDto;
import com.my.hotel.dto.ReservationDetailDto;
import com.my.hotel.dto.request.GetRoomsRequestDto;
import com.my.hotel.dto.RoomDto;
import com.my.hotel.service.RoomService;
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
@RequestMapping(value = Routes.URI_REST_ROOM)
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/admin/getAllRooms")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getAllRooms(@RequestBody GetRoomsRequestDto requestDto) throws Throwable { 
        log.info("API getAllRooms STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            List<RoomDto> result = roomService.getAllRooms(requestDto);
            response.put("result", result);
            log.info("API bookRoom END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API bookRoom END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/admin/available/{roomClassId}")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getAllAvailableRooms(@PathVariable("roomClassId") String roomClassId) throws Throwable {
        log.info("API getAllAvailableRooms STARTED with INPUT roomClassId={}", roomClassId);
        List<RoomDto> response = roomService.getAllAvailableRooms(roomClassId);
        log.info("API getAllAvailableRooms END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/rental/detail")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getRentalSlipDetailByRoom(@RequestParam("roomId") String roomId) throws Throwable {
        log.info("API getRentalSlipDetailByRoom STARTED with INPUT roomId={}", roomId);
        List<Map<String, Object>> response = roomService.getRentalSlipDetailByRoom(roomId);
        log.info("API getRentalSlipDetailByRoom END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/rental/guest")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getGuestManifestByRoom(@RequestParam("roomId") String roomId) throws Throwable {
        log.info("API getGuestManifestByRoom STARTED with INPUT roomId={}", roomId);
        List<Map<String, Object>> response = roomService.getGuestManifestByRoom(roomId);
        log.info("API getGuestManifestByRoom END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/rental/service")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getServiceByRoom(@RequestParam("roomId") String roomId) throws Throwable {
        log.info("API getServiceByRoom STARTED with INPUT roomId={}", roomId);
        List<Map<String, Object>> response = roomService.getServiceByRoom(roomId);
        log.info("API getServiceByRoom END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/rental/surcharge")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> getSurchargeByRoom(@RequestParam("roomId") String roomId) throws Throwable {
        log.info("API getSurchargeByRoom STARTED with INPUT roomId={}", roomId);
        List<Map<String, Object>> response = roomService.getSurchargeByRoom(roomId);
        log.info("API getSurchargeByRoom END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/change-room-status")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> changeRoomStatus(@RequestBody ChangeRoomStatusDto requestDto) throws Throwable {
        log.info("API changeRoomStatus STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomService.changeRoomStatus(requestDto);
            log.info("API changeRoomStatus END with OUTPUT response={}", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API changeRoomStatus END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/create-new-room")
    public ResponseEntity<?> createNewRoom(@RequestBody NewRoomReqDto requestDto) throws Throwable {
        log.info("API createNewRoom STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomService.createRoom(requestDto);
            log.info("API createNewRoom END with OUTPUT response={}", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API createNewRoom END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/modify-room")
    public ResponseEntity<?> modifyRoom(@RequestBody NewRoomReqDto requestDto) throws Throwable {
        log.info("API modifyRoom STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomService.modifyRoom(requestDto);
            log.info("API modifyRoom END with OUTPUT response={}", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API modifyRoom END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
