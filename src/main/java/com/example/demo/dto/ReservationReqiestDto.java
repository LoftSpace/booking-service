package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationReqiestDto {
    private String userEmail;
    private List<Integer> seatIds;
    private Long screeningId;
}
