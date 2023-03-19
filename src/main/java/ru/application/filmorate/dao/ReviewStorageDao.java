package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.impl.ReviewStorage;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.util.Mapper;

import java.sql.PreparedStatement;
import java.util.List;
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
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return review;
    }

    @Override
    public Review getById(Integer reviewId) {
        String sql = "SELECT R.ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, R.USEFUL " +
                "FROM REVIEW R " +
                "WHERE ID = ?";

        return jdbcTemplate.queryForObject(sql, Mapper::reviewMapper, reviewId);
    }

    @Override
    public Review update(Review review) {
        String sql = "UPDATE REVIEW SET CONTENT = ?, IS_POSITIVE = ? " +
                "WHERE ID = ?";

        int newRows = jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        if (newRows == 0) {
            String message = String.format("Отзыв с ID = %d не найден.", review.getReviewId());
            log.debug(message);
            throw new ObjectWasNotFoundException(message);
        }
        return getById(review.getReviewId());
    }

    @Override
    public void delete(Integer reviewId) {
        String sql = "DELETE FROM REVIEW WHERE ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    @Override
    public List<Review> getAll() {
        String sql = "SELECT R.ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, R.USEFUL " +
                "FROM REVIEW R";

        return jdbcTemplate.query(sql, Mapper::reviewMapper);
    }

    @Override
    public List<Review> getAllByFilm(Integer filmId) {
        String sql = "SELECT R.ID, R.CONTENT, R.IS_POSITIVE, R.USER_ID, R.FILM_ID, R.USEFUL " +
                "FROM REVIEW R " +
                "WHERE FILM_ID = ?";

        return jdbcTemplate.query(sql, Mapper::reviewMapper, filmId);
    }

    @Override
    public void addLike(Integer reviewId, Integer userId) {
        String sql = "INSERT INTO REVIEW_RATING (REVIEW_ID, USER_ID, IS_POSITIVE) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, true);

        incrementReviewRating(reviewId);
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) {
        String sql = "INSERT INTO REVIEW_RATING (REVIEW_ID, USER_ID, IS_POSITIVE) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, true);

        decrementReviewRating(reviewId);
    }

    @Override
    public void deleteLike(Integer reviewId, Integer userId) {
        String sql = "DELETE FROM REVIEW_RATING WHERE REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, userId);

        decrementReviewRating(reviewId);
    }

    @Override
    public void deleteDislike(Integer reviewId, Integer userId) {
        String sql = "DELETE FROM REVIEW_RATING WHERE REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, reviewId, userId);

        incrementReviewRating(reviewId);
    }

    private void incrementReviewRating(Integer reviewId) {
        String sql = "UPDATE REVIEW SET USEFUL = USEFUL + 1 " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    private void decrementReviewRating(Integer reviewId) {
        String sql = "UPDATE REVIEW SET USEFUL = USEFUL - 1 " +
                "WHERE ID = ?";
        jdbcTemplate.update(sql, reviewId);
    }
}
