package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Screening {
    @Id
    @GeneratedValue
    private Long screeningId;

    private Long movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


}
