package ru.application.filmorate.model;

import lombok.Data;

@Data
public class LikeFilm {
    private int id;
    private int filmId;
    private long userId;
}
