package ru.application.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.application.filmorate.exception.ObjectWasNotFoundException;
import ru.application.filmorate.exception.ReviewValidationException;
import ru.application.filmorate.model.Review;
import ru.application.filmorate.storage.ReviewStorage;
import ru.application.filmorate.storage.util.enumeration.EventType;
import ru.application.filmorate.storage.util.enumeration.OperationType;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final EventService eventService;
    private final FilmService filmService;
    private final UserService userService;

    /**
     * Метод создания отзыва
     *
     * @param review Объект отзыва
     * @return Созданный объект отзыва
     */
    public Review add(Review review) {
        validateReview(review);
        Review addedReview = reviewStorage.add(review);
        eventService.create(addedReview.getUserId(), EventType.REVIEW, OperationType.ADD, addedReview.getReviewId());
        return addedReview;
    }

    /**
     * Метод изменения отзыва
     *
     * @param review Объект отзыва
     * @return Изменённый объект отзыва
     */
    public Review update(Review review) {
        validateReview(review);
        Review updatedReview = reviewStorage.update(review);
        eventService.create(updatedReview.getUserId(), EventType.REVIEW, OperationType.UPDATE, updatedReview.getReviewId());
        return updatedReview;
    }

    /**
     * Метод получения отзыва по ID
     *
     * @param reviewId id отзыва
     * @return Объект отзыва
     */
    public Review get(Integer reviewId) {
        try {
            return reviewStorage.get(reviewId);
        } catch (EmptyResultDataAccessException e) {
            log.debug("get(Integer reviewId): Отзыв с идентификатором {} не найден.", reviewId);
            throw new ObjectWasNotFoundException(String.format("Отзыв с идентификатором %d не найден.", reviewId));
        }
    }

    /**
     * Метод удаления отзыва по ID
     *
     * @param reviewId id отзыва
     */
    public void delete(Integer reviewId) {
        Review review = get(reviewId);
        reviewStorage.delete(reviewId);
        eventService.create(review.getUserId(), EventType.REVIEW, OperationType.REMOVE, review.getReviewId());
    }

    /**
     * Метод получения списка всех отзывов по ID фильма
     *
     * @param filmId id фильма
     * @param count  Количество отзывов в списке
     * @return Список с отзывами по ID фильма
     */
    public List<Review> get(Integer filmId, Integer count) {
        List<Review> reviews;
        if (filmId == null) {
            reviews = reviewStorage.get();
        } else {
            reviews = reviewStorage.getAllByFilm(filmId);
        }
        return reviews.stream()
                .sorted(Comparator.comparing(Review::getUseful).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Метод добавления Like к отзыву по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    public void addLike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        Optional<Boolean> isLike = reviewStorage.isLike(reviewId, userId);
        if (isLike.isPresent()) {
            if (isLike.get()) {
                log.debug("addLike(Integer reviewId, Integer userId): У отзыва с id = {} уже есть лайк от " +
                        "пользователя с id = {}.", reviewId, userId);
                throw new ReviewValidationException(String.format("У отзыва с id: %s уже есть лайк " +
                        "от пользователя с id: %s.", reviewId, userId));
            } else {
                reviewStorage.changeUserLike(reviewId, userId, true);
            }
        } else {
            reviewStorage.addLike(reviewId, userId);
        }
        reviewStorage.recalculateUseful(reviewId);
    }

    /**
     * Метод добавления DisLike к отзыву по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    public void addDislike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        Optional<Boolean> isLike = reviewStorage.isLike(reviewId, userId);
        if (isLike.isPresent()) {
            if (!isLike.get()) {
                log.debug("addDislike(Integer reviewId, Integer userId): У отзыва с id = {} уже есть дизлайк от " +
                        "пользователя с id = {}.", reviewId, userId);
                throw new ReviewValidationException(String.format("У отзыва с id: %s уже есть дизлайк " +
                        "от пользователя с id: %s.", reviewId, userId));
            } else {
                reviewStorage.changeUserLike(reviewId, userId, false);
            }
        } else {
            reviewStorage.addDislike(reviewId, userId);
        }
        reviewStorage.recalculateUseful(reviewId);
    }

    /**
     * Метод удаления Like у отзыва по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    public void deleteLike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        Optional<Boolean> isLike = reviewStorage.isLike(reviewId, userId);
        if (isLike.isEmpty()) {
            log.debug("deleteLike(Integer reviewId, Integer userId): У отзыва с id: {} нет лайка " +
                    "от пользователя с id: {}.", reviewId, userId);
            throw new ReviewValidationException(String.format("У отзыва с id: %s нет лайка " +
                    "от пользователя с id: %s.", reviewId, userId));
        } else if (isLike.get()) {
            reviewStorage.deleteLike(reviewId, userId);
            reviewStorage.recalculateUseful(reviewId);
        }
    }

    /**
     * Метод удаления DisLike у отзыва по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    public void deleteDislike(Integer reviewId, Integer userId) {
        validateReviewAndUser(reviewId, userId);
        Optional<Boolean> isLike = reviewStorage.isLike(reviewId, userId);
        if (isLike.isEmpty()) {
            log.debug("deleteDislike(Integer reviewId, Integer userId): У отзыва с id: {} нет дизлайка " +
                    "от пользователя с id: {}.", reviewId, userId);
            throw new ReviewValidationException(String.format("У отзыва с id: %s нет дизлайка " +
                    "от пользователя с id: %s.", reviewId, userId));
        } else if (!isLike.get()) {
            reviewStorage.deleteDislike(reviewId, userId);
            reviewStorage.recalculateUseful(reviewId);
        }
    }

    /**
     * Метод проверки отзыва на наличие в БД
     *
     * @param review Объект отзыва
     */
    private void validateReview(Review review) {
        filmService.get(review.getFilmId());
        userService.get(review.getUserId());
    }

    /**
     * Метод проверки пользователя и отзыва в БД
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    private void validateReviewAndUser(Integer reviewId, Integer userId) {
        get(reviewId);
        userService.get(userId);
    }
}
