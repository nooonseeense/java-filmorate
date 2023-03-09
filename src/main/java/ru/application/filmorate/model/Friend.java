package ru.application.filmorate.model;

import lombok.Data;

@Data
public class Friend {
    private int id;
    private int user1Id;
    private int user2Id;
}
