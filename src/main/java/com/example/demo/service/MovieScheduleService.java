package com.example.demo.service;

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
    private final ScreeningService screeningService;
    private final MovieScheduleRepository movieScheduleRepository;

    public List<Screening> getScheduleByMovieId(Integer movieId){
        return screeningService.getScreeningListByMovieId(movieId);
    }

    public List<MovieScheduleDto> getMovieWithSchedules(Integer movieId){
        return movieScheduleRepository.findByMovieId(movieId)
                .orElseThrow(() -> new EntityNotFoundException("상영정보 없음"));
    }
}
