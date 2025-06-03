package com.example.demo.factory;

import com.example.demo.domain.RequestSeatIds;
import com.example.demo.domain.Reservation;
import com.example.demo.domain.Screening;
import com.example.demo.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationFactory {
    private final ReservationNumberGenerator reservationNumberGenerator;
    private final ScreeningService screeningService;

    public List<Reservation> buildReservations(Integer userId, RequestSeatIds requestSeatIds, Integer screeningId) {
        String reservedTime = getCurrentTime();
        String reservationNumber = reservationNumberGenerator.generateReservationNumber(userId,reservedTime,screeningId);

        Screening screening = screeningService.getScreeningById(screeningId);
        // 이 부분은 Reservation의 책임이지 않는가?
        return requestSeatIds.getIds()
                .stream()
                .map(seatId -> Reservation.builder()
                        .reservationNumber(reservationNumber)
                        .userId(userId)
                        .reservedDate(reservedTime)
                        .seatId(seatId)
                        .movieId(screening.getMovieId())
                        .screeningId(screening.getScreeningId())
                        .build())
                .toList();
    }

    private static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }
}
