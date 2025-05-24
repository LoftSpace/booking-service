package com.example.demo.service;

import com.example.demo.domain.Movie;
import com.example.demo.domain.Screening;
import com.example.demo.dto.MovieScheduleDto;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieInfoServiceTest {
    @InjectMocks
    private MovieInfoService movieInfoService;

    @Mock
    private MovieService movieService;
    @Mock
    private ScreeningService screeningService;

    private Movie testMovie;
    private List<Screening> testScreeningList;
    private Long movieId;
    private static final Long SAMPLE_MOVIE_ID = 7L;

    @BeforeEach
    void setUp() {
        testMovie = new Movie(movieId,"Avengers","2h30m");
    }

    @Test
    public void getMovieScheduleByMovieId() {
        //given
        movieId = SAMPLE_MOVIE_ID;
        initScreeningListByMovieId(movieId);

        when(movieService.getMovieById(anyLong())).thenReturn(testMovie);
        when(screeningService.getScreeningListByMovieId(anyLong())).thenReturn(testScreeningList);

        //when
        List<MovieScheduleDto> movieScheduleList = movieInfoService.getMovieScheduleByMovieIdVer1(movieId);
        //then
        Assertions.assertThat(movieScheduleList).isNotNull();
        Assertions.assertThat(movieScheduleList.size()).isEqualTo(testScreeningList.size());
        testDetailField(movieScheduleList);
    }

    @Test
    public void getMovieScheduleByMovieIdWhenScheduleIsNull(){
        //given
        movieId = SAMPLE_MOVIE_ID;
        when(movieService.getMovieById(anyLong())).thenReturn(testMovie);
        when(screeningService.getScreeningListByMovieId(anyLong())).thenThrow(new EntityNotFoundException("상영정보 없음"));

        //
        org.junit.jupiter.api.Assertions.assertThrows(EntityNotFoundException.class,
                () -> movieInfoService.getMovieScheduleByMovieIdVer1(movieId));

    }

    private void testDetailField(List<MovieScheduleDto> movieScheduleList) {
        for(int i = 0; i < movieScheduleList.size(); i++){
            MovieScheduleDto dto = movieScheduleList.get(i);
            Screening originalScreening = testScreeningList.get(i);

            Assertions.assertThat(dto.getMovieName()).isEqualTo(testMovie.getMovieName()); // 영화 제목 일치 여부
            Assertions.assertThat(dto.getStartTime()).isEqualTo(originalScreening.getStartTime()); // 상영 시작 시간 일치 여부
            Assertions.assertThat(dto.getEndTime()).isEqualTo(originalScreening.getEndTime()); // 상영 종료 시간 일치 여부

        }
    }

    private void initScreeningListByMovieId(Long movieId) {
        testScreeningList = new ArrayList<>();
        testScreeningList.add(new Screening(1L, movieId, "10:00", "12:30"));
        testScreeningList.add(new Screening(2L, movieId, "13:00", "15:30"));

    }
}
