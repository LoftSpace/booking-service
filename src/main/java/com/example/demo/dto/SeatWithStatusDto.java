package com.example.demo.dto;

import com.example.demo.domain.SeatGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class SeatWithStatusDto {
    private Integer seatNum;
    private Integer row;
    private Integer col;
    private SeatGrade grade;
    private Integer price;
    private boolean isReserved;
}
