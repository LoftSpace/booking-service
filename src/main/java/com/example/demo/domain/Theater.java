package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

@Getter
@AllArgsConstructor
public class Theater {
    private Integer theaterId;
    private HashMap<SeatLocation,Seat> seatMap;
}
