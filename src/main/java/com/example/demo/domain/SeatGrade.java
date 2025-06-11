package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum SeatGrade {
    S_CLASS(1,20000),
    A_CLASS(2,18000),
    B_CLASS(3, 15000);

    private Integer gradeId;
    private Integer price;


    public static SeatGrade fromId(int id) {
        for (SeatGrade grade : values()) {
            if (grade.gradeId == id) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Invalid gradeId: " + id);
    }
}
