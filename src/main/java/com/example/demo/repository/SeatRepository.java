package com.example.demo.repository;


import com.example.demo.domain.Seat;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SeatRepository extends JpaRepository<Seat,Integer> {

}
