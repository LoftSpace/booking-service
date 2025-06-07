package com.example.demo.repository;

import com.example.demo.domain.Reservation;
import com.example.demo.domain.ReservationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Set;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    @Query("SELECT r.seatId FROM Reservation r WHERE r.screeningId = :screeningId")
    Set<Integer> findReservedSeatIdByScreeningId(@Param("screeningId") Integer screeningId);

    @Query("""
    SELECT new com.example.demo.domain.ReservationInfo
    (
        r.id, r.reservationNumber, r.reservedDate,
        s.id, s.startTime,
        m.id, m.movieName,
        st.id
    )
    FROM Reservation r
    JOIN Screening s ON r.screeningId = s.id
    JOIN Movie m ON r.movieId = m.id
    JOIN Seat st ON r.seatId = st.id
    WHERE r.userId = :userId
""")
    List<ReservationInfo> findReservationInfoByUserId(@Param("userId") Integer userId);

    @Query("""
    SELECT new com.example.demo.domain.ReservationInfo
    (
        r.id, r.reservationNumber, r.reservedDate,
        s.id, s.startTime,
        m.id, m.movieName,
        st.id
    )
    FROM Reservation r
    JOIN Screening s ON r.screeningId = s.id
    JOIN Movie m ON r.movieId = m.id
    JOIN Seat st ON r.seatId = st.id
    WHERE r.reservation_number = :reservationNumber
    """)
    ReservationInfo findReservationInfoByReservationNumber(@Param("reservationNumber") String reservationNumber);

    List<Reservation> findAllByScreeningId(Integer screeningId);
    List<Reservation> findByUserId(Integer userId);

}
