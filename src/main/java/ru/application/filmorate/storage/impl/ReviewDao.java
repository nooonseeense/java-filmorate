package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.exception.ObjectDoesNotExist;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.storage.util.Mapper;
import ru.application.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewDao implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review add(Review review) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement rs = connection.prepareStatement(
                    "INSERT INTO REVIEW(CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) VALUES (?,?,?,?,?)",
                    new String[]{"id"}
            );
            rs.setString(1, review.getContent());
            rs.setBoolean(2, review.getIsPositive());
            rs.setInt(3, review.getUserId());
            rs.setInt(4, review.getFilmId());
            rs.setInt(5, 0);
            return rs;
        }, keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return review;
    }

    @Override
    public Review get(Integer reviewId) {
        return jdbcTemplate.queryForObject(
                "SELECT ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL " +
                "FROM REVIEW " +
                "WHERE ID = ?",
                Mapper::reviewMapper, reviewId
        );
    }

    @Override
    public Review update(Review review) {
        int newRows = jdbcTemplate.update(
                "UPDATE REVIEW SET CONTENT = ?, IS_POSITIVE = ? WHERE ID = ?",
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId()
        );
        if (newRows == 0) {
            log.debug("update(Review review): Отзыв с ID = {} не найден.", review.getReviewId());
            throw new ObjectDoesNotExist(String.format("Отзыв с ID = %d не найден.", review.getReviewId()));
        }
        return get(review.getReviewId());
    }

    @Override
    public void delete(Integer reviewId) {
        jdbcTemplate.update("DELETE FROM REVIEW WHERE ID = ?", reviewId);
    }

    @Override
    public List<Review> get() {
        return jdbcTemplate.query(
                "SELECT ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL " +
                "FROM REVIEW",
                Mapper::reviewMapper
        );
    }

    @Override
    public List<Review> getAllByFilm(Integer filmId) {
        return jdbcTemplate.query(
                "SELECT ID, CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL " +
                "FROM REVIEW " +
                "WHERE FILM_ID = ?",
                Mapper::reviewMapper, filmId
        );
    }

    @Override
    public void addLike(Integer reviewId, Integer userId) {
        jdbcTemplate.update("INSERT INTO REVIEW_RATING (REVIEW_ID, USER_ID, IS_POSITIVE) VALUES (?, ?, ?)",
                reviewId, userId, true
        );
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) {
        jdbcTemplate.update("INSERT INTO REVIEW_RATING (REVIEW_ID, USER_ID, IS_POSITIVE) VALUES (?, ?, ?)",
                reviewId, userId, false
        );
    }

    @Override
    public void deleteLike(Integer reviewId, Integer userId) {
        jdbcTemplate.update("DELETE FROM REVIEW_RATING WHERE REVIEW_ID = ? AND USER_ID = ?", reviewId, userId);
    }

    @Override
    public void deleteDislike(Integer reviewId, Integer userId) {
        jdbcTemplate.update("DELETE FROM REVIEW_RATING WHERE REVIEW_ID = ? AND USER_ID = ?", reviewId, userId);
    }

    @Override
    public Optional<Boolean> isLike(Integer reviewId, Integer userId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(
                "SELECT IS_POSITIVE " +
                "FROM REVIEW_RATING " +
                "WHERE REVIEW_ID = ? AND USER_ID = ?",
                reviewId, userId
        );
        Optional<Boolean> isLike = Optional.empty();
        while (sqlRowSet.next()) {
            isLike = Optional.of(sqlRowSet.getBoolean("is_positive"));
        }
        return isLike;
    }

    @Override
    public void changeUserLike(Integer reviewId, Integer userId, boolean like) {
        jdbcTemplate.update("UPDATE REVIEW_RATING SET IS_POSITIVE = ? WHERE REVIEW_ID = ? AND USER_ID = ?",
                like, reviewId, userId
        );
    }

    @Override
    public void recalculateUseful(Integer reviewId) {
        Integer reviewUseful = getReviewUseful(reviewId);
        jdbcTemplate.update("UPDATE REVIEW SET USEFUL = ? WHERE ID = ?", reviewUseful, reviewId);
    }

    @Override
    public Boolean isExist(Integer reviewId) {
        return jdbcTemplate.queryForObject("SELECT EXISTS(SELECT * FROM REVIEW WHERE ID = ?)",
                Boolean.class, reviewId);
    }

    /**
     * Метод получения полезности отзыва
     * @param reviewId id отзыва
     * @return расчитывает текущую полезность отзыва
     */
    private Integer getReviewUseful(Integer reviewId) {
        List<Boolean> useful = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM REVIEW_RATING WHERE REVIEW_ID = ?", reviewId);

        while (sqlRowSet.next()) {
            useful.add(sqlRowSet.getBoolean("is_positive"));
        }

        return Math.toIntExact(useful.stream().filter(p -> p).count() - useful.stream().filter(p -> !p).count());
    }
}
