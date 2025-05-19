package com.example.demo.repository;


import com.example.demo.domain.Seat;
import org.springframework.data.repository.Repository;


import java.util.List;


public interface SeatRepository extends Repository<Seat,Integer> {
    public abstract List<Seat> findAll();
    public abstract Seat findById(Integer seatId);
    public abstract void deleteById(Integer seatId);
}
