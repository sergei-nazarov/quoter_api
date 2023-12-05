SHOW
TABLES;
INSERT INTO USERS (EMAIL, PASSWORD, USERNAME, CREATED)
-- password for all users 123
values ('serega@gmail.com', '$2a$10$FleTHG6poyR1robPua7g6.NJSZlPTwtPyykwXnt4HLfnK8L5yv1p6', 'serega',
        '2021-12-01 14:30:15'),
       ('sveta@gmail.com', '$2a$10$FleTHG6poyR1robPua7g6.NJSZlPTwtPyykwXnt4HLfnK8L5yv1p6', 'sveta',
        '2023-11-15 12:30:15'),
       ('soprano@gmail.com', '$2a$10$FleTHG6poyR1robPua7g6.NJSZlPTwtPyykwXnt4HLfnK8L5yv1p6', 'soprano',
        '2023-12-01 17:30:15');
INSERT INTO ROLES (NAME)
values ('ROLE_USER'),
       ('ROLE_ADMIN');
INSERT INTO USERS_ROLES (USER_ID, ROLE_ID)
values (1, 1),
       (1, 2),
       (2, 1),
       (3, 1);
INSERT INTO QUOTES (AUTHOR_ID, CONTENT, CREATED)
values ('1', 'My first quote', '2021-12-01 14:30:15'),
       ('2', 'Sveta quote!!!', '2021-12-01 14:30:15'),
       ('3', 'Soprano quote!!!', '2022-12-01 14:30:15');


