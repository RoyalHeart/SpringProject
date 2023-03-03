-- INSERT INTO BOOK(AUTHOR, TITLE) VALUES('TOM', 'HELLO WORLD!');
-- UPDATE BOOK SET AUTHOR = 'TOM' , TITLE = 'SAD', IMPORTED = GETDATE(), LIBRARYID = 1 WHERE ID = 924;
-- INSERT INTO library (name) VALUES ('Openlibrary');
-- INSERT INTO library (name) VALUES ('Gutendex');
-- INSERT INTO library (name) VALUES ('CrossRef');

-- ALTER TABLE book ADD libraryId bigint not null DEFAULT 1;
-- ALTER TABLE book ADD Constraint FK_BookLibrary FOREIGN KEY (libraryId) REFERENCES library(id);
-- ALTER TABLE book DROP Constraint FK_BookLibrary;
DROP TABLE BORROW;
CREATE TABLE borrow (
    userId BIGINT FOREIGN KEY REFERENCES user_detail(id),
    ibookId BIGINT FOREIGN KEY REFERENCES Book(id),
    CONSTRAINT PK_borrow PRIMARY KEY (userId,bookId)
);

INSERT INTO borrow (userId, bookId) values(1, 1);
INSERT INTO borrow (userId, bookId) values(1, 2);
INSERT INTO borrow (userId, bookId) values(1, 3);
INSERT INTO borrow (userId, bookId) values(1, 4);
INSERT INTO borrow (userId, bookId) values(1, 20);
INSERT INTO borrow (userId, bookId) values(1, 880);
INSERT INTO borrow (userId, bookId) values(1, 68);
INSERT INTO borrow (userId, bookId) values(2, 20);
INSERT INTO borrow (userId, bookId) values(2, 880);
INSERT INTO borrow (userId, bookId) values(2, 68);
INSERT INTO borrow (userId, bookId) values(2, 9);
INSERT INTO borrow (userId, bookId) values(2, 11);
INSERT INTO borrow (userId, bookId) values(2, 13);
INSERT INTO borrow (userId, bookId) values(2, 15);
INSERT INTO borrow (userId, bookId) values(2, 32);
INSERT INTO borrow (userId, bookId) values(2, 893);
INSERT INTO borrow (userId, bookId) values(2, 82);

SELECT u.username, b.author, b.title FROM Borrow bo
    JOIN book b on bookId = b.id
    JOIN user_detail u on u.id = userId
        WHERE userId = 2;

CREATE PROCEDURE get_borrower_name_book AS 
SELECT u.username, b.author, b.title FROM Borrow bo
    JOIN book b on bookId = b.id
    JOIN user_detail u on u.id = userId
GO;

drop PROCEDURE get_borrower_name_book;
EXEC get_borrower_name_book;

CREATE FUNCTION get_admin_borrow_book()
RETURNS TABLE 
AS
RETURN 
SELECT u.username, b.author, b.title FROM Borrow bo
    JOIN book b on bookId = b.id
    JOIN user_detail u on u.id = userId
        WHERE userId = 1;

CREATE FUNCTION get_year_diff(@published SMALLINT)
RETURNS SMALLINT
AS
BEGIN
    RETURN Year(GETDATE()) - @published
END;

SELECT dbo.get_year_diff(b.published) from book b;
SELECT avg(b.published),  max(b.published), min(b.published) from book b;

SELECT * FROM get_admin_borrow_book();

SELECT * FROM book where published < 300;

SELECT * FROM library;