package com.example.demo.repository;

import com.example.demo.domain.Screening;
import com.example.demo.dto.MovieScheduleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ScreeningRepository extends JpaRepository<Screening,Long> {

    boolean existsById(Long screeningId);
    List<Screening> findAllByMovieId(Long movieId);

    @Query(value = "SELECT movieId,screeningId,movieName,startTime,endTime" +
            "FROM movie m" +
            "LEFT JOIN screening s ON  m.movieId = s.movieId" +
            "WHERE movieId = :movieId"
    ,nativeQuery = true)
    List<MovieScheduleDto> findAllMovieScheduleByMovieId(@Param("movieId") Long movieId);
}
