package com.example.demo.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
    private Integer userId;
    @NotEmpty(message = "좌석은 하나 이상 선택해야 합니다.")
    private List<Integer> seatIds;
    private Integer screeningId;
}
