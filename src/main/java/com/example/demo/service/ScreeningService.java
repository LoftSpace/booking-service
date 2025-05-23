package com.example.demo.service;

import com.example.demo.domain.Screening;
import com.example.demo.repository.ScreeningRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {
    @Autowired
    ScreeningRepository screeningRepository;

    public Screening getScreeningById(Long screeningId){
        return screeningRepository.findById(screeningId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("해당 시간대에 영화 없음")));
    }

    public void assertScreeningExists(Long screeningId){
        if(screeningRepository.existsById(screeningId))
            throw new EntityNotFoundException(String.format("해당 시간대에 영화 없음"));
    }

    public List<Screening> getScreeningListByMovieId(Long movieId){
        return screeningRepository.findAllByMovieId(movieId);
    }
}
