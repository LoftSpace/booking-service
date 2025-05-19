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
        assertSeatsNoConflict(requestSeatIds,screeningId);
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

    private void assertSeatsNoConflict(List<Integer> seatIds, Long screeningId) throws Exception {
        Set<Integer> reservedSeats = reservationService.getReservedSeatIdByScreeningId(screeningId);
        List<Integer> unavailableSeats = seatIds.stream()
                .filter(reservedSeats::contains)
                .toList();

        if(!unavailableSeats.isEmpty())
            throw new Exception(String.format("이미 예약 되어있는 좌석 : " + unavailableSeats));
    }

    // 역할 : 좌석현황 응답 dto 조합
    // 고민 : dto에서 한번에 가져올지 아니면 아래 처럼 비즈니스 로직에서 조립하는 방식으로 할지?
    // 조립 : 모든 좌석 조회 + 예약좌석 조회 => stream()으로 새로운 dto에 담기
    // dto : 좌석 테이블 join 예약 테이블 => dto에 담기
    public SeatStatusResponseDto getSeatStatus(Long screeningId) {
        //책임 : 모든 좌석을 가져온다. 예약 좌석을 가져온다. 각 좌석에 상태를 부여한다. 응답 dto를 반환한다.
        screeningService.assertScreeningExists(screeningId);

        // screening이 존재하는지를 getReservedSeatsIdByScreeingId에서 확인하면 안되는가? -> getReservedSeatsIdByScreeningId에서 예외를 던진다면,
        // screening 자체가 없는건지 아니면 screening에 대한 예매가 아직 없는건지 불분명확함. 같은 종류의 예외가 발생할 수 있는 곳이 두곳임.
        // 하지만 이는 예외 분기처리로 가능하다.
        // 또한 ReservationService에서 불필요한 외부 도메인(ScreeningService)에 의존하게 됨.
        // ReservationService에서
        // 재사용성측면에서 어짜피 getReservedSeatIdByScreeningId()를 위해서는 항상 screeningId 존재여부 확인해야함.
        // 현재 개발 초기이기 때문에 미래에 변경 여부가 엄청 많다. 변경에 대한 대비를 위해 불필요한 의존을 줄이는 것이더 효과적이라고 판단.
        // 또한 현재까지는 getReservedSeatIdByScreeningId()를 재사용될 경우가 별로 없다고 생각했다.
        // 따라서 불필요한 의존으로 변경시 영향력이 전파되는 것보다 재사용마다 코드를 작성하는게 더 안전하다고 판단.
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