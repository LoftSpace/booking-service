package com.example.demo.controller;

import com.example.demo.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final ReservationService reservationService;

    @GetMapping("/{userId}/reservations")
    public ResponseEntity<?> getReservationsByUserId(@PathVariable("userId") Integer userId){
        try {
            return ResponseEntity.ok().body(reservationService.getUserReservation(userId));
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

}
