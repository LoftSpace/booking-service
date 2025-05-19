package com.example.demo.service;

import com.example.demo.domain.Reservation;
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

    public Set<Integer> getReservedSeatIdByScreeningId(Long screeningId){
        screeningService.getScreeningById(screeningId);
        return reservationRepository.findReservedSeatIdByScreeningId(screeningId);
    }

    public List<Reservation> getUserReservation(String userEmail) {
        return reservationRepository.findByUserEmail(userEmail);
    }

    public List<Reservation> getAllReservationByScreeningId(Long screeningId){
        return reservationRepository.findAllByScreeningId(screeningId);
    }

    public void reserve(List<Reservation> reservations){
        reservationRepository.saveAll(reservations);
    }

    //public void Reserve(Long userId, Long screeningId, )
}
