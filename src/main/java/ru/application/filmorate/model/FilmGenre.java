package ru.application.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FilmGenre {
    private int id;
    private int filmId;
    private int genreId;
}
