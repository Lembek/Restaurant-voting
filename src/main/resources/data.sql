INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User', 'user@gmail.com', '{noop}password'),
       ('Admin', 'admin@javaops.ru', '{noop}admin');

INSERT INTO USER_ROLES (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME)
VALUES ('Restaurant1'),
       ('Restaurant2');

INSERT INTO DISH (NAME, PRICE, RESTAURANT_ID)
VALUES ('Fish', 100, 1),
       ('Soup', 50, 1),
       ('Steak', 90, 2);

INSERT INTO DISH (NAME, PRICE, LOCAL_DATE, RESTAURANT_ID)
VALUES ('Borch', 60, '2000-05-11', 1),
       ('Salad', 45, '2000-05-11', 1);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID)
VALUES (1, 1);

INSERT INTO VOTE (LOCAL_DATE, USER_ID, RESTAURANT_ID)
VALUES ('2000-05-11', 1, 1);