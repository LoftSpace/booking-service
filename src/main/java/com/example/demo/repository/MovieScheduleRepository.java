package com.example.demo.repository;

import com.example.demo.dto.MovieScheduleDto;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MovieScheduleRepository {
    List<MovieScheduleDto> findMovieSchedulesByMovieId(Long movieId);
}
