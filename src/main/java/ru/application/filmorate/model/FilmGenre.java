package ru.application.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilmGenre {
    private int id;
    private int filmId;
    private int genreId;
}
