package com.example.demo.repository;

import com.example.demo.domain.Screening;

import java.util.Optional;

public interface ScreeningRepository {
    public abstract Optional<Screening> findById(Long screeningId);
    public abstract boolean existsById(Long screeningId);
}
