package com.my.hotel.controller;

import com.my.hotel.bl.GetInvoiceBL;
import com.my.hotel.common.Routes;
import com.my.hotel.dto.request.CreateInvoiceRequestDto;
import com.my.hotel.dto.response.InvoiceResponseDto;
import com.my.hotel.service.InvoiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_INVOICE)
public class InvoiceController {
    @Autowired
    private GetInvoiceBL getInvoiceBL;
    @Autowired
    private InvoiceService invoiceService;
    
    @GetMapping("/export/{invoiceId}")
    public ResponseEntity<?> getInvoiceByRentalDetailId(@PathVariable Integer invoiceId) {
        log.info("API getInvoiceByRentalDetailId STARTED with INPUT invoiceId={}", invoiceId);
        Map<String, Object> response = new HashMap<>();
        try {
            InvoiceResponseDto result = getInvoiceBL.getInvoiceByRentalDetailId(invoiceId);
            log.info("API getInvoiceByRentalDetailId END with OUTPUT result={}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getInvoiceByRentalDetailId END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        log.info("API getAll STARTED");
        Map<String, Object> response = new HashMap<>();
        try {
            List<InvoiceResponseDto> result = getInvoiceBL.getAll();
            log.info("API getAll END");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API getAll END with Error ={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/export/create")
    public ResponseEntity<?> createInvoiceByRentalSlipDetail(@RequestBody CreateInvoiceRequestDto requestDto) throws Throwable {
        log.info("API createInvoiceByRentalSlipDetail STARTED with INPUT invoiceId={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            Integer result = invoiceService.createInvoiceByRentalSlipDetail(requestDto);
            log.info("API createInvoiceByRentalSlipDetail END with OUTPUT result={}", result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            log.info("API createInvoiceByRentalSlipDetail END with OUTPUT response={}", response);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
