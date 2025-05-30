package com.example.demo.controller;

import com.example.demo.domain.RequestSeatIds;
import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.SeatSelectRequestDto;
import com.example.demo.service.BookingService;
import com.example.demo.service.MovieScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            bookingService.selectSeat(request.getUserId(),new RequestSeatIds(request.getSeatIds()),request.getScreeningId());
            return ResponseEntity.ok().body("좌석 선택 완료");
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }

    }
    @PostMapping("/reservations")
    public ResponseEntity<?> reserve(@RequestBody ReservationRequestDto request) {
        try{
            bookingService.reserve(request.getUserId(),new RequestSeatIds(request.getSeatIds()),request.getScreeningId());
            return ResponseEntity.ok().body(String.format("예매 완료"));
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



}
