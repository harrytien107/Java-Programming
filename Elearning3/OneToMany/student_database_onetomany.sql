-- =============================================
-- Student Database Complete SQL Script
-- JPA One-to-Many Relationship Project
-- Date: June 3, 2025
-- =============================================

-- Create database
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- Drop tables if they exist (for clean recreation)
DROP TABLE IF EXISTS BOOKS;
DROP TABLE IF EXISTS STUDENTS;

-- =============================================
-- Create STUDENTS table (Parent Entity)
-- =============================================
CREATE TABLE STUDENTS (
    id INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255),
    marks INT,
    PRIMARY KEY (id)
);

-- =============================================
-- Create BOOKS table (Child Entity)
-- =============================================
CREATE TABLE BOOKS (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(30),
    author VARCHAR(255),
    isbn VARCHAR(255),
    student_id INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_books_student 
        FOREIGN KEY (student_id) 
        REFERENCES STUDENTS(id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

-- =============================================
-- Insert data into STUDENTS table
-- =============================================
INSERT INTO STUDENTS (id, firstName, lastName, marks) VALUES
(1, 'Sang', 'Nguyen', 9),
(2, 'Tran', 'Thi Binh', 92),
(3, 'Le', 'Van Cuong', 78),
(5, 'Hoang', 'Van Em', 95),
(6, 'Vo', 'Thi Hoa', 82),
(7, 'Dang', 'Van Giang', 89),
(8, 'Bui', 'Thi Huong', 91),
(9, 'Ngo', 'Van Khoa', 87),
(10, 'Ly', 'Thi Linh', 93),
(11, 'Lam', 'Nguyen', 9),
(19, 'Lam', 'Nguyen', 9);

-- =============================================
-- Insert data into BOOKS table
-- =============================================
INSERT INTO BOOKS (id, title, author, isbn, student_id) VALUES
(1, 'Java Persistence with Spring', 'Catalin Tudose', '9781617299186', 16),
(3, 'Java Persistence with Spring', 'Catalin Tudose', '9781617299186', 18),
(4, 'Java Persistence with Spring', 'Catalin Tudose', '9781617299186', 19);

-- =============================================
-- Reset AUTO_INCREMENT for proper sequence
-- =============================================
ALTER TABLE STUDENTS AUTO_INCREMENT = 20;
ALTER TABLE BOOKS AUTO_INCREMENT = 5;

-- =============================================
-- Verification Queries
-- =============================================

-- Show all students
SELECT 'STUDENTS TABLE:' as '';
SELECT * FROM STUDENTS ORDER BY id;

-- Show all books with student information
SELECT 'BOOKS WITH STUDENT INFO:' as '';
SELECT 
    b.id as book_id,
    b.title,
    b.author,
    b.isbn,
    b.student_id,
    CONCAT(s.firstName, ' ', s.lastName) as student_name,
    s.marks
FROM BOOKS b
LEFT JOIN STUDENTS s ON b.student_id = s.id
ORDER BY b.id;

-- Show students with their books (One-to-Many relationship)
SELECT 'ONE-TO-MANY RELATIONSHIP:' as '';
SELECT 
    s.id as student_id,
    CONCAT(s.firstName, ' ', s.lastName) as student_name,
    s.marks,
    COUNT(b.id) as total_books,
    GROUP_CONCAT(b.title SEPARATOR ', ') as books
FROM STUDENTS s
LEFT JOIN BOOKS b ON s.id = b.student_id
GROUP BY s.id, s.firstName, s.lastName, s.marks
ORDER BY s.id;

-- Show table structures
SELECT 'STUDENTS TABLE STRUCTURE:' as '';
DESCRIBE STUDENTS;

SELECT 'BOOKS TABLE STRUCTURE:' as '';
DESCRIBE BOOKS;

