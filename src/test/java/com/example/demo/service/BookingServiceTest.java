package com.example.demo.service;


import com.example.demo.domain.RequestSeats;
import com.example.demo.domain.Screening;
import com.example.demo.factory.ReservationNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @InjectMocks
    private BookingService bookingService;

    @Mock
    private  SeatService seatService;
    @Mock
    private ReservationService reservationService;
    @Mock
    private ScreeningService screeningService;
    @Mock
    private ReservationNumberGenerator reservationNumberGenerator;

    static final int SAMPLE_USER_ID = 1;
    static final int SAMPLE_SCREENING_ID = 1024;
    static final int SAMPLE_MOVIE_ID = 4;

    private Screening testScreening;
    private Integer screeningId;
    private Integer userId;
    private Integer movieId;
    private Set<Integer> reservedSeatIds =  new HashSet<>();
    private List<Integer> rawRequestSeatIds = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        userId = SAMPLE_USER_ID;
        screeningId = SAMPLE_SCREENING_ID;
        movieId = SAMPLE_MOVIE_ID;
        testScreening = new Screening(screeningId,movieId,"2025-05-24 16:51","2025-05-24 18:51");

    }
    // 좌석 1,2,3,4,5 예매 요청, 이미 1,2,3, 예매된 상태 -> 실패 처리
    @Test
    public void reservationShouldThrowOnSeatConflictTest() {
        //given

        initReservedSeatIds();
        for(int i = 0; i < 5; i++)
            rawRequestSeatIds.add(i);
        RequestSeats requestSeats = new RequestSeats(rawRequestSeatIds);

        when(reservationService.getReservedSeatIdByScreeningId(screeningId)).thenReturn(reservedSeatIds);

        //then
        Exception exception = assertThrows(Exception.class,
                () -> bookingService.reserveSeat(userId, requestSeats, screeningId));
        org.assertj.core.api.Assertions.assertThat(exception.getMessage()).isEqualTo("이미 예약 되어있는 좌석 : " + reservedSeatIds);

    }

    // 요청 좌석 : 4,5 예약 좌석 1,2,3 -> 겹치는 거 없으므로 성공
    @Test
    public void reservationSuccessTest() throws Exception {
        //given

        rawRequestSeatIds.add(4);
        rawRequestSeatIds.add(5);
        RequestSeats requestSeats = new RequestSeats(rawRequestSeatIds);
        initReservedSeatIds();

        when(screeningService.getScreeningById(anyInt())).thenReturn(testScreening);
        when(reservationService.getReservedSeatIdByScreeningId(screeningId)).thenReturn(reservedSeatIds);

        //then
        Assertions.assertDoesNotThrow(() ->
                bookingService.reserveSeat(userId, requestSeats,screeningId));
    }

    // 요청 좌석이 없을 때
    @Test
    public void reservationNoRequestSeatIdTest() throws Exception {
        //given
        initReservedSeatIds();

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            new RequestSeats(rawRequestSeatIds);
        });
    }



    private void initReservedSeatIds() {
        reservedSeatIds.add(1);
        reservedSeatIds.add(2);
        reservedSeatIds.add(3);
    }
}
