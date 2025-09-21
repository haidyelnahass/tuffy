package com.eg.tracking.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class RedisCacheManager {

  private final RedisTemplate<String, Object> redisTemplate;
  private final RedisTemplate<String, String> stringRedisTemplate;

  public void saveGeoLocationToCache(String key, Point value, String id) {
    redisTemplate.opsForGeo().add(key, value, id);
  }

  public Point getGeoLocationFromCache(String key, String id) {
    List<Point> points = redisTemplate.opsForGeo().position(key, id);
    return points != null ? points.get(0) : null;
  }

  public GeoResults<RedisGeoCommands.GeoLocation<Object>> getNearbyLocationsFromCache(
    String key, double lon, double lat, int radius, int limit) {
    return redisTemplate.opsForGeo().radius(
      key, new Circle(new Point(lon, lat), new Distance(radius, Metrics.KILOMETERS)),
      RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
        .includeCoordinates()
        .includeDistance()
        .limit(limit)
    );
  }

  public void saveMetaDataToCache(String key, Map<String, String> hashMap) {
    stringRedisTemplate.opsForHash().putAll(key, hashMap);
  }

  public Map<String, String> getMetaDataFromCache(String key) {
    HashOperations<String, String, String> ops = stringRedisTemplate.opsForHash();
    return ops.entries(key);
  }

  public Set<String> getKeys(String pattern) {
    return stringRedisTemplate.keys(pattern);
  }

  public void removeGeoLocation(String key, String member) {
    redisTemplate.opsForGeo().remove(key, member);
  }

  public void deleteFromCache(String key) {
    stringRedisTemplate.delete(key);
  }

}
