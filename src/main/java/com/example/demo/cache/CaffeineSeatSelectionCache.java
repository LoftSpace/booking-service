package com.example.demo.cache;

import com.example.demo.dto.SeatLockInfo;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
@RequiredArgsConstructor
public class CaffeineSeatSelectionCache implements SeatSelectionCache {

    private final Cache<String, SeatLockInfo> cache;

    @Override
    public void lockSeat(Integer screeningId, Integer seatId,SeatLockInfo seatLockInfo) {
        String key = screeningId + "-" + seatId;
        SeatLockInfo newLock = new SeatLockInfo(seatLockInfo.getUserId(), System.currentTimeMillis());

        if(isSeatLockExists(key, newLock))
            throw new IllegalStateException("이미 선택된 좌석입니다.");

    }

    private boolean isSeatLockExists(String key, SeatLockInfo newLock) {
        return cache.asMap().putIfAbsent(key, newLock) != null;
    }

    @Override
    public SeatLockInfo getLock(Integer screeningId, Integer seatId) {
        return cache.getIfPresent(screeningId + "-" + seatId);
    }

    @Override
    public void releaseSeat() {

    }
    @Override
    public Set<Integer> getAllLockByScreeningId(Integer screeningId){
        String prefix = screeningId + "-";
        return cache.asMap().keySet().stream()
                .filter(key -> key.startsWith(prefix))
                .map(key -> {
                    String[] split = key.split("-");
                    return Integer.parseInt(split[1]);
                })
                .collect(Collectors.toSet());
    }
}
