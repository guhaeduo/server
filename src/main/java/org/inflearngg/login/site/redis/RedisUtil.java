package org.inflearngg.login.site.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    // Redis에 데이터 저장
    public void setData(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
    // 데이터 조회
    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    // 데이터 삭제
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
    // 데이터 기한과 함께 저장
    public void setDataExpire(String key, String value, long time) {
        stringRedisTemplate.opsForValue().set(key, value, time, java.util.concurrent.TimeUnit.SECONDS); // 초단위로 계산
    }
}
