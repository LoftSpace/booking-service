package com.example.demo.controller;

import com.example.demo.domain.RequestSeats;
import com.example.demo.domain.Reservation;
import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.dto.SeatSelectRequestDto;
import com.example.demo.service.BookingService;
import com.example.demo.service.MovieScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final MovieScheduleService movieScheduleService;

    @GetMapping("/screenings/{screeningId}/seats")
    public ResponseEntity<?> getSeatStatus(@PathVariable("screeningId") Integer screeningId) {
        try{
            return ResponseEntity.ok(bookingService.getSeatStatus(screeningId));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping("/seats/select")
    public ResponseEntity<?> selectSeat(@RequestBody SeatSelectRequestDto request){
        try{
            bookingService.selectSeat(request.getUserId(),new RequestSeats(request.getSeatIds()),request.getScreeningId());
            return ResponseEntity.ok().body("좌석 선택 완료");
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }

    }

    @PostMapping("/reservations")
    public ResponseEntity<?> reserve(@RequestBody ReservationRequestDto request) {
        try{
            // 유효성 검사 로직 중복 방지를 위해 클래스로 감싸서 보낸다. 또한 요청 좌석 객체는 충돌 검사 책임도 맡음.
            String reservationNumber = bookingService.reserveSeat(request.getUserId(), new RequestSeats(request.getSeatIds()), request.getScreeningId());
            return ResponseEntity.ok(
                    new ReservationResponseDto("예매 완료",reservationNumber)
            );
        } catch(Exception e){
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @GetMapping("/{movieId}/screenings")
    public ResponseEntity<?> getScreeningsByMovie(@PathVariable("movieId") Integer movieId){
        try{
            return ResponseEntity.ok(movieScheduleService.getMovieWithSchedules(movieId));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/reservations/{reservationId}")
    public ResponseEntity<?> getReservationInfo(@PathVariable("reservationId") String reservationId){
        try{
            return ResponseEntity.ok(bookingService.getReservationInfo(reservationId));
        } catch(Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }
}
