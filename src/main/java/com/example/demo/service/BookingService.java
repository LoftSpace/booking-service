package com.example.demo.service;

import com.example.demo.SeatSelectionCache;
import com.example.demo.domain.RequestSeatIds;
import com.example.demo.domain.Reservation;
import com.example.demo.domain.Seat;
import com.example.demo.dto.SeatLockInfo;
import com.example.demo.dto.SeatStatusResponseDto;
import com.example.demo.dto.SeatWithStatusDto;
import com.example.demo.factory.ReservationFactory;
import com.example.demo.factory.ReservationNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final SeatService seatService;
    private final ReservationService reservationService;
    private final ScreeningService screeningService;
    private final SeatSelectionCache seatSelectionCache;
    private final ReservationFactory reservationFactory;

    private ConcurrentHashMap<Integer,ReentrantLock> screeningLock = new ConcurrentHashMap<>();

    @Transactional
    public void selectSeat(Integer userId, RequestSeatIds requestSeatIds, Integer screeningId) throws Exception {
        ReentrantLock lock = screeningLock.computeIfAbsent(screeningId, id -> new ReentrantLock());
        lock.lock();
        try {
            assertSeatsAreAvailable(requestSeatIds, screeningId, userId);

            SeatLockInfo seatLockInfo = new SeatLockInfo(userId, System.currentTimeMillis());
            selectSeats(requestSeatIds, screeningId, seatLockInfo);
        } finally {
            lock.unlock();
        }
    }

    private void selectSeats(RequestSeatIds requestSeatIds, Integer screeningId, SeatLockInfo seatLockInfo) {
        for(Integer seatId : requestSeatIds.getIds()){
            seatSelectionCache.lockSeat(screeningId,seatId, seatLockInfo);
        }
    }

    @Transactional
    public void reserveSeat(Integer userId, RequestSeatIds requestSeatIds, Integer screeningId) throws Exception {
        assertSeatsAreAvailable(requestSeatIds,screeningId,userId);

        List<Reservation> reservations = reservationFactory.buildReservations(userId, requestSeatIds, screeningId);
        reservationService.saveReservations(reservations);
    }

    private void assertSeatsAreAvailable(RequestSeatIds requestSeatIds,Integer screeningId,Integer userId) throws Exception {
        assertSeatsNotSelectedByOthers(requestSeatIds,screeningId,userId);
        assertSeatsNotReserved(requestSeatIds, screeningId);
        // 추후 좌석 유효 조건 추가 가능
    }

    private void assertSeatsNotSelectedByOthers(RequestSeatIds requestSeatIds, Integer screeningId, Integer userId) {
        for(Integer seatId : requestSeatIds.getIds()){
            SeatLockInfo lock = seatSelectionCache.getLock(screeningId, seatId);
            if(isSelectedByOthers(userId, lock)) {
                throw new IllegalStateException(String.format("이미 선택된 좌석이 있습니다" + seatId));
            }

        }
    }

    private static boolean isSelectedByOthers(Integer userId, SeatLockInfo lock) {
        if(lock != null && !lock.getUserId().equals(userId))
            return true;
        else
            return false;
    }

    private void assertSeatsNotReserved(RequestSeatIds requestSeatIds, Integer screeningId) throws Exception {
        Set<Integer> reservedSeatIds = reservationService.getReservedSeatIdByScreeningId(screeningId);
        if(reservedSeatIds.isEmpty()) return;

        List<Integer> conflictSeats = requestSeatIds.getConflicts(reservedSeatIds);

        if(!conflictSeats.isEmpty())
            throw new Exception(String.format("이미 예약 되어있는 좌석 : " + conflictSeats));
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