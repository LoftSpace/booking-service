package com.example.demo.service;

import com.example.demo.domain.Seat;
import com.example.demo.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    @Autowired
    private final SeatRepository seatRepository;

    public Seat findSeatById(Integer seatId){
        return seatRepository.findById(seatId).get();
    }

    public void deleteSeatById(Integer seatId){
        seatRepository.deleteById(seatId);
    }

    public List<Seat> findAllSeats(){
        return  seatRepository.findAll();
    }

}
