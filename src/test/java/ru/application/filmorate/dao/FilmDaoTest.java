package ru.application.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.application.filmorate.controller.FilmController;
import ru.application.filmorate.model.Film;
import ru.application.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDaoTest {

    private final FilmController filmController;

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnFilm() {
        Film filmExpected = Film.builder()
                .id(1)
                .name("Film1")
                .description("D1")
                .duration(100)
                .releaseDate(LocalDate.of(2004, 12, 27))
                .mpa(new Mpa(1, "G"))
                .build();

        Film filmInDb = filmController.get(filmExpected.getId());

        assertEquals(filmExpected.getId(), filmInDb.getId());
        assertEquals(filmExpected.getName(), filmInDb.getName());
        assertEquals(filmExpected.getDescription(), filmInDb.getDescription());
        assertEquals(filmExpected.getReleaseDate(), filmInDb.getReleaseDate());
        assertEquals(filmExpected.getMpa().getId(), filmInDb.getMpa().getId());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnFilms() {
        Film filmExpected = Film.builder()
                .id(1)
                .name("Film1")
                .description("D1")
                .duration(100)
                .releaseDate(LocalDate.of(2004, 12, 27))
                .mpa(new Mpa(1, "G"))
                .build();

        Film filmExpected2 = Film.builder()
                .id(2)
                .name("Film2")
                .description("D2")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 12, 28))
                .mpa(new Mpa(2, "PG"))
                .build();

        List<Film> filmsInDb = filmController.get();

        assertEquals(filmExpected.getId(), filmsInDb.get(0).getId());
        assertEquals(filmExpected.getName(), filmsInDb.get(0).getName());
        assertEquals(filmExpected.getDescription(), filmsInDb.get(0).getDescription());
        assertEquals(filmExpected.getReleaseDate(), filmsInDb.get(0).getReleaseDate());
        assertEquals(filmExpected.getMpa(), filmsInDb.get(0).getMpa());

        assertEquals(filmExpected2.getId(), filmsInDb.get(1).getId());
        assertEquals(filmExpected2.getName(), filmsInDb.get(1).getName());
        assertEquals(filmExpected2.getDescription(), filmsInDb.get(1).getDescription());
        assertEquals(filmExpected2.getReleaseDate(), filmsInDb.get(1).getReleaseDate());
        assertEquals(filmExpected2.getMpa(), filmsInDb.get(1).getMpa());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnNewFilm() {
        Film film = Film.builder()
                .id(4)
                .name("Film2")
                .description("D2")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 12, 28))
                .mpa(new Mpa(1, "G"))
                .build();

        Film filmExpected = Film.builder()
                .id(4)
                .name("Film2")
                .description("D2")
                .duration(120)
                .releaseDate(LocalDate.of(2005, 12, 28))
                .mpa(new Mpa(1, "G"))
                .build();

        Film actualFilm = filmController.add(film);

        assertEquals(filmExpected.getId(), actualFilm.getId());
        assertEquals(filmExpected.getName(), actualFilm.getName());
        assertEquals(filmExpected.getDescription(), actualFilm.getDescription());
        assertEquals(filmExpected.getReleaseDate(), actualFilm.getReleaseDate());
        assertEquals(filmExpected.getMpa().getId(), actualFilm.getMpa().getId());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnUpdateFilm() {
        Film filmExpected = Film.builder()
                .id(1)
                .name("Обновленный фильм")
                .description("Обновленное описание")
                .duration(110)
                .releaseDate(LocalDate.of(2005, 12, 27))
                .mpa(new Mpa(2, "PG"))
                .build();


        filmController.update(filmExpected);
        Film actualFilm = filmController.get(filmExpected.getId());

        assertEquals(filmExpected.getId(), actualFilm.getId());
        assertEquals(filmExpected.getName(), actualFilm.getName());
        assertEquals(filmExpected.getDescription(), actualFilm.getDescription());
        assertEquals(filmExpected.getReleaseDate(), actualFilm.getReleaseDate());
        assertEquals(filmExpected.getMpa().getId(), actualFilm.getMpa().getId());
    }

    @Test
    @Sql(scripts = "file:src/test/resources/schema-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "file:src/test/resources/data-tst.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void shouldReturnPopularMoviesByLikes() {
        Film filmExpected = Film.builder()
                .id(1)
                .name("Film1")
                .description("D1")
                .duration(100)
                .releaseDate(LocalDate.of(2004, 12, 27))
                .mpa(new Mpa(1, "G"))
                .build();

        Film actualFilm = filmController.getPopularMoviesByLikes(1,null,null).get(0);

        assertEquals(filmExpected.getId(), actualFilm.getId());
        assertEquals(filmExpected.getName(), actualFilm.getName());
        assertEquals(filmExpected.getDescription(), actualFilm.getDescription());
        assertEquals(filmExpected.getReleaseDate(), actualFilm.getReleaseDate());
        assertEquals(filmExpected.getMpa(), actualFilm.getMpa());
    }
}