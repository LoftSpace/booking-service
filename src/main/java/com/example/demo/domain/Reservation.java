package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue
    private Long reservationId;

    private String userEmail;
    private Long movieId;
    private Long screeningId;
    private Integer seatId;

    @Builder
    public Reservation (String userEmail, Long movieId, Long screeningId, Integer seatId){
        this.userEmail = userEmail;
        this.movieId = movieId;
        this.screeningId = screeningId;
        this.seatId = seatId;
    }



}
