package com.eg.ride.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisCacheManager {

  private final RedisTemplate<String, Object> redisTemplate;

  public void saveToCache(String key, Object value) {
    redisTemplate.opsForValue().set(key, value, Duration.ofMinutes(10));
  }

  public Object getFromCache(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void deleteFromCache(String key) {
    redisTemplate.delete(key);
  }
}
