package com.example.demo.reservation;

import com.example.demo.domain.RequestSeats;
import com.example.demo.domain.ReservationInfo;
import com.example.demo.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ReservationTest {
    @Autowired
    BookingService bookingService;

    private List<Integer> requestSeatIds = new ArrayList<>();
    private Integer userId;
    private Integer screeningId;
    @Test
    public void 예매_저장_후_아이디로_반환_테스트(){
        //given : 예매
        userId = 1;
        requestSeatIds.add(1);
        screeningId = 1030;
        RequestSeats requestSeats = new RequestSeats(requestSeatIds);
        String reservationNumber = bookingService.reserveSeat(userId, requestSeats, screeningId);

        // when : 예매 조회
        ReservationInfo reservationInfo = bookingService.getReservationInfo(reservationNumber);

        Assertions.assertThat(reservationInfo.getReservationNumber()).isEqualTo(reservationNumber);
    }

    @Test
    public void 예매_안하고_예매_조회_테스트() {
        //given
        userId = 1;
        requestSeatIds.add(1);
        //when

        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class, () -> {
            bookingService.getReservationInfo("asd");
        });
    }
}
