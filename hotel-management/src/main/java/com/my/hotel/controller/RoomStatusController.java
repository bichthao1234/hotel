package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.RoomKindDto;
import com.my.hotel.dto.RoomStatusDto;
import com.my.hotel.dto.RoomTypeDto;
import com.my.hotel.service.RoomStatusService;
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
@RequestMapping(value = Routes.URI_REST_ROOM_STATUS)
//@PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
public class RoomStatusController {
    @Autowired
    private RoomStatusService roomStatusService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRoomTypes() {
        List<RoomStatusDto> roomStatus = this.roomStatusService.getAllRoomStatus()
                .stream().sorted(Comparator.comparing(RoomStatusDto::getId)).collect(Collectors.toList());
        return new ResponseEntity<>(roomStatus, HttpStatus.OK);
    }

}
