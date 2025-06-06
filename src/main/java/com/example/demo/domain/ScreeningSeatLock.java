package com.example.demo.domain;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class ScreeningSeatLock {
    private ConcurrentHashMap<Integer, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public void withLock(Integer screeningId, Runnable action){
        ReentrantLock lock = lockMap.computeIfAbsent(screeningId, id -> new ReentrantLock());
        lock.lock();
        try{
            action.run();
        } finally {
            lock.unlock();
        }
    }
}
