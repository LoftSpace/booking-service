package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
@Getter
public class RequestSeatIds {
    private List<Integer> ids;

    public RequestSeatIds(List<Integer> ids){
        if(ids == null || ids.isEmpty())
            throw new IllegalArgumentException("좌석을 선택해야 합니다");

        this.ids = ids;
    }
    public boolean isEmpty() {
        return ids.isEmpty();
    }
    public List<Integer> getConflicts(Set<Integer> reservedSeats) {
        return ids.stream().filter(reservedSeats::contains).toList();
    }
}
