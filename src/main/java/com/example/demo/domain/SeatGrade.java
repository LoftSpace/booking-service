package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum SeatGrade {
    S_CLASS(20000),
    A_CLASS(18000),
    B_CLASS(15000);

    private Integer price;

}
