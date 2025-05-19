package com.example.demo.repository;

import com.example.demo.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Set<Integer> findReservedSeatIdByScreeningId(Long screeningId);
    List<Reservation> findAllByScreeningId(Long screeningId);
    List<Reservation> findByUserEmail(String userEmail);
}
