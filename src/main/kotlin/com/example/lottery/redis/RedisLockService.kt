package com.example.lottery.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisLockService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    fun tryLock(key: String, timeout: Long): Boolean {
        val value = "locked"
        val result = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS)
        return result ?: false
    }

    fun releaseLock(key: String) {
        redisTemplate.delete(key)
    }
}