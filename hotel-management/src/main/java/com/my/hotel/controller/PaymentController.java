package com.my.hotel.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.my.hotel.common.Routes;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_PAYMENT)
public class PaymentController {
    @Autowired
    private WebClient.Builder webClientBuilder;
    @GetMapping("/get-exchange-rate")
    private ResponseEntity<?> getExchangeRate() {
        String uri = "https://www.dongabank.com.vn/exchange/export";
        String exchangeRateResp = "";
        try {
            String result = webClientBuilder.build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            exchangeRateResp = result.replace("(", "").replace(")", "");
            return new ResponseEntity<>(exchangeRateResp, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
