package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SeatStatusResponseDto {
    private Integer screeningId;
    private List<SeatWithStatusDto> availableSeatWithPrice;
}
