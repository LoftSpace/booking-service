package com.example.demo.repository;


import com.example.demo.dto.MovieScheduleDto;

import java.util.List;
import java.util.Optional;

public interface MovieScheduleRepository {
    Optional<List<MovieScheduleDto>> findByMovieId(Integer movieId);
}
