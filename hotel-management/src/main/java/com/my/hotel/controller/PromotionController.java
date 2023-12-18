package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.PromotionDetailDto;
import com.my.hotel.dto.PromotionDto;
import com.my.hotel.dto.RequestNewPromotionDto;
import com.my.hotel.service.PromotionService;
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
@RequestMapping(value = Routes.URI_REST_PROMOTION)
public class PromotionController {
    @Autowired
    private PromotionService promotionService;
    
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        log.info("API getAllPromotion");
        try {
            List<PromotionDto> promotionDtos = promotionService.getAllPromotions();
            log.info("API getAllPromotion END with OUTPUT response={}", promotionDtos);
            return new ResponseEntity<>(promotionDtos, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API getAllPromotion END with error:", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        PromotionDto promotion = this.promotionService.getById(id);
        return new ResponseEntity<>(promotion, HttpStatus.OK);
    }

    @PostMapping("/create-new")
    public ResponseEntity<?> createPromotion(@RequestBody RequestNewPromotionDto requestDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = promotionService.createNewPromotion(requestDto);
            response.put("result", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editPromotion(@RequestBody PromotionDto promotionDto) {
        log.info("API editPromotion STARTED with INPUT serviceDto={}", promotionDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = promotionService.save(promotionDto);
            response.put("result", result);
            log.info("API editPromotion END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API editPromotion END with an Error: ", e);
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }

    @PostMapping("/add-detail")
    public ResponseEntity<?> addPromotionDetail(@RequestBody PromotionDetailDto requestDto) {
        log.info("API addPromotionDetail STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = promotionService.addPromotionDetail(requestDto);
            response.put("result", result);
            log.info("API addPromotionDetail END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API addPromotionDetail END with OUTPUT error={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> deletePromotion(@PathVariable("id") String id) {
        log.info("API deletePromotion STARTED with INPUT id={}", id);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = promotionService.delete(id);
            response.put("result", result);
            log.info("API deletePromotion END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API deletePromotion END with an Error: ", e);
            return ResponseEntity.badRequest().body( e.getMessage());
        }
    }
}
