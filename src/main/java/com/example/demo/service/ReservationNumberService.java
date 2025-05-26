package com.example.demo.service;

import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReservationNumberService {
    private static final String CHARACTERS = "01346789ABCDFGHJKMNPQRSTUVWXYZ";
    private static final int LENGTH = 5;

    public String generateReservationNumber(Integer userId, String reservationTime, Integer screeningId ) {
        String reservationNumber;
        UUID uuidV7 = UuidCreator.getTimeOrderedEpoch();
        return uuidV7.toString().substring(0,8);
    }
}
