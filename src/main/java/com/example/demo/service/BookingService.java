package com.example.demo.service;

import com.example.demo.SeatSelectionCache;
import com.example.demo.domain.RequestSeatIds;
import com.example.demo.domain.Reservation;
import com.example.demo.domain.Screening;
import com.example.demo.domain.Seat;
import com.example.demo.dto.SeatLockInfo;
import com.example.demo.dto.SeatStatusResponseDto;
import com.example.demo.dto.SeatWithStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final SeatSelectionCache seatSelectionCache;

    @Transactional
    public void selectSeat(Integer userId, Integer seatId, Integer screeningId){
        SeatLockInfo lock = seatSelectionCache.getLock(screeningId, seatId);
        if(lock != null)
            throw new IllegalStateException("이미 선택된 좌석입니다. 다른 좌석을 선택해주세요.");

        SeatLockInfo seatLockInfo = new SeatLockInfo(userId, System.currentTimeMillis());
        seatSelectionCache.lockSeat(screeningId,seatId,seatLockInfo);
    }

    @Transactional
    public void reserve(Integer userId, RequestSeatIds requestSeatIds, Integer screeningId) throws Exception {
        assertSeatsAreAvailable(requestSeatIds,screeningId);

        List<Reservation> reservations = buildReservations(userId,requestSeatIds,screeningId);
        reservationService.saveReservations(reservations);
    }

    private void assertSeatsAreAvailable(RequestSeatIds requestSeatIds,Integer screeningId) throws Exception {

        assertSeatsNoConflict(requestSeatIds, screeningId);
        // 추후 좌석 유효 조건 추가 가능
    }

    private void assertSeatsNoConflict(RequestSeatIds requestSeatIds, Integer screeningId) throws Exception {
        Set<Integer> reservedSeatIds = reservationService.getReservedSeatIdByScreeningId(screeningId);
        if(reservedSeatIds.isEmpty()) return;

        List<Integer> conflictSeats = requestSeatIds.getConflicts(reservedSeatIds);

        if(!conflictSeats.isEmpty())
            throw new Exception(String.format("이미 예약 되어있는 좌석 : " + conflictSeats));
    }

    private List<Reservation> buildReservations(Integer userId,RequestSeatIds requestSeatIds, Integer screeningId) {
        String reservedTime = getCurrentTime();
        String reservationNumber = reservationNumberService.generateReservationNumber(userId,reservedTime,screeningId);

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