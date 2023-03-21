package ru.application.filmorate.storage.film;

import ru.application.filmorate.model.Film;
import ru.application.filmorate.util.enumeration.FilmSort;

import java.util.List;

public interface FilmStorage {
    /**
     * Метод получения списка всех фильмов
     *
     * @return Список всех фильмов
     */
    List<Film> get();

    /**
     * Метод получения объекта фильма по ID
     *
     * @param filmId id фильма
     * @return Объект фильма
     */
    Film getById(Integer filmId);

    /**
     * Метод получения списка популярных фильмов
     *
     * @param count Количество отображаемых фильмов в списке
     * @return Список фильмов
     */
    List<Film> getPopularMoviesByLikes(Integer count);

    /**
     * Метод расширенного поиска списка фильмов по определённым параметрам
     *
     * @param query текст для поиска
     * @param by    может принимать значения director (поиск по режиссёру), title (поиск по названию)
     * @return Список фильмов
     */
    List<Film> getPopularMoviesFromAdvancedSearch(String query, String by);

    /**
     * Метод получения списка популярных фильмов по жанру
     *
     * @param count   Количество отображаемых фильмов в списке
     * @param genreId id жанра
     * @return Список фильмов
     */
    List<Film> getPopularMoviesByLikes(Integer count, Integer genreId);

    /**
     * Метод получения списка популярных фильмов по году
     *
     * @param count Количество отображаемых фильмов в списке
     * @param year  Запрашиваемый год
     * @return Список фильмов
     */
    List<Film> getPopularMoviesByLikes(Integer count, Short year);

    /**
     * Метод получения списка популярных фильмов по жанру и году
     *
     * @param count   Количество отображаемых фильмов в списке
     * @param genreId id жанра
     * @param year    Запрашиваемый год
     * @return Список фильмов
     */
    List<Film> getPopularMoviesByLikes(Integer count, Integer genreId, Short year);

    /**
     * Метод получения списка общих фильмов
     *
     * @param userId   id первого пользователя
     * @param friendId id второго пользователя
     * @return Список общих фильмов
     */
    List<Film> getCommonMovies(Integer userId, Integer friendId);

    /**
     * Метод получения списка фильмов по ID режиссёра
     *
     * @param directorId id режиссёра
     * @param sortBy     Сортировка по лайкам или году
     * @return Список фильмов по ID режиссёра
     */
    List<Film> getBy(int directorId, FilmSort sortBy);

    /**
     * Метод создания фильма
     *
     * @param film Объект фильма
     * @return Созданный объект фильма
     */
    Film add(Film film);

    /**
     * Метод изменения объекта фильм
     *
     * @param film Объект фильма
     * @return Изменённый объект фильма
     */
    Film update(Film film);

    /**
     * Метод добавления жанра в объект фильма
     *
     * @param film Объект фильма
     */
    void addGenres(Film film);

    /**
     * Метод добавления режиссёра в объект фильма
     *
     * @param film Объект фильма
     */
    void addDirectors(Film film);

    /**
     * Метод удаления жанра из объекта фильма
     *
     * @param film Объект фильма
     */
    void removeGenres(Film film);

    /**
     * Метод удаления режиссёра из объекта фильма
     *
     * @param film Объект фильма
     */
    void removeDirectors(Film film);

    /**
     * Метод получения списка рекомендованных фильмов по ID пользователя
     *
     * @param userId id пользователя
     * @return Список рекомендованных фильмов
     */
    List<Film> getRecommendedFilms(Integer userId);

    /**
     * Метод удаления фильма по ID
     *
     * @param id id фильма
     */
    void removeFilmById(Integer id);
}