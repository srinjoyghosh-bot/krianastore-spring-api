package com.joy.krianastore.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
/**
 * Configuration class for setting up Redis connections and templates.
 * This class defines beans required for interacting with a Redis database.
 * This configures the cache settings for Redis, including TTL and serialization options.
 */
@Configuration
@EnableCaching
@EnableRedisRepositories
public class RedisConfig {
    /**
     * Configures and returns a RedisTemplate for interacting with Redis data.
     *
     * @param connectionFactory the RedisConnectionFactory to create Redis connections
     * @return a configured RedisTemplate instance
     */
    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
    /**
     * Provides a default cache configuration for managing cache settings in Redis.
     * This configuration specifies the time-to-live (TTL) for cache entries, key and value serialization formats,
     * and the handling of null values.
     *
     * @return a RedisCacheConfiguration object with customized settings:
     *         - Entries expire after 10 minutes.
     *         - Keys are serialized using {@link StringRedisSerializer}.
     *         - Values are serialized using {@link GenericJackson2JsonRedisSerializer}.
     *         - Null value caching is disabled.
     */
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
    }
}
