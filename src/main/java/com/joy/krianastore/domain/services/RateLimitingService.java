package com.joy.krianastore.domain.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimitingService {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Creates a bucket for a particular endpoint and user combo
     * @param endpoint the endpoint for which bucket is created
     */
    public Bucket createBucket(String endpoint) {
        log.info("Creating bucket for endpoint {}", endpoint);
        long capacity=getCapacity(endpoint);
        log.info("Found capacity {} for endpoint {}",capacity, endpoint);
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(capacity, Duration.ofMinutes(1)));
        log.info("Created bucket for endpoint {}", endpoint);
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * checks whether a request is allowed based on the bucket's token availability
     * @param endpoint is the request endpoint
     * @param userId is the user requesting the endpoint
     * @return true if request should be allowed
     * @throws IllegalArgumentException if not bucket is configured for that endpoint
     */
    public boolean allowRequest(String endpoint,String userId) {
        log.info("Checking if request for endpoint {} is allowed for user {}",endpoint,userId);
        String key=getKey(endpoint,userId);
        Bucket bucket=buckets.computeIfAbsent(key, k -> createBucket(endpoint) );
        if (bucket == null) {
            throw new IllegalArgumentException("No bucket configured for key: " + key);
        }
        return bucket.tryConsume(1);
    }

    /**
     * Returns bucket key combining the endpoint and user id
     * @param endpoint the endpoint to be rate limited
     * @param userId the user for which it has to be rate limited
     * @return the combined bucket key
     */
    private String getKey(String endpoint, String userId) {
        return endpoint + ":" + userId;
    }

    /**
     * Gives the maximum number of tokens that a bucket can hold, which is used to control how many requests can be served at once
     * @param endpoint the endpoint whose capacity is to be fetched
     * @return returns the endpoint capacity
     */
    private long getCapacity(String endpoint) {
        if(endpoint.equals("/api/transaction")) return 10;
        else if(endpoint.equals("/api/reports")) return 5;
        else return 1;
    }
}
