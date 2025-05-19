package com.example.demo.repository;

import com.example.demo.domain.Reservation;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReservationRepository extends Repository<Reservation,Long> {
    void save(Reservation reservation);

    Set<Integer> findReservedSeatIdByScreeningId(Long screeningId);
    List<Reservation> findAllReservationByScreeningId(Long screeningId);
    List<Reservation> findByUserId(Long userId);
    List<Reservation> findById(Long reservationId);
    List<Reservation> saveAll(List<Reservation> reservations);
}
