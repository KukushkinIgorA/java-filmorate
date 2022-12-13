--Заполнение таблицы жанров
INSERT INTO GENRE (genre_id, name) VALUES (1, 'Комедия') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (genre_id, name) VALUES (2, 'Драма') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (genre_id, name) VALUES (3, 'Мультфильм') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (genre_id, name) VALUES (4, 'Триллер') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (genre_id, name) VALUES (5, 'Документальный') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (genre_id, name) VALUES (6, 'Боевик') ON CONFLICT DO NOTHING;

--Заполнение таблицы рейтингов
INSERT INTO RATING_MPA (name, description) VALUES ('G', 'у фильма нет возрастных ограничений') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('PG', 'детям рекомендуется смотреть фильм с родителями') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('PG-13', 'детям до 13 лет просмотр не желателен') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('NC-17', 'лицам до 18 лет просмотр запрещён') ON CONFLICT DO NOTHING;