package ru.application.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Friend {
    private int id;
    private int user1Id;
    private int user2Id;
}
