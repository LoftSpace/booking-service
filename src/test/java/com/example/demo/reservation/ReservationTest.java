package com.example.demo.reservation;

import com.example.demo.domain.RequestSeatIds;
import com.example.demo.service.BookingService;
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
        //given
        userId = 1;
        requestSeatIds.add(1);
        screeningId = 1030;
        RequestSeatIds
        bookingService.reserveSeat(userId,requestSeatIds,screeningId);
        //
        bookingService.getReservationInfo()
    }
}
