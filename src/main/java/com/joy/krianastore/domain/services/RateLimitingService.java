package com.joy.krianastore.domain.services;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @PostConstruct
    public void setupBuckets() {
        addBucket("/api/transactions", 10, Duration.ofMinutes(1)); // 10 requests per minute
        addBucket("/api/reports", 5, Duration.ofMinutes(1)); // 5 requests per minute
    }

    public void addBucket(String key, long capacity, Duration refillDuration) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.intervally(capacity, refillDuration));
        Bucket bucket = Bucket.builder().addLimit(limit).build();
        buckets.put(key, bucket);
    }

    /**
     * checks whether a request is allowed based on the bucket's token availability
     * @param key is the request endpoint
     * @return true if request should be denied
     * @throws IllegalArgumentException if not bucket is configured for that endpoint
     */
    public boolean allowRequest(String key) {
        Bucket bucket = buckets.get(key);
        if (bucket == null) {
            throw new IllegalArgumentException("No bucket configured for key: " + key);
        }
        return !bucket.tryConsume(1);
    }
}
