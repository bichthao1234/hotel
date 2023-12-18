package com.my.hotel.controller;

import com.my.hotel.bl.ImportCustomerBL;
import com.my.hotel.common.Routes;
import com.my.hotel.dto.CustomerDto;
import com.my.hotel.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_CUSTOMER)
public class CustomerController {

    @Autowired
    private ImportCustomerBL importCustomerBL;

    @Autowired
    private CustomerService customerService;

    @Transactional
    @PostMapping("/import-customer/preview")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> previewFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam("sheetName") String sheetName) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            log.info("API previewFile STARTED with INPUT fileName={}", fileName);

            InputStream inputStream = file.getInputStream();
            List<CustomerDto> result = importCustomerBL.previewFile(inputStream, sheetName);

            Map<String, Object> response = new HashMap<>();
            response.put("result", result);

            log.info("API previewFile END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            throw new IOException("File is empty!");
        }
    }

    @PostMapping("/import-customer/save")
//    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<?> importCustomers(@RequestBody List<CustomerDto> customers) {
        log.info("API importCustomer STARTED with INPUT customers.size={}", customers.size());
        boolean result = importCustomerBL.importCustomer(customers);
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        log.info("API importCustomers END with OUTPUT response={}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") String id) {
        CustomerDto customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("/create")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customer) {
        log.info("API createCustomer STARTED with INPUT customer={}", customer);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = customerService.createCustomer(customer);
            response.put("result", result);
            log.info("API createCustomer END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API createCustomer END with OUTPUT error={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
