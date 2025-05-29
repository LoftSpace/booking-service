package com.example.demo.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationInfo {
    private Long reservationId;
    private String reservationNumber;
    private String reservedDate;

    private Integer screeningId;
    private String startTime;

    private Integer movieId;
    private String movieName;

    private Integer seatId;


    public ReservationInfo(Long reservationId, String reservationNumber, String reservedDate,
                              Integer screeningId, String startTime,
                              Integer movieId, String movieName,
                              Integer seatId) {
        this.reservationId = reservationId;
        this.reservationNumber = reservationNumber;
        this.reservedDate = reservedDate;
        this.screeningId = screeningId;
        this.startTime = startTime;
        this.movieId = movieId;
        this.movieName = movieName;
        this.seatId = seatId;

    }



}
