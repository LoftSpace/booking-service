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
    public void lockSeat(Integer screeningId, Integer seatId,SeatLockInfo seatLockInfo) {
        String key = screeningId + "-" + seatId;
        SeatLockInfo newLock = new SeatLockInfo(seatLockInfo.getUserId(), System.currentTimeMillis());

        if(isSeatLockExists(key, newLock))
            throw new IllegalStateException("이미 선택된 좌석입니다.");

    }

    private boolean isSeatLockExists(String key, SeatLockInfo newLock) {
        if(cache.asMap().putIfAbsent(key, newLock) == null)
            return false;
        return true;
    }

    @Override
    public SeatLockInfo getLock(Integer screeningId, Integer seatId) {
        return cache.getIfPresent(screeningId + "-" + seatId);
    }

    @Override
    public void releaseSeat() {

    }
}
