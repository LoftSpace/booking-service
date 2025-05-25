package com.example.demo.service;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.Screening;
import com.example.demo.domain.Seat;
import com.example.demo.dto.SeatStatusResponseDto;
import com.example.demo.dto.SeatWithStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {
    @Autowired
    private final SeatService seatService;
    @Autowired
    private final ReservationService reservationService;
    @Autowired
    private final ScreeningService screeningService;

    public void reserve(String userEmail, List<Integer> requestSeatIds, Long screeningId) throws Exception {
        Screening screening = screeningService.getScreeningById(screeningId);
        Set<Integer> reservedSeatIds = reservationService.getReservedSeatIdByScreeningId(screeningId);

        assertSeatsNoConflict(requestSeatIds,reservedSeatIds);
        List<Reservation> reservations = buildReservations(requestSeatIds,screening,userEmail);
        reservationService.reserve(reservations);
    }

    private static List<Reservation> buildReservations(List<Integer> seatIds, Screening screening,String userEmail) {
        return seatIds.stream()
                .map(seatId -> Reservation.builder()
                        .seatId(seatId)
                        .userEmail(userEmail)
                        .screeningId(screening.getScreeningId())
                        .movieId(screening.getMovieId())
                        .build())
                        .toList();
    }

    private void assertSeatsNoConflict(List<Integer> seatIds, Set<Integer> reservedSeats) throws Exception {
        List<Integer> unavailableSeats = seatIds.stream()
                .filter(reservedSeats::contains)
                .toList();

        if(!unavailableSeats.isEmpty())
            throw new Exception(String.format("이미 예약 되어있는 좌석 : " + unavailableSeats));
    }

    // 유연한 변경을 더 중요시하여 조인 + 서브쿼리 대신 비즈니스 로직으로 SeatStatus를 조합한다.
    public SeatStatusResponseDto getSeatStatus(Long screeningId) {
        assertScreeningExists(screeningId);
        Set<Integer> reservedSeatIds = reservationService.getReservedSeatIdByScreeningId(screeningId);
        List<Seat> allSeats = seatService.findAllSeats();

        List<SeatWithStatusDto> allSeatsWithStatus = buildSeatsWithStatus(allSeats, reservedSeatIds);
        return new SeatStatusResponseDto(screeningId, allSeatsWithStatus);
    }

    private void assertScreeningExists(Long screeningId) {
        screeningService.getScreeningById(screeningId);
    }

    private static List<SeatWithStatusDto> buildSeatsWithStatus(List<Seat> allSeats, Set<Integer> reservedSeatIds) {
        List<SeatWithStatusDto> allSeatsWithStatus = allSeats.stream()
                .map(seat -> new SeatWithStatusDto(
                        seat.getSeatId(),
                        seat.getRow(),
                        seat.getCol(),
                        seat.getGrade(),
                        seat.getPrice(),
                        reservedSeatIds.contains(seat.getSeatId())
                ))
                .toList();
        return allSeatsWithStatus;
    }

}