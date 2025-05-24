package com.example.demo.service;

import com.example.demo.domain.Movie;
import com.example.demo.domain.Screening;
import com.example.demo.dto.MovieScheduleDto;
import com.example.demo.repository.MovieScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieScheduleService {
    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final MovieScheduleRepository movieScheduleRepository;

    public List<Screening> getScheduleByMovieId(Long movieId){
        return screeningService.getScreeningListByMovieId(movieId);
    }

    public List<MovieScheduleDto> getMovieScheduleByMovieIdVer2(Long movieId){
        return movieScheduleRepository.findByMovieId(movieId)
                .orElseThrow(() -> new EntityNotFoundException("상영정보 없음"));
    }
}
