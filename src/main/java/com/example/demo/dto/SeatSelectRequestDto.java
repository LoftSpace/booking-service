package com.example.demo.dto;

import com.example.demo.domain.RequestSeatIds;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SeatSelectRequestDto {
    private Integer userId;
    private RequestSeatIds seatIds;
    private Integer screeningId;
}
