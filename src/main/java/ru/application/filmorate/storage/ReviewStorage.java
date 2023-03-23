package ru.application.filmorate.storage;

import ru.application.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    /**
     * Метод создания отзыва
     *
     * @param review Объект отзыва
     * @return Созданный объект отзыва
     */
    Review add(Review review);

    /**
     * Метод получения отзыва по ID
     *
     * @param reviewId id отзыва
     * @return Объект отзыва
     */
    Review get(Integer reviewId);

    /**
     * Метод изменения отзыва
     *
     * @param review Объект отзыва
     * @return Изменённый объект отзыва
     */
    Review update(Review review);

    /**
     * Метод удаления отзыва по ID
     *
     * @param reviewId id отзыва
     */
    void delete(Integer reviewId);

    List<Review> get();

    /**
     * Метод получения списка всех отзывов по ID фильма
     *
     * @param filmId id фильма
     * @return Список с отзывами по ID фильма
     */
    List<Review> getAllByFilm(Integer filmId);

    /**
     * Метод добавления Like к отзыву по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void addLike(Integer reviewId, Integer userId);

    /**
     * Метод добавления DisLike к отзыву по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void addDislike(Integer reviewId, Integer userId);

    /**
     * Метод удаления Like у отзыва по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void deleteLike(Integer reviewId, Integer userId);

    /**
     * Метод удаления DisLike у отзыва по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     */
    void deleteDislike(Integer reviewId, Integer userId);

    /**
     * Метод проверки Like у отзыва по ID пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     * @return Empty лайк/дизлайк отсутствует, true лайк, false дизлайк
     */
    Optional<Boolean> isLike(Integer reviewId, Integer userId);

    /**
     * Метод изменения Like отзыва по id пользователя
     *
     * @param reviewId id отзыва
     * @param userId   id пользователя
     * @param rate true лайк, false дизлайк
     */
    void changeUserLike(Integer reviewId, Integer userId, boolean rate);

    /**
     * Метод пересчёта рейтинга полезности
     *
     * @param reviewId id отзыва
     */
    void recalculateUseful(Integer reviewId);
}
