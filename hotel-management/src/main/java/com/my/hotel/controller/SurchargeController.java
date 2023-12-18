package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.RequestNewSurchargeDto;
import com.my.hotel.dto.SurchargeDetailDto;
import com.my.hotel.dto.SurchargeDto;
import com.my.hotel.dto.request.CreateNewSurchargePriceRequestDto;
import com.my.hotel.service.SurchargeService;
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
@RequestMapping(value = Routes.URI_REST_SURCHARGE)
public class SurchargeController {
    @Autowired
    private SurchargeService surchargeService;
    
    @GetMapping("/admin/get-all")
    public ResponseEntity<?> getAll() {
        try {
            List<SurchargeDto> surchargeDetailDtos = surchargeService.getAll();
            return new ResponseEntity<>(surchargeDetailDtos, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        SurchargeDto surcharge = this.surchargeService.getById(id);
        return new ResponseEntity<>(surcharge, HttpStatus.OK);
    }

    @PostMapping("/add-new-price")
    public ResponseEntity<?> createNewSurchargePrice(@RequestBody CreateNewSurchargePriceRequestDto requestDto) {
        log.info("API createNewSurchargePrice STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = surchargeService.createNewPrice(requestDto);
            response.put("result", result);
            log.info("API createNewSurchargePrice END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API createNewSurchargePrice END with OUTPUT error={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-new")
    public ResponseEntity<?> createSurcharge(@RequestBody RequestNewSurchargeDto requestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = surchargeService.createNewSurcharge(requestDto);
            response.put("result", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editSurcharge(@RequestBody SurchargeDto surchargeDto) {
        log.info("API editSurcharge STARTED with INPUT surchargeDto={}", surchargeDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = surchargeService.save(surchargeDto);
            response.put("result", result);
            log.info("API editSurcharge END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API editSurcharge END with an Error: ", e);
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deleteSurcharge(@PathVariable("id") String id) {
        log.info("API deleteSurcharge STARTED with INPUT id={}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = surchargeService.delete(id);
            response.put("result", result);
            log.info("API deleteSurcharge END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API deleteSurcharge END with an Error: ", e);
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
    
    @PostMapping("/admin/save-surcharge-detail-with-rental-slip-detail")
    public ResponseEntity<?> saveSurchargeDetailWithRentalSlipDetail(@RequestBody SurchargeDetailDto requestDto) {
        log.info("API saveSurchargeDetailWithRentalSlipDetail STARTED with INPUT requestDto={}", requestDto);
        boolean result = false;
        Map<String, Object> response = new HashMap<>();
        try {
            result = surchargeService.saveWithRentalSlip(requestDto);
            response.put("result", result);
            log.info("API saveSurchargeDetailWithRentalSlipDetail END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("result", result);
            response.put("error", e.getMessage());
            log.info("API saveSurchargeDetailWithRentalSlipDetail END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
