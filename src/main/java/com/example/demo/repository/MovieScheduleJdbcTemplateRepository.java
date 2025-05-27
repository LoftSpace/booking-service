package com.example.demo.repository;

import com.example.demo.dto.MovieScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovieScheduleJdbcTemplateRepository implements MovieScheduleRepository{
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Optional<List<MovieScheduleDto>> findByMovieId(Integer movieId) {
        String sql = "SELECT * " +
                "FROM movie m " +
                "LEFT JOIN screening s ON m.movie_id = s.movie_id " +
                "WHERE m.movie_id = ? ";

        return Optional.of(jdbcTemplate.query(sql, movieScheduleDtoRowMapper(),movieId));

    }

    private RowMapper<MovieScheduleDto> movieScheduleDtoRowMapper() {
        return new RowMapper<MovieScheduleDto>() {
            @Override
            public MovieScheduleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return MovieScheduleDto.builder()
                        .movieId(rs.getLong("movie_id"))
                        .movieName(rs.getString("movie_name"))
                        .screeningId(rs.getLong("screening_id"))
                        .startTime(rs.getString("start_time"))
                        .endTime(rs.getString("end_time"))
                        .build();
            }
        };
    }
}
