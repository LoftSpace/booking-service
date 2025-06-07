package com.example.demo.reservation;

import com.example.demo.domain.RequestSeats;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.service.BookingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


@SpringBootTest
public class SeatSelectionConcurrencyTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    private List<Integer> seatIds1 = new ArrayList<>();
    private List<Integer> seatIds2 = new ArrayList<>();
    private  Integer screeningId = 1039;
    private ExecutorService executorService = Executors.newFixedThreadPool(100);
    private int threadCount = 2;
    private CountDownLatch latch = new CountDownLatch(threadCount);

    @BeforeEach
    void cleanUp() {
        jdbcTemplate.execute("delete from Reservation where screening_id = 1026 and seat_id in (0,1,2,3,4,5,6,7);");
    }

    @Test
    void 좌석_선택_동시성_테스트() throws InterruptedException {
        AtomicInteger successCount = new AtomicInteger();
        seatIds1.add(1);
        seatIds1.add(2);
        seatIds1.add(3);
        seatIds2.add(3);
        seatIds2.add(4);
        seatIds2.add(1);
        RequestSeats requestSeats1 = new RequestSeats(seatIds1);
        RequestSeats requestSeats2 = new RequestSeats(seatIds2);
        for(int i = 0; i < threadCount; i ++){
            int userId = i;
            executorService.submit(() -> {
                try{
                    if(userId==0)
                        bookingService.selectSeat(userId, requestSeats1,screeningId);
                    else
                        bookingService.selectSeat(userId, requestSeats2,screeningId);
                    successCount.getAndIncrement();
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Assertions.assertThat(successCount.get()).isEqualTo(1);
    }

    @Test
    void 좌석_선택_N명_동시성_테스트() throws InterruptedException {
        AtomicInteger successCount = new AtomicInteger();
        List<Integer> seatIds3 = new ArrayList<>();

        seatIds1.add(1);
        seatIds1.add(2);
        seatIds1.add(3);
        seatIds2.add(3);
        seatIds2.add(4);
        seatIds2.add(5);
        seatIds3.add(5);
        seatIds3.add(6);
        seatIds3.add(7);


        RequestSeats requestSeats1 = new RequestSeats(seatIds1);
        RequestSeats requestSeats2 = new RequestSeats(seatIds2);
        RequestSeats requestSeats3 = new RequestSeats(seatIds3);



        for(int i = 0; i < 3; i ++){
            int userId = i + 1;
            executorService.submit(() -> {
                try{
                    System.out.println(userId + "start");
                    if(userId == 1)
                        bookingService.selectSeat(userId, requestSeats1,screeningId);
                    else if(userId == 2 )
                        bookingService.selectSeat(userId, requestSeats2,screeningId);
                    else if(userId == 3 )
                        bookingService.selectSeat(userId, requestSeats3,screeningId);

                    successCount.getAndIncrement();
                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println(userId + "failed");
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Assertions.assertThat(successCount.get()).isEqualTo(2);
    }
}
