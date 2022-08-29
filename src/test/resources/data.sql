insert into STUDENTS (id, first_name, last_name, birthday, created_at)
values (1, 'Aliko', 'Dangote', '1997-03-17', '2021-12-12T01:02:03'),
       (2, 'Bill', 'Gates', '1955-12-31', '2021-12-12T01:02:03'),
       (3, 'Folrunsho', 'Alakija', '1997-12-31', '2021-12-12T01:02:03'),
       (4, 'Bill', 'Brant', '1988-03-02', '2021-12-12T01:02:03'),
       (5, 'Folrunsho', 'Ningbro', '1983-01-25', '2021-12-12T01:02:03'),
       (6, 'Folrunsho', 'Po', '1993-06-03', '2021-12-12T01:02:03');

insert into APP_USERS (id, first_name, last_name, email, username, password, is_locked, enabled)
values (1, 'Aliko', 'Dangote', 'dangote@gmail.com', 'gangote123', 'password1', false, true),
       (2, 'Aliko', 'Monster', 'monster@gmail.com', 'monster123', 'password2', false, true),
       (3, 'Aliko', 'Ambassador', 'ambassador@gmail.com', 'ambassador123', 'password3', false, true);

insert into TOKENS_CONFIRMATION (id, token, created_at, expires_at, confirmed_at, app_user_id)
values (1, 'AlikoDangotegangote123', now(), now(), now(), 1);

SELECT setval('students_id_seq', (SELECT MAX(id) from public.students));
