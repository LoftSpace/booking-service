package com.example.demo.service;

import com.example.demo.domain.Movie;
import com.example.demo.domain.Screening;
import com.example.demo.dto.MovieScheduleDto;
import com.example.demo.repository.MovieScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieInfoService {
    private final MovieService movieService;
    private final ScreeningService screeningService;


    public List<MovieScheduleDto> getMovieScheduleByMovieIdVer1(Long movieId){
        Movie movie = movieService.getMovieById(movieId);
        List<Screening> screeningListByMovieId = screeningService.getScreeningListByMovieId(movieId);

        List<MovieScheduleDto> movieSchedules = screeningListByMovieId.stream()
                .map(screening -> MovieScheduleDto.builder()
                        .movieId(movieId)
                        .movieName(movie.getMovieName())
                        .screeningId(screening.getScreeningId())
                        .startTime(screening.getStartTime())
                        .endTime(screening.getEndTime())
                        .build()
                )
                .toList();
        return movieSchedules;
    }

    public List<MovieScheduleDto> getMovieScheduleByMovieIdVer2(Long movieId){
        return screeningService.screeningRepository.findAllMovieScheduleByMovieId(movieId);
    }
}
