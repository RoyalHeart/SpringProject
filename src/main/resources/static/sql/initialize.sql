DROP TABLE book;
DROP TABLE user;
CREATE TABLE book(
    id serial unique,
    title varchar(255) not null,
    author varchar(255) not null,
    unique(title,author)
);
CREATE TABLE user_detail (
  id serial NOT NULL,
  username varchar(45) NOT NULL,
  user_password varchar(64) NOT NULL,
  user_role varchar(45) NOT NULL,
);

INSERT INTO book(title, author) VALUES ('Hello World', 'Tom');
INSERT INTO book(title, author) VALUES ('Spring Framework', 'Violet');
INSERT INTO book(title, author) VALUES ('Intro to CS', 'Timmy');
INSERT INTO book(title, author) VALUES ('Computer Network', 'Bella');
INSERT INTO user(username, user_password) VALUES ('admin', '$2a$12$Jt8ENKHcdh28mkizdJfEc.ekBTcRRRX9Cp3bz5Ze.dYnUHL3QbRmK');