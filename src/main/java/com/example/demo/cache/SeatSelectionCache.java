package com.example.demo.cache;

import com.example.demo.dto.SeatLockInfo;

import java.util.Set;

public interface SeatSelectionCache {
    void lockSeat(Integer screeningId, Integer seatId,SeatLockInfo seatLockInfo);
    SeatLockInfo getLock(Integer screeningId, Integer seatId);
    void releaseSeat();
    public Set<Integer> getAllLockByScreeningId(Integer screeningId);
}
