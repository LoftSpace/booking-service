package com.example.demo.service;

import com.example.demo.dto.SeatLockInfo;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class SeatLockManager {
    private final ConcurrentHashMap<String, SeatLockInfo> seatLockMap = new ConcurrentHashMap<>();

    public void getLock(String key){

    }
}
