package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.RequestNewRoomClassification;
import com.my.hotel.dto.RequestNewServiceDto;
import com.my.hotel.dto.RoomClassificationDto;
import com.my.hotel.dto.RoomTypeDto;
import com.my.hotel.dto.ServiceDetailDto;
import com.my.hotel.dto.ServiceDto;
import com.my.hotel.dto.request.AlterRentalDetailRequestDto;
import com.my.hotel.dto.request.CreateNewRoomClassPriceRequestDto;
import com.my.hotel.dto.request.CreateNewServicePriceRequestDto;
import com.my.hotel.dto.request.PayForServiceRequestDto;
import com.my.hotel.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_SERVICE)
public class ServiceController {
    @Autowired
    private ServiceService serviceService;
    
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        log.info("API getAllService");
        try {
            List<ServiceDto> serviceDtos = serviceService.getAllServices();
            log.info("API getAllService END with OUTPUT response={}", serviceDtos);
            return new ResponseEntity<>(serviceDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API getAllService END with error:", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        ServiceDto service = this.serviceService.getById(id);
        return new ResponseEntity<>(service, HttpStatus.OK);
    }

    @PostMapping("/add-new-price")
    public ResponseEntity<?> createNewServicePrice(@RequestBody CreateNewServicePriceRequestDto requestDto) {
        log.info("API createNewServicePrice STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = serviceService.createNewPrice(requestDto);
            response.put("result", result);
            log.info("API createNewServicePrice END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API createNewServicePrice END with OUTPUT error={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-new")
    public ResponseEntity<?> createService(@RequestBody RequestNewServiceDto requestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = serviceService.createNewService(requestDto);
            response.put("result", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editService(@RequestBody ServiceDto serviceDto) {
        log.info("API editService STARTED with INPUT serviceDto={}", serviceDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = serviceService.save(serviceDto);
            response.put("result", result);
            log.info("API editService END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API editService END with an Error: ", e);
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") String id) {
        log.info("API deleteService STARTED with INPUT id={}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = serviceService.delete(id);
            response.put("result", result);
            log.info("API deleteService END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API deleteService END with an Error: ", e);
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PutMapping("/admin/pay")
    public ResponseEntity<?> payForService(@RequestBody PayForServiceRequestDto requestDto) {
        log.info("API payForService STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = serviceService.payForService(requestDto.getRentalSlipDetailId(), requestDto.getServiceId());
            response.put("result", result);
            log.info("API payForService END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API payForService END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/admin/add-list")
    public ResponseEntity<?> addServiceToRentalSlip(@RequestBody AlterRentalDetailRequestDto requestDto) {
        log.info("API addServiceToRentalSlip STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = serviceService.addServiceToRentalSlip(requestDto);
            response.put("result", result);
            log.info("API addServiceToRentalSlip END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API addServiceToRentalSlip END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/admin/save-service-detail-with-rental-slip-detail")
    public ResponseEntity<?> saveServiceDetailWithRentalSlipDetail(@RequestBody ServiceDetailDto requestDto) {
        log.info("API saveServiceDetailWithRentalSlipDetail STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = serviceService.saveWithRentalSlip(requestDto);
            response.put("result", result);
            log.info("API saveServiceDetailWithRentalSlipDetail END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API saveServiceDetailWithRentalSlipDetail END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
