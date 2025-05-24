package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieScheduleDto {
    private Long movieId; // movieId가 들어가야 하는가?
    private Long screeningId;
    private String movieName;
    private String startTime;
    private String endTime;
    // 변경 가능성 : 새로운 필드 추가 ex) 평점, 제작사, 배우 등

}
