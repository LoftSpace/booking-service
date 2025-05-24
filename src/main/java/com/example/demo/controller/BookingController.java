package com.example.demo.controller;

import com.example.demo.dto.ReservationReqiestDto;
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
    public ResponseEntity<?> getSeatStatus(@PathVariable("screeningId") Long screeningId) {
        try{
            return ResponseEntity.ok(bookingService.getSeatStatus(screeningId));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PostMapping("/reservations")
    public ResponseEntity<?> reserve(@RequestBody ReservationReqiestDto request) {
        try{
            bookingService.reserve(request.getUserEmail(),request.getSeatIds(),request.getScreeningId());
            return ResponseEntity.ok().body(String.format("예매 완료"));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{movieId}/screenings")
    public ResponseEntity<?> getScreeningsByMovie(@PathVariable("movieId") Long movieId){
        try{
            return ResponseEntity.ok(movieScheduleService.getMovieScheduleByMovieIdVer2(movieId));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
