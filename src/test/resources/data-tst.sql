INSERT INTO GENRE(id, name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');

INSERT INTO MPA(id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');

INSERT INTO USERS (email, login, name, birthday)
VALUES  ('test@ya.com', 'testy', 'Tree', DATE '2021-10-10'),
        ('test@gu.com', 'testg', 'Bree', DATE '2020-10-10'),
        ('test@mom.ru', 'testm', 'Dree', DATE '2019-10-10');

INSERT INTO FRIEND (user1_id, user2_id)
VALUES (1,2),
       (1,3),
       (2,3);

INSERT INTO FILM (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA)
VALUES  ('Film1', 'D1',  DATE '2004-12-27', 100, 1),
        ('Film2', 'D2',  DATE '2005-12-28', 120, 2),
        ('Film3', 'D3',  DATE '2006-12-29', 130, 3);

INSERT INTO LIKE_FILM (FILM_ID, USER_ID)
VALUES (1,2),
       (1,3),
       (2,3);