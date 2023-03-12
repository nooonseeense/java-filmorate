drop table if exists FILM cascade;
drop table if exists GENRE cascade;
drop table if exists MPA cascade;
drop table if exists LIKE_FILM cascade;
drop table if exists USERS cascade;
drop table if exists FILM_GENRE cascade;
drop table if exists FRIEND cascade;

CREATE TABLE IF NOT EXISTS GENRE (
    ID   INTEGER NOT NULL,
    NAME CHARACTER VARYING(50),
    constraint GENRE_PK
        primary key (ID)
);

CREATE UNIQUE INDEX IF NOT EXISTS GENRE_ID_UINDEX
    on GENRE (ID);

CREATE TABLE IF NOT EXISTS MPA (
    ID   INTEGER               NOT NULL,
    NAME CHARACTER VARYING(50) NOT NULL,
    constraint MPA_PK
        primary key (ID)
);

CREATE TABLE IF NOT EXISTS FILM (
    ID           INTEGER               auto_increment,
    NAME         CHARACTER VARYING(50) NOT NULL,
    DESCRIPTION  CHARACTER VARYING(255),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    MPA          INTEGER,
    RATING       INTEGER DEFAULT 0,
    constraint FILM_PK
        primary key (ID),
    constraint MPA_FK
        foreign key (MPA) references MPA
            on update set null on delete set null
);

CREATE UNIQUE INDEX IF NOT EXISTS FILM_ID_UINDEX
    on FILM (ID);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
    ID       INTEGER NOT NULL auto_increment,
    FILM_ID  INTEGER NOT NULL,
    GENRE_ID INTEGER NOT NULL,
    constraint FILM_GENRE_PK
        primary key (ID),
    constraint FILM_FK
        foreign key (FILM_ID) references FILM
            on update cascade on delete cascade,
    constraint GENRE_FK
        foreign key (GENRE_ID) references GENRE
            on update cascade on delete cascade
);

CREATE UNIQUE INDEX IF NOT EXISTS FILM_GENRE_ID_UINDEX
    on FILM_GENRE (FILM_ID, GENRE_ID);

CREATE UNIQUE INDEX IF NOT EXISTS MPA_ID_UINDEX
    on MPA (ID);

CREATE TABLE IF NOT EXISTS USERS (
    ID       INTEGER               auto_increment,
    EMAIL    CHARACTER VARYING(50) NOT NULL,
    LOGIN    CHARACTER VARYING(50) NOT NULL,
    NAME     CHARACTER VARYING(50),
    BIRTHDAY DATE,
    constraint USERS_PK
        primary key (ID)
);

create unique index if not exists USER_EMAIL_UINDEX on USERS (email);
create unique index if not exists USER_LOGIN_UINDEX on USERS (login);

CREATE TABLE IF NOT EXISTS FRIEND (
    ID       INTEGER auto_increment,
    USER1_ID INTEGER NOT NULL,
    USER2_ID INTEGER NOT NULL,
    constraint FRIEND_PK
        primary key (ID),
    constraint FRIEND_USERS1_FK
        foreign key (USER1_ID) references USERS
            on update cascade on delete cascade,
    constraint FRIEND_USERS2_FK
        foreign key (USER2_ID) references USERS
            on update cascade on delete cascade
);

CREATE UNIQUE INDEX IF NOT EXISTS FRIEND_ID_UINDEX
    on FRIEND (USER1_ID, USER2_ID);

CREATE TABLE IF NOT EXISTS LIKE_FILM (
    ID      INTEGER auto_increment,
    FILM_ID INTEGER NOT NULL,
    USER_ID INTEGER NOT NULL,
    constraint LIKE_FILM_PK
        primary key (ID),
    constraint LIKE_FILM_FILM_FK
        foreign key (FILM_ID) references FILM
            on update cascade on delete cascade,
    constraint LIKE_FILM_USERS_FK
        foreign key (USER_ID) references USERS
            on update cascade on delete cascade
);

CREATE UNIQUE INDEX IF NOT EXISTS LIKE_FILM_ID_UINDEX
    on LIKE_FILM (ID);

CREATE UNIQUE INDEX IF NOT EXISTS USERS_ID_UINDEX
    on USERS (ID);