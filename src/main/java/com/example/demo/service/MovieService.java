package com.example.demo.service;

import com.example.demo.domain.Movie;
import com.example.demo.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Movie getMovieById(Long movieId){
        return movieRepository.findById(movieId).orElseThrow(
                () -> new EntityNotFoundException("해당 영화 상영정보 없음"));
    }
}
