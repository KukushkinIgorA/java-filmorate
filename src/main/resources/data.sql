--Заполнение таблицы жанров
INSERT INTO GENRE (name) VALUES ('Комедия') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (name) VALUES ('Драма') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (name) VALUES ('Мультфильм') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (name) VALUES ('Триллер') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (name) VALUES ('Документальный') ON CONFLICT DO NOTHING;
INSERT INTO GENRE (name) VALUES ('Боевик') ON CONFLICT DO NOTHING;

--Заполнение таблицы рейтингов
INSERT INTO RATING_MPA (name, description) VALUES ('G', 'у фильма нет возрастных ограничений') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('PG', 'детям рекомендуется смотреть фильм с родителями') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('PG-13', 'детям до 13 лет просмотр не желателен') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого') ON CONFLICT DO NOTHING;
INSERT INTO RATING_MPA (name, description) VALUES ('NC-17', 'лицам до 18 лет просмотр запрещён') ON CONFLICT DO NOTHING;