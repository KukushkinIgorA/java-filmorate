drop table FILM_GENRE;

drop table FILM_LIKE;

drop table FILMS;

drop table GENRE;

drop table RATING_MPA;

drop table USER_FRIEND;

drop table USERS;

create table if not exists users
(
    user_id  integer generated always as identity
        constraint "USERS_pk"
            primary key,
    email    varchar(255) not null,
    login    varchar(32)  not null,
    name     varchar(32),
    birthday date
);

create table if not exists user_friend
(
    user_friend_id integer generated always as identity
        constraint user_friend_pk
            primary key,
    user_id        integer not null
        constraint user_friend_users_null_fk
            references users,
    friend_id      integer not null
        constraint user_friend_users_user_id_fk
            references users,
    confirm_flg    boolean default false
);

create table if not exists rating_mpa
(
    rating_mpa_id   integer generated always as identity
        constraint rating_pk
            primary key,
    name        varchar(8) not null  UNIQUE,
    description varchar(200)
);

create table if not exists films
(
    film_id      integer generated always as identity
        constraint films_pk
            primary key,
    name         varchar(128) not null,
    description  varchar(200),
    release_date date,
    duration     integer,
    rating       integer DEFAULT 0,
    rating_mpa_id    integer
        constraint films_rating_mpa_null_fk
            references rating_mpa
);

create table if not exists genre
(
    genre_id integer
        constraint genre_pk
            primary key,
    name     varchar(32) not null UNIQUE
);

create table if not exists film_genre
(
    film_genre_id integer generated always as identity
        constraint film_genre_pk
            primary key,
    film_id       integer not null
        constraint film_genre_films_null_fk
            references films,
    genre_id      integer not null
        constraint film_genre_genre_null_fk
            references genre,
    UNIQUE(film_id, genre_id)
);

create table if not exists film_like
(
    film_like_id integer generated always as identity
        constraint film_like_pk
            primary key,
    film_id      integer not null
        constraint film_like_films_null_fk
            references films,
    user_id      integer not null
        constraint film_like_users_null_fk
            references users
);