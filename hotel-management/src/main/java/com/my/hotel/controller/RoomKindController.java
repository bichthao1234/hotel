package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.RoomKindDto;
import com.my.hotel.dto.RoomTypeDto;
import com.my.hotel.service.RoomKindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_ROOM_KIND)
//@PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
public class RoomKindController {
    @Autowired
    private RoomKindService roomKindService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRoomTypes() {
        List<RoomKindDto> roomKindDtos = this.roomKindService.getAllRoomType()
                .stream().sorted(Comparator.comparing(RoomKindDto::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(roomKindDtos, HttpStatus.OK);
    }

    @PostMapping("/getByName")
    public ResponseEntity<?> geByName(@RequestBody Map<String, String> name) {
        List<RoomKindDto> roomTypeDtos = this.roomKindService.findByName(name.get("name"));
        return new ResponseEntity<>(roomTypeDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewRoomKind(@RequestBody RoomKindDto roomKindDto) {
        log.info("API addNewRoomKind STARTED with INPUT roomKindDto={}", roomKindDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomKindService.addNew(roomKindDto);
            response.put("result", result);
            log.info("API addNewRoomKind END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API addNewRoomKind END with an Error: ",  e.getMessage());
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
    @PostMapping("/edit")
    public ResponseEntity<?> editRoomKind(@RequestBody RoomKindDto roomKindDto) {
        log.info("API addNewRoomKind STARTED with INPUT roomKindDto={}", roomKindDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomKindService.save(roomKindDto);
            response.put("result", result);
            log.info("API addNewRoomKind END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API addNewRoomKind END with an Error: ",  e.getMessage());
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
}
