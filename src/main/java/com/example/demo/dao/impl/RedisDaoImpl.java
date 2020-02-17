package com.example.demo.dao.impl;

import com.example.demo.dao.RedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisDaoImpl implements RedisDao {

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Override
    public String getOneStringByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setOneStringByKey(String key, String value, int timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    @Override
    public Set<String> getSetByKeyAndScore(String key, int beginScore, int endScore) {
        redisTemplate.opsForZSet().removeRangeByScore(key, 1, beginScore - 1);
        return redisTemplate.opsForZSet().rangeByScore(key, beginScore, endScore);
    }

    @Override
    public void addOneStringToZSet(String key, String newVal, String oldVal, double score) {
        if (oldVal != null)
            redisTemplate.opsForZSet().remove(key, oldVal);
        redisTemplate.opsForZSet().add(key, newVal, score);
    }
}
