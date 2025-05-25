package com.example.demo.repository;

import com.example.demo.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening,Long> {

    Optional<List<Screening>> findAllByMovieId(Long movieId);
    boolean existsById(Long screeningId);
    Optional<Screening> findById(Long screeningId);
    Screening save(Screening screening);
    void deleteById(Long screeningId);
}
