package com.joy.krianastore.core;

import com.joy.krianastore.core.exception.CurrencyConversionException;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Client service responsible for fetching and converting currency exchange rates.
 * This class uses a {@link RestTemplate} to consume an external currency conversion API.
 */
@Service
public class CurrencyConversionClient {

    private final String BASE_URL = "https://api.fxratesapi.com/latest";

    /**
     * Converts an amount from one currency to another using current exchange rates.
     * Caches the result to improve performance and reduce external API calls.
     *
     * @param fromCurrency the currency code to convert from (e.g., "USD")
     * @param toCurrency   the currency code to convert to (e.g., "EUR")
     * @return the converted amount in the destination currency
     * @throws CurrencyConversionException if the conversion fails or currencies are not supported
     */
    @Cacheable(value = "currencyRates", key = "#fromCurrency + '-' + #toCurrency")
    public BigDecimal getConversionRate(String fromCurrency, String toCurrency) {
        RestTemplate restTemplate = new RestTemplate();
        String url = BASE_URL + "?base=" + fromCurrency + "&symbols=" + toCurrency;
        CurrencyResponse response = restTemplate.getForObject(url, CurrencyResponse.class);
        if (response == null || !response.getRates().containsKey(toCurrency)) {
            throw new CurrencyConversionException("Could not get currency rate from " + fromCurrency + " to " + toCurrency);
        }
        return response.getRates().get(toCurrency);
    }
    /**
     * Internal class representing the response from the currency conversion API.
     */
    @Data
    private static class CurrencyResponse {
        private String base;
        private Map<String, BigDecimal> rates;
    }
}
