package com.nvm.shoestoreapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nvm.shoestoreapi.dto.goship.RateRequest;
import com.nvm.shoestoreapi.dto.goship.RateResponseListWrapper;
import com.nvm.shoestoreapi.dto.goship.WardResponseListWrapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.nvm.shoestoreapi.util.Constant.GO_SHIP_TOKEN;

@RestController
@CrossOrigin("http://localhost:4200")
@AllArgsConstructor
@RequestMapping("/api/go-ship")
public class GoShipController {
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/rates")
    public ResponseEntity<?> getRates(@RequestBody RateRequest rateRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", GO_SHIP_TOKEN);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<RateRequest> entity = new HttpEntity<>(rateRequest, headers);

            String apiUrl = "http://sandbox.goship.io/api/v2/rates";
            RateResponseListWrapper responseWrapper = restTemplate.postForObject(apiUrl, entity, RateResponseListWrapper.class);

            return ResponseEntity.ok(Objects.requireNonNull(responseWrapper).getData());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getResponseBodyAsString());
        }
    }

    @GetMapping("/wards")
    public ResponseEntity<?> getWards(@RequestParam String districtId) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", GO_SHIP_TOKEN);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String apiUrl = "http://sandbox.goship.io/api/v2/districts/" + districtId + "/wards";

            ResponseEntity<WardResponseListWrapper> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    entity,
                    WardResponseListWrapper.class
            );

            return ResponseEntity.ok(Objects.requireNonNull(responseEntity.getBody()).getData());
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
