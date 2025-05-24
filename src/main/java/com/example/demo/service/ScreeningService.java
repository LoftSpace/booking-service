package com.example.demo.service;

import com.example.demo.domain.Screening;

import com.example.demo.repository.ScreeningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    @Qualifier("screeningJdbcTemplateRepository")
    private final ScreeningRepository screeningJdbcTemplateRepository;
    @Qualifier("screeningJpaRepository")
    private final ScreeningRepository screeningJpaRepository;

    public Screening getScreeningById(Long screeningId){
        return screeningJpaRepository.findById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("해당 시간대에 영화 없음")));
    }

    public void assertScreeningExists(Long screeningId){
        if(screeningJpaRepository.existsById(screeningId))
            throw new EntityNotFoundException(String.format("해당 시간대에 영화 없음"));
    }

    public List<Screening> getScreeningListByMovieId(Long movieId){
        return screeningJpaRepository.findAllByMovieId(movieId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("상영 정보 없음")));
    }


}
