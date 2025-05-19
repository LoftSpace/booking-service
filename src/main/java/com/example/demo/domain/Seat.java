package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "seat")
@RequiredArgsConstructor
public class Seat {
    @Id
    private Integer seatId;

    private Integer seatNum;
    private Integer row;
    private Integer col;


    @Enumerated(EnumType.STRING)
    private SeatGrade grade;

    public Integer getPrice() {
        return grade.getPrice();
    }
}
