package ru.application.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.storage.FilmGenreStorage;
import ru.application.filmorate.storage.util.Mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmGenreDao implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Genre> get(int id) {
        return jdbcTemplate.query(
                "SELECT g.ID, g.NAME " +
                "FROM FILM_GENRE fg " +
                "LEFT JOIN GENRE g ON fg.GENRE_ID = g.ID " +
                "WHERE fg.FILM_ID = ?",
                Mapper::genreMapper, id
        );
    }

    @Override
    public void set(List<Film> films) {
        if (films != null) {
            List<Integer> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
            Map<Integer, Film> filmsMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
            SqlParameterSource source = new MapSqlParameterSource("filmsId", filmsId);
            SqlRowSet set = namedJdbcTemplate.queryForRowSet(
                    "SELECT FILM_ID, g.* " +
                    "FROM FILM_GENRE " +
                    "JOIN GENRE g ON g.ID = FILM_GENRE.GENRE_ID " +
                    "WHERE FILM_GENRE.FILM_ID IN (:filmsId)",
                    source
            );
            while (set.next()) {
                int filmId = set.getInt("film_id");
                int genreId = set.getInt("id");
                String genreName = set.getString("name");
                filmsMap.get(filmId).getGenres().add(new Genre(genreId, genreName));
            }
        }
    }
}