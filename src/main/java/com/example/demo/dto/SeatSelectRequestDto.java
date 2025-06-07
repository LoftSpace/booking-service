package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SeatSelectRequestDto {
    private Integer userId;
    private List<Integer> seatIds;
    private Integer screeningId;
}
