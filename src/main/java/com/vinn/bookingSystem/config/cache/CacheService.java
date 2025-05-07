package com.vinn.bookingSystem.config.cache;

public interface CacheService {
    Object getCachedValue(String cacheName, String key);
    void putCachedValue(String cacheName, String key, Object value);
    void evictCachedValue(String cacheName, String key);
}