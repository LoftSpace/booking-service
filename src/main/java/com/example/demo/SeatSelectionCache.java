package com.example.demo;

import com.example.demo.dto.SeatLockInfo;

public interface SeatSelectionCache {
    SeatLockInfo lockSeat(Integer screeningId, Integer seatId,SeatLockInfo seatLockInfo);
    SeatLockInfo getLock(Integer screeningId, Integer seatId);
    void releaseSeat();
}
