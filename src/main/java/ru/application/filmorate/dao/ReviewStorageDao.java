package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.impl.ReviewStorage;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.util.Mapper;

import java.sql.PreparedStatement;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewStorageDao implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review add(Review review) {

        String sql = "INSERT INTO REVIEW (CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement rs = connection.prepareStatement(sql, new String[]{"id"});
            rs.setString(1, review.getContent());
            rs.setBoolean(2, review.getIsPositive());
            rs.setInt(3, review.getUserId());
            rs.setInt(4, review.getFilmId());
            rs.setInt(5, 0);
            return rs;
        }, keyHolder);
        review.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return review;
    }

    @Override
    public Review getById(Integer reviewId) {
        String sql = "SELECT R.ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, R.USEFUL " +
                "FROM REVIEW R " +
                "WHERE ID = ?";

        return jdbcTemplate.queryForObject(sql, Mapper::reviewMapper, reviewId);
    }
}
