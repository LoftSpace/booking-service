package com.example.demo.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
    private Integer userId;
    private List<Integer> seatIds;
    private Integer screeningId;
}
