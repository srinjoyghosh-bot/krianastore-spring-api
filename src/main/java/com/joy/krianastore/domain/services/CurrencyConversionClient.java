package com.joy.krianastore.domain.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConversionClient {

    private final String BASE_URL = "https://api.fxratesapi.com/latest";

    public BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?base=" + fromCurrency + "&symbols=" + toCurrency;
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);
        if (response == null) {
            //TODO throw exception
            return null;
        }
        BigDecimal rate = response.getRates().get(toCurrency);
        return amount.multiply(rate);
    }

    @Data
    private static class CurrencyResponse {
        private String base;
        private Map<String, BigDecimal> rates;
    }
}
