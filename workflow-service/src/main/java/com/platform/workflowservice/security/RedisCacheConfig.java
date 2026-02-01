package com.platform.workflowservice.security;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory){

        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new StringRedisSerializer())
                        )
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(new GenericJackson2JsonRedisSerializer())
                        )
                        .disableCachingNullValues();

        RedisCacheConfiguration ticketCache =
                defaultConfig.entryTtl(Duration.ofMinutes(5));

        RedisCacheConfiguration pageCache =
                defaultConfig.entryTtl(Duration.ofMinutes(2));


        return RedisCacheManager.builder(factory)
                .withCacheConfiguration("tickets",ticketCache)
                .withCacheConfiguration("tickets-page",pageCache)
                .build();
    }
}
