package com.joy.krianastore.core;

import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConversionClient {

    private final String BASE_URL = "https://api.fxratesapi.com/latest";

    @Cacheable(value = "currencyRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal getConversionRate(String fromCurrency, String toCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?base=" + fromCurrency + "&symbols=" + toCurrency;
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);
        if (response == null) {
            throw new RuntimeException("Could not get currency rate from " + fromCurrency + " to " + toCurrency);
        }
        return response.getRates().get(toCurrency);
    }

    @Data
    private static class CurrencyResponse {
        private String base;
        private Map<String, BigDecimal> rates;
    }
}
