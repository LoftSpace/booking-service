package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SeatSelectRequestDto {
    private Integer userId;
    private Integer seatId;
    private Integer screeningId;
}
