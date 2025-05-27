package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "reservation")
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "reservation_number")
    private String reservationNumber;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "movie_id")
    private Integer movieId;
    @Column(name = "screening_id")
    private Integer screeningId;
    @Column(name = "reserved_date")
    private String reservedDate;
    @Column(name = "seat_id")
    private Integer seatId;

    @Builder
    public Reservation (String reservationNumber,Integer userId, Integer movieId, Integer screeningId,String reservedDate,Integer seatId){
        this.reservationNumber = reservationNumber;
        this.userId = userId;
        this.movieId = movieId;
        this.screeningId = screeningId;
        this.reservedDate = reservedDate;
        this.seatId = seatId;
    }

}
