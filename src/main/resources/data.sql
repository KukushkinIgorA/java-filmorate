-- --Заполнение таблицы жанров
-- INSERT INTO GENRE (GENRE_ID, NAME) VALUES (1, 'Комедия') ON CONFLICT DO NOTHING;
-- INSERT INTO GENRE (GENRE_ID, NAME) VALUES (2, 'Драма') ON CONFLICT DO NOTHING;
-- INSERT INTO GENRE (GENRE_ID, NAME) VALUES (3, 'Мультфильм') ON CONFLICT DO NOTHING;
-- INSERT INTO GENRE (GENRE_ID, NAME) VALUES (4, 'Триллер') ON CONFLICT DO NOTHING;
-- INSERT INTO GENRE (GENRE_ID, NAME) VALUES (5, 'Документальный') ON CONFLICT DO NOTHING;
-- INSERT INTO GENRE (GENRE_ID, NAME) VALUES (6, 'Боевик') ON CONFLICT DO NOTHING;
--
-- --Заполнение таблицы рейтингов
-- INSERT INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES (1, 'G', 'у фильма нет возрастных ограничений') ON CONFLICT DO NOTHING;
-- INSERT INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES (2, 'PG', 'детям рекомендуется смотреть фильм с родителями') ON CONFLICT DO NOTHING;
-- INSERT INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES (3, 'PG-13', 'детям до 13 лет просмотр не желателен') ON CONFLICT DO NOTHING;
-- INSERT INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого') ON CONFLICT DO NOTHING;
-- INSERT INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION) VALUES (5, 'NC-17', 'лицам до 18 лет просмотр запрещён') ON CONFLICT DO NOTHING;

MERGE INTO RATING_MPA (RATING_MPA_ID, NAME, DESCRIPTION)
    VALUES (1, 'G', 'У фильма нет возрастных ограничений'),
           (2, 'PG', 'Детям рекомендуется смотреть фильм с родителями'),
           (3, 'PG-13', 'Детям до 13 лет просмотр не желателен'),
           (4, 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии'),
           (5, 'NC-17', 'Лицам до 18 лет просмотр запрещён');

MERGE INTO GENRE (GENRE_ID, NAME)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');