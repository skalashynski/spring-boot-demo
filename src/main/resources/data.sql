DROP TABLE IF EXISTS STUDENT;

CREATE TABLE STUDENT (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  birthday DATE DEFAULT NULL
);

INSERT INTO STUDENT (id, first_name, last_name, birthday) VALUES
(1, 'Aliko', 'Dangote', '1997-03-17'),
(2, 'Bill', 'Gates', '1955-12-31'),
(3, 'Folrunsho', 'Alakija', '1997-12-31');