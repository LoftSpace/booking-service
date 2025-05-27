package com.example.demo.repository;

import com.example.demo.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT r.seatId FROM Reservation r WHERE r.screeningId = :screeningId")
    Set<Integer> findReservedSeatIdByScreeningId(@Param("screeningId") Integer screeningId);

    List<Reservation> findAllByScreeningId(Integer screeningId);
    List<Reservation> findByUserId(Integer userId);

}
