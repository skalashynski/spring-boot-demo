CREATE TABLE STUDENTS
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(250) NOT NULL,
    last_name  VARCHAR(250) NOT NULL,
    birthday   DATE      DEFAULT NULL,
    created_at TIMESTAMP DEFAULT NULL
);

CREATE TABLE APP_USERS
(
    id            SERIAL PRIMARY KEY,
    first_name    VARCHAR(250) NOT NULL,
    last_name     VARCHAR(250) NOT NULL,
    email         VARCHAR(250) NOT NULL,
    username      VARCHAR(250) NOT NULL,
    password      VARCHAR(250) NOT NULL,
    is_locked     BOOLEAN NULL,
    enabled       BOOLEAN NULL,
    app_user_role VARCHAR(10) DEFAULT 'STUDENT'
);

CREATE TABLE TOKENS_CONFIRMATION
(
    id           SERIAL PRIMARY KEY,
    token        VARCHAR(250) NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    expires_at   TIMESTAMP    NOT NULL,
    confirmed_at TIMESTAMP NULL,
    app_user_id  INT          NOT NULL
);
