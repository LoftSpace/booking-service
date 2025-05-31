package com.example.demo;

import com.example.demo.dto.SeatLockInfo;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaffeineSeatSelectionCache implements SeatSelectionCache{

    private final Cache<String, SeatLockInfo> cache;

    @Override
    public SeatLockInfo lockSeat(Integer screeningId, Integer seatId,SeatLockInfo seatLockInfo) {
        String key = screeningId + "-" + seatId;
        SeatLockInfo newLock = new SeatLockInfo(seatLockInfo.getUserId(), System.currentTimeMillis());
        SeatLockInfo existing = cache.asMap().putIfAbsent(key, newLock);
        if(existing != null)
            throw new IllegalStateException("이미 선택된 좌석입니다. 락 획득 실패");
        return existing;
    }

    @Override
    public SeatLockInfo getLock(Integer screeningId, Integer seatId) {
        return cache.getIfPresent(screeningId + "-" + seatId);
    }

    @Override
    public void releaseSeat() {

    }
}
