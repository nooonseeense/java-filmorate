package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Genre;
import ru.application.filmorate.impl.FilmGenreStorage;
import ru.application.filmorate.util.Mapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmGenreDao implements FilmGenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public List<Genre> get(int id) {
        String sql = "SELECT g.ID, g.NAME " +
                "FROM FILM_GENRE AS fg " +
                "LEFT JOIN GENRE AS g ON fg.GENRE_ID = g.ID " +
                "WHERE fg.FILM_ID = ?";
        return jdbcTemplate.query(sql, Mapper::genreMapper, id);
    }

    @Override
    public void setGenres(List<Film> films) {
        if (films != null) {
            String sqlGenres = "SELECT FILM_ID, g.* " +
                    "FROM FILM_GENRE " +
                    "JOIN GENRE AS g ON g.ID = FILM_GENRE.GENRE_ID " +
                    "WHERE FILM_GENRE.FILM_ID IN (:filmsId)";

            List<Integer> filmsId = films.stream().map(Film::getId).collect(Collectors.toList());
            Map<Integer, Film> filmsMap = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));

            SqlParameterSource source = new MapSqlParameterSource("filmsId", filmsId);
            SqlRowSet set = namedJdbcTemplate.queryForRowSet(sqlGenres, source);

            while (set.next()) {
                int filmId = set.getInt("film_id");
                int genreId = set.getInt("id");
                String genreName = set.getString("name");
                filmsMap.get(filmId).getGenres().add(new Genre(genreId, genreName));
            }
        }
    }
}