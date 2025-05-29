package com.example.demo.service;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationInfo;
import com.example.demo.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService {
    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final ScreeningService screeningService;

    public Set<Integer> getReservedSeatIdByScreeningId(Integer screeningId){
        return reservationRepository.findReservedSeatIdByScreeningId(screeningId);
    }

    public List<ReservationInfo> getUserReservation(Integer userId) {
        return reservationRepository.findReservationInfoByUserId(userId);
    }

    public List<Reservation> getAllReservationByScreeningId(Integer screeningId){
        return reservationRepository.findAllByScreeningId(screeningId);
    }

    public void saveReservations(List<Reservation> reservations){
        reservationRepository.saveAll(reservations);
    }

}
