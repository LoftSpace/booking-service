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

    private Integer rowId;
    private Integer colId;
    private Integer gradeId;


    public SeatGrade getSeatGrade() {
        return SeatGrade.fromId(this.gradeId);
    }

    public Integer getPrice() {
        return getSeatGrade().getPrice();
    }
}
