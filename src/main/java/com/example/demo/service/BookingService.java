package com.example.demo.service;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.Screening;
import com.example.demo.domain.Seat;
import com.example.demo.dto.SeatStatusResponseDto;
import com.example.demo.dto.SeatWithStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatService seatService;
    private final ReservationService reservationService;
    private final ScreeningService screeningService;
    private final ReservationNumberService reservationNumberService;

    public void reserve(Integer userId, List<Integer> requestSeatIds, Integer screeningId) throws Exception {
        Screening screening = screeningService.getScreeningById(screeningId);
        Set<Integer> reservedSeatIds = reservationService.getReservedSeatIdByScreeningId(screeningId);

        assertSeatsNoConflict(requestSeatIds,reservedSeatIds);

        List<Reservation> reservations = buildReservations(userId,requestSeatIds,screening);
        reservationService.saveReservations(reservations);
    }

    private void assertSeatsNoConflict(List<Integer> seatIds, Set<Integer> reservedSeats) throws Exception {
        List<Integer> unavailableSeats = seatIds.stream()
                .filter(reservedSeats::contains)
                .toList();

        if(!unavailableSeats.isEmpty())
            throw new Exception(String.format("이미 예약 되어있는 좌석 : " + unavailableSeats));
    }

    private List<Reservation> buildReservations(Integer userId,List<Integer> seatIds, Screening screening) {
        String reservedTime = getCurrentTime();
        String reservationNumber = reservationNumberService.generateReservationNumber(userId,reservedTime,screening.getScreeningId());

        return seatIds.stream()
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


    // 유연한 변경을 더 중요시하여 조인 + 서브쿼리 대신 비즈니스 로직으로 SeatStatus를 조합한다.
    public SeatStatusResponseDto getSeatStatus(Integer screeningId) {
        assertScreeningExists(screeningId);

        Set<Integer> reservedSeatIds = reservationService.getReservedSeatIdByScreeningId(screeningId);
        List<Seat> allSeats = seatService.findAllSeats();

        List<SeatWithStatusDto> allSeatsWithStatus = buildSeatsWithStatus(allSeats, reservedSeatIds);
        return new SeatStatusResponseDto(screeningId, allSeatsWithStatus);
    }

    private void assertScreeningExists(Integer screeningId) {
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