package com.example.demo.repository;

import com.example.demo.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening,Integer> {

    Optional<List<Screening>> findAllByMovieId(Integer movieId);
    boolean existsById(Integer screeningId);
    Optional<Screening> findById(Integer screeningId);
    Screening save(Screening screening);
    void deleteById(Integer screeningId);
}
