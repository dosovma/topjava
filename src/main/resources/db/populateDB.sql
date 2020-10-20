DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (user_id, datetime, description, calories)
VALUES (100000, timestamp '2020-10-19 10:00', 'Завтрак', 500),
       (100000, timestamp '2020-10-19 13:00', 'Обед', 1000),
       (100000, timestamp '2020-10-19 20:00', 'Ужин', 500),
       (100000, timestamp '2020-10-20 00:00', 'Еда на граничное значение', 100),
       (100000, timestamp '2020-10-20 10:00', 'Завтрак', 1000),
       (100000, timestamp '2020-10-20 13:00', 'Обед', 500),
       (100000, timestamp '2020-10-20 20:00', 'Обед', 500);