package com.example.demo;

import com.example.demo.dto.SeatLockInfo;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CaffeineSeatSelectionCache implements SeatSelectionCache{

    @Override
    @CachePut(value = "seatLocks", key = "#screeningId + '-' + #seatId")
    public SeatLockInfo lockSeat(Integer screeningId, Integer seatId,SeatLockInfo seatLockInfo) {
        return seatLockInfo;
    }
    @Override
    @Cacheable(value = "seatLocks", key = "#screeningId + '-' + #seatId")
    public SeatLockInfo getLock(Integer screeningId, Integer seatId) {
        return null;
    }

    @Override
    public void releaseSeat() {

    }
}
