package ru.application.filmorate.storage.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.util.Mapper;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
            throw new ObjectWasNotFoundException(String.format("Отзыв с ID = %d не найден.", review.getReviewId()));
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
    }

    @Override
    public void addDislike(Integer reviewId, Integer userId) {
        String sql = "INSERT INTO REVIEW_RATING (REVIEW_ID, USER_ID, IS_POSITIVE) " +
                "VALUES (?, ?, ?)";

        jdbcTemplate.update(sql, reviewId, userId, false);
    }

    @Override
    public void deleteLike(Integer reviewId, Integer userId) {
        String sql = "DELETE FROM REVIEW_RATING WHERE REVIEW_ID = ? AND USER_ID = ?";

        jdbcTemplate.update(sql, reviewId, userId);
    }

    @Override
    public void deleteDislike(Integer reviewId, Integer userId) {
        String sql = "DELETE FROM REVIEW_RATING WHERE REVIEW_ID = ? AND USER_ID = ?";

        jdbcTemplate.update(sql, reviewId, userId);
    }

    /**
     *
     * @param reviewId ID отзыва
     * @param userId ID пользователя
     * @return TRUE - пользователь поставил лайк, FALSE - пользователь поставил дизлайк,
     * EMPTY - пользователь не ставил лайк/дизлайк.
     */
    @Override
    public Optional<Boolean> isLike(Integer reviewId, Integer userId) {
        String sql = "SELECT IS_POSITIVE " +
                "FROM REVIEW_RATING " +
                "WHERE REVIEW_ID = ? AND USER_ID = ?";

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, reviewId, userId);
        Optional<Boolean> isLike = Optional.empty();
        while (sqlRowSet.next()) {
            isLike = Optional.of(sqlRowSet.getBoolean("is_positive"));
        }
        return isLike;
    }

    @Override
    public void changeUserLike(Integer reviewId, Integer userId, boolean like) {
        String sql = "UPDATE REVIEW_RATING SET IS_POSITIVE = ? " +
                "WHERE REVIEW_ID = ? AND USER_ID = ?";

        jdbcTemplate.update(sql, like, reviewId, userId);
    }

    @Override
    public void recalculateUseful(Integer reviewId) {
        int reviewUseful = getReviewUseful(reviewId);

        String sql = "UPDATE REVIEW SET USEFUL = ? " +
                "WHERE ID = ?";

        jdbcTemplate.update(sql, reviewUseful, reviewId);
    }

    private int getReviewUseful(Integer reviewId) {
        String sql = "SELECT * FROM REVIEW_RATING WHERE REVIEW_ID = ?";

        List<Boolean> useful = new ArrayList<>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, reviewId);
        while (sqlRowSet.next()) {
            useful.add(sqlRowSet.getBoolean("is_positive"));
        }

        return Math.toIntExact(useful.stream().filter(p -> p).count() - useful.stream().filter(p -> !p).count());
    }
}
