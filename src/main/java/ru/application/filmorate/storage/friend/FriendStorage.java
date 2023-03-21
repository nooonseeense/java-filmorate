package ru.application.filmorate.storage.friend;

import ru.application.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    /**
     * Метод добавления пользователя в друзья
     *
     * @param id       id первого пользователя
     * @param friendId id второго пользователя
     */
    void addFriends(Integer id, Integer friendId);

    /**
     * Метод удаления пользователя из друзей
     *
     * @param id       id первого пользователя
     * @param friendId id второго пользователя
     */
    void removeFriends(Integer id, Integer friendId);

    /**
     * Метод получения общих друзей двух пользователей
     *
     * @param id      id первого пользователя
     * @param otherId id второго пользователя
     * @return Список общих друзей двух пользователей
     */
    List<User> getListOfFriendsSharedWithAnotherUser(Integer id, Integer otherId);

    /**
     * Метод получения списка друзей пользователя по id
     *
     * @param id id пользователя
     * @return список друзей пользователя
     */
    List<User> getListOfFriends(Integer id);
}
