package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatLockInfo {
    private Integer userId;
    private long lockedAt;

}
