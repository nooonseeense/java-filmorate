package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.model.User;
import ru.application.filmorate.storage.film.FilmStorageDao;
import ru.application.filmorate.storage.review.ReviewStorageDao;
import ru.application.filmorate.storage.user.UserStorageDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReviewStorageDaoTest {

    private final FilmStorageDao filmStorage;
    private final UserStorageDao userStorage;
    private final ReviewStorageDao reviewStorage;

    private Review goodReview;
    private Review badReview;
    private User user;
    private Film film;

    @BeforeEach
    public void init() {

        film = Film.builder()
                .id(1)
                .name("film name")
                .description("film description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(4, null))
                .build();

        user = User.builder()
                .id(1)
                .name("user name")
                .login("user login")
                .email("user@mail.com")
                .birthday(LocalDate.of(2010, 7, 19))
                .build();

        goodReview = Review.builder()
                .reviewId(1)
                .content("Good Film")
                .isPositive(Boolean.TRUE)
                .userId(1)
                .filmId(1)
                .build();

        badReview = Review.builder()
                .reviewId(2)
                .content("Bad Film")
                .isPositive(Boolean.FALSE)
                .userId(1)
                .filmId(1)
                .build();
    }


    @Test
    @DisplayName("Тестирование добавления отзыва")
    public void add() {
        filmStorage.add(film);
        userStorage.create(user);

        Review addedReview = reviewStorage.add(goodReview);

        assertEquals(addedReview.getReviewId(), 1);
        assertEquals(addedReview.getContent(), "Good Film");
        assertEquals(addedReview.getIsPositive(), true);
        assertEquals(addedReview.getUserId(), 1);
        assertEquals(addedReview.getFilmId(), 1);
        assertEquals(addedReview.getUseful(), 0);
    }

    @Test
    @DisplayName("Тестирование поиска отзыва по id")
    public void getById() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        Review reviewFromDb = reviewStorage.getById(addedReview.getReviewId());

        assertEquals(reviewFromDb.getReviewId(), 1);
    }

    @Test
    @DisplayName("Тестирование обновлегния отзыва")
    public void update() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        goodReview.setContent("Bad Film");
        goodReview.setIsPositive(Boolean.FALSE);

        reviewStorage.update(goodReview);
        Review reviewFromDb = reviewStorage.getById(addedReview.getReviewId());

        assertEquals(reviewFromDb.getContent(), "Bad Film");
        assertEquals(reviewFromDb.getIsPositive(), false);
    }

    @Test
    @DisplayName("Тестирование удаления отзыва")
    public void delete() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        reviewStorage.delete(addedReview.getReviewId());

        assertThrows(EmptyResultDataAccessException.class,
                () -> reviewStorage.getById(addedReview.getReviewId()));
    }

    @Test
    @DisplayName("Тестирование получения всех отзывов")
    public void getAll() {
        filmStorage.add(film);
        userStorage.create(user);
        reviewStorage.add(goodReview);

        List<Review> reviews = reviewStorage.getAll();

        assertEquals(reviews.size(), 1);
    }

    @Test
    @DisplayName("Тестирование получения всех отзывов по Id фильма")
    public void getAllByFilm() {
        filmStorage.add(film);
        userStorage.create(user);
        reviewStorage.add(goodReview);
        reviewStorage.add(badReview);

        List<Review> reviewsByFilm = reviewStorage.getAllByFilm(film.getId());

        assertEquals(reviewsByFilm.size(), 2);
    }

    @Test
    @DisplayName("Тестирование добавления лайка к отзыву")
    public void addLike() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        reviewStorage.addLike(addedReview.getReviewId(), user.getId());
        Optional<Boolean> like = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(like)
                .isPresent()
                .contains(true);
    }

    @Test
    @DisplayName("Тестирование добавления дизлайка к отзыву")
    public void addDislike() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        reviewStorage.addDislike(addedReview.getReviewId(), user.getId());
        Optional<Boolean> dislike = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(dislike)
                .isPresent()
                .contains(false);
    }

    @Test
    @DisplayName("Тестирование удаления лайка к отзыву")
    public void deleteLike() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        reviewStorage.addLike(addedReview.getReviewId(), user.getId());
        reviewStorage.deleteLike(addedReview.getReviewId(), user.getId());

        Optional<Boolean> like = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(like).isEmpty();
    }

    @Test
    @DisplayName("Тестирование удаления дизлайка к отзыву")
    public void deleteDislike() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        reviewStorage.addDislike(addedReview.getReviewId(), user.getId());
        reviewStorage.deleteDislike(addedReview.getReviewId(), user.getId());

        Optional<Boolean> dislike = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(dislike).isEmpty();
    }

    @Test
    @DisplayName("Тестирование проверки оценки пользователем отзыва")
    public void isLike() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        Optional<Boolean> noLike = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(noLike).isEmpty();

        reviewStorage.addLike(addedReview.getReviewId(), user.getId());
        Optional<Boolean> like = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(like)
                .isPresent()
                .contains(true);

        reviewStorage.deleteLike(addedReview.getReviewId(), user.getId());
        reviewStorage.addDislike(addedReview.getReviewId(), user.getId());
        Optional<Boolean> dislike = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(dislike)
                .isPresent()
                .contains(false);
    }

    @Test
    @DisplayName("Тестирование изменения оценки пользователем отзыва на противоположную")
    public void changeUserLike() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        reviewStorage.addLike(addedReview.getReviewId(), user.getId());
        reviewStorage.changeUserLike(addedReview.getReviewId(), user.getId(), false);
        Optional<Boolean> dislike = reviewStorage.isLike(addedReview.getReviewId(), user.getId());

        assertThat(dislike)
                .isPresent()
                .contains(false);
    }

    @Test
    @DisplayName("Тестирование перерасчета полезности отзыва")
    public void recalculateUseful() {
        filmStorage.add(film);
        userStorage.create(user);
        Review addedReview = reviewStorage.add(goodReview);

        assertEquals(addedReview.getUseful(), 0);

        reviewStorage.addLike(addedReview.getReviewId(), user.getId());
        reviewStorage.recalculateUseful(addedReview.getReviewId());
        Review reviewFromDb = reviewStorage.getById(addedReview.getReviewId());

        assertEquals(reviewFromDb.getUseful(), 1);

        reviewStorage.deleteLike(addedReview.getReviewId(), user.getId());
        reviewStorage.addDislike(addedReview.getReviewId(), user.getId());
        reviewStorage.recalculateUseful(addedReview.getReviewId());
        Review reviewAfterDislike = reviewStorage.getById(addedReview.getReviewId());

        assertEquals(reviewAfterDislike.getUseful(), -1);
    }
}
