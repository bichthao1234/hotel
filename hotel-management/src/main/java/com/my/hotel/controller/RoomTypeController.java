package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.JwtWrapperDto;
import com.my.hotel.dto.LoginRequestDto;
import com.my.hotel.dto.RoomKindDto;
import com.my.hotel.dto.RoomTypeDto;
import com.my.hotel.service.AuthService;
import com.my.hotel.service.RoomTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_ROOM_TYPE)
//@PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
public class RoomTypeController {
    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRoomTypes() {
        List<RoomTypeDto> roomTypeDtos = this.roomTypeService.getAllRoomType()
                .stream().sorted(Comparator.comparing(RoomTypeDto::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(roomTypeDtos, HttpStatus.OK);
    }

    @PostMapping("/getByName")
    public ResponseEntity<?> geByName(@RequestBody Map<String, String> name) {
        List<RoomTypeDto> roomTypeDtos = this.roomTypeService.findByName(name.get("name"));
        return new ResponseEntity<>(roomTypeDtos, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addNewRoomType(@RequestBody RoomTypeDto roomTypeDto) {
        log.info("API addNewRoomType STARTED with INPUT roomTypeDto={}", roomTypeDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomTypeService.save(roomTypeDto);
            response.put("result", result);
            log.info("API addNewRoomType END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API addNewRoomType END with an Error: ",  e.getMessage());
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editRoomType(@RequestBody RoomTypeDto roomTypeDto) {
        log.info("API editRoomType STARTED with INPUT roomTypeDto={}", roomTypeDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomTypeService.save(roomTypeDto);
            response.put("result", result);
            log.info("API editRoomType END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API editRoomType END with an Error: ",  e.getMessage());
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
}
