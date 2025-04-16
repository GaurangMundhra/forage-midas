package com.jpmc.midascore.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.jpmc.midascore.dto.Incentive;
import com.jpmc.midascore.dto.TransactionDto;

@Service
public class IncentiveService {

    private final RestTemplate restTemplate;

    @Value("${incentive.api-url}")
    private String incentiveApiUrl;

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Incentive getIncentive(TransactionDto transaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransactionDto> request = new HttpEntity<>(transaction, headers);

        try {
            ResponseEntity<Incentive> response = restTemplate.postForEntity(incentiveApiUrl, request, Incentive.class);
            return response.getBody() != null ? response.getBody() : new Incentive(0.0);
        } catch (RestClientException e) {
            System.err.println("Failed to fetch incentive: " + e.getMessage());
            return new Incentive(0.0); // ✅ Ensure Incentive class has a constructor accepting double
        }
    }
}
