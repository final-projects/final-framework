package com.ilikly.finalframework.redis;

import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author likly
 * @version 1.0
 * @date 2018-11-22 13:52:35
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public interface Redis {

    static RedisOperations key() {
        return RedisRegistry.getInstance().key();
    }

    static ValueOperations value() {
        return RedisRegistry.getInstance().value();
    }

    static HashOperations hash() {
        return RedisRegistry.getInstance().hash();
    }

    static ListOperations list() {
        return RedisRegistry.getInstance().list();
    }

    static SetOperations set() {
        return RedisRegistry.getInstance().set();
    }

    static ZSetOperations zset() {
        return RedisRegistry.getInstance().zset();
    }

    static boolean lock(Object key, Object value, long timeout, TimeUnit unit) {
        return Boolean.TRUE.equals(value().setIfAbsent(key, value, timeout, unit));
    }

    static boolean unlock(Object key, Object value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return Boolean.TRUE.equals(key().execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(key), value));
    }

}
