package com.example.demo.repository;

import com.example.demo.domain.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening,Long> {

    boolean existsById(Long screeningId);
}
