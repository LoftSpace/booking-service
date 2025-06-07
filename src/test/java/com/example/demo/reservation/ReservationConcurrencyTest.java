package com.example.demo.reservation;


import com.example.demo.domain.RequestSeats;
import com.example.demo.domain.Reservation;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.BookingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
public class ReservationConcurrencyTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    ReservationRepository reservationRepository;

    private List<Integer> seatIds1 = new ArrayList<>();
    private List<Integer> seatIds2 = new ArrayList<>();
    private  Integer screeningId = 1026;
    private ExecutorService executorService = Executors.newFixedThreadPool(100);
    private int threadCount = 100;
    private CountDownLatch latch = new CountDownLatch(threadCount);


    @Test
    public void 같은좌석_예매시_동시성_테스트() {

        for(int i = 0; i < 2;i ++)
            seatIds1.add(i);
        RequestSeats requestSeats = new RequestSeats(seatIds1);

        for(int i = 0; i < threadCount; i ++){
            int userId = i;
            executorService.submit(() -> {
                try{
                    bookingService.reserveSeat(userId, requestSeats,screeningId);
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        executorService.shutdown();

        List<Reservation> allByScreeningId = reservationRepository.findAllByScreeningId(screeningId);
        Assertions.assertThat(allByScreeningId.size()).isEqualTo(2);

    }

    @Test
    public void 겹치는_좌석_예매_동시성_테스트() throws Exception {
        int user1 = 1;
        int user2 = 2;
        seatIds1.add(1);
        seatIds1.add(2);
        seatIds2.add(2);
        seatIds2.add(3);

        RequestSeats firstRequestSeats = new RequestSeats(seatIds1);
        RequestSeats secondRequestSeats = new RequestSeats(seatIds2);

        bookingService.reserveSeat(user1, firstRequestSeats,screeningId);
        bookingService.reserveSeat(user2, secondRequestSeats,screeningId);

        reservationRepository.findAllByScreeningId(screeningId);

    }
}
