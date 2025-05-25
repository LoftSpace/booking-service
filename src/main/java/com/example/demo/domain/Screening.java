package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Screening {
    @Id
    @GeneratedValue
    private Long screeningId;

    @Column(name = "movieId")
    private Long movieId;
    @Column(name = "startTime")
    private String startTime;
    @Column(name = "endTime")
    private String endTime;



}
