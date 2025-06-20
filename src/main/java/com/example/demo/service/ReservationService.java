package com.example.demo.service;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationInfo;
import com.example.demo.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Set<Integer> getReservedSeatIdByScreeningId(Integer screeningId){
        return reservationRepository.findReservedSeatIdByScreeningId(screeningId);
    }

    public List<ReservationInfo> getUserReservation(Integer userId) {
        return reservationRepository.findReservationInfoByUserId(userId);
    }

    public List<Reservation> getAllReservationByScreeningId(Integer screeningId){
        return reservationRepository.findAllByScreeningId(screeningId);
    }

    public List<Reservation> saveReservations(List<Reservation> reservations){
        return reservationRepository.saveAll(reservations);
    }

    public ReservationInfo getReservationInfoByReservationId(String reservationId){
        return reservationRepository.findReservationInfoByReservationNumber(reservationId)
                .orElseThrow(() ->  new EntityNotFoundException("유효하지 않은 예매번호"));
    }
}
