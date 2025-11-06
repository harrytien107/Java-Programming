-- =============================================
-- Many-to-Many Database Setup Script
-- Database: manytomany_db
-- Docker MySQL Container
-- Date: June 3, 2025
-- =============================================

-- Create the new database
CREATE DATABASE IF NOT EXISTS manytomany_db;
USE manytomany_db;

-- Drop tables if they exist (for clean recreation)
-- Must drop junction table first due to foreign key constraints
DROP TABLE IF EXISTS STUDENT_BOOKS;
DROP TABLE IF EXISTS BOOKS;
DROP TABLE IF EXISTS STUDENTS;

-- =============================================
-- Create STUDENTS table (Entity)
-- =============================================
CREATE TABLE STUDENTS (
    id INT NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255),
    marks INT,
    PRIMARY KEY (id)
);

-- =============================================
-- Create BOOKS table (Entity)
-- Note: No student_id column for Many-to-Many
-- =============================================
CREATE TABLE BOOKS (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255),
    author VARCHAR(255),
    isbn VARCHAR(255),
    pages INT,
    price DECIMAL(10,2),
    PRIMARY KEY (id)
);

-- =============================================
-- Create STUDENT_BOOKS junction table (Many-to-Many)
-- This is the key table for Many-to-Many relationship
-- =============================================
CREATE TABLE STUDENT_BOOKS (
    student_id INT NOT NULL,
    book_id BIGINT NOT NULL,
    PRIMARY KEY (student_id, book_id),
    CONSTRAINT fk_student_books_student 
        FOREIGN KEY (student_id) 
        REFERENCES STUDENTS(id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE,
    CONSTRAINT fk_student_books_book 
        FOREIGN KEY (book_id) 
        REFERENCES BOOKS(id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

-- =============================================
-- Insert sample data into STUDENTS table
-- =============================================
INSERT INTO STUDENTS (firstName, lastName, marks) VALUES
('Sang', 'Nguyen', 90),
('Tran', 'Thi Binh', 92),
('Le', 'Van Cuong', 78),
('Hoang', 'Van Em', 95),
('Vo', 'Thi Hoa', 82),
('Dang', 'Van Giang', 89),
('Bui', 'Thi Huong', 91),
('Ngo', 'Van Khoa', 87),
('Ly', 'Thi Linh', 93);

-- =============================================
-- Insert sample data into BOOKS table
-- =============================================
INSERT INTO BOOKS (title, author, isbn, pages, price) VALUES
('Java Persistence with Spring', 'Catalin Tudose', '9781617299186', 500, 49.99),
('Spring in Action', 'Craig Walls', '9781617294945', 520, 45.99),
('Effective Java', 'Joshua Bloch', '9780134685991', 416, 52.99),
('Clean Code', 'Robert C. Martin', '9780132350884', 464, 44.99),
('Design Patterns', 'Gang of Four', '9780201633610', 395, 54.99),
('Java: The Complete Reference', 'Herbert Schildt', '9781260440232', 1248, 59.99),
('Spring Boot in Action', 'Craig Walls', '9781617292545', 296, 41.99),
('Microservices Patterns', 'Chris Richardson', '9781617294549', 520, 49.99);

-- =============================================
-- Insert data into STUDENT_BOOKS junction table
-- Creating Many-to-Many relationships
-- =============================================
INSERT INTO STUDENT_BOOKS (student_id, book_id) VALUES
-- Sang Nguyen reads multiple books
(1, 1), (1, 2), (1, 3),
-- Tran Thi Binh reads different books
(2, 2), (2, 4), (2, 7),
-- Le Van Cuong reads books
(3, 1), (3, 6),
-- Hoang Van Em reads multiple books
(4, 3), (4, 4), (4, 5),
-- Vo Thi Hoa reads books
(5, 1), (5, 5), (5, 8),
-- Dang Van Giang reads books
(6, 2), (6, 6), (6, 7),
-- Bui Thi Huong reads books
(7, 3), (7, 8),
-- Ngo Van Khoa reads books
(8, 4), (8, 6),
-- Ly Thi Linh reads books
(9, 5), (9, 7), (9, 8);

-- =============================================
-- Verification Queries
-- =============================================

-- Show database and tables created
SELECT 'DATABASE AND TABLES CREATED SUCCESSFULLY!' as 'STATUS';
SELECT DATABASE() as 'CURRENT_DATABASE';

-- Show all students
SELECT 'STUDENTS:' as '';
SELECT * FROM STUDENTS ORDER BY id;

-- Show all books
SELECT 'BOOKS:' as '';
SELECT * FROM BOOKS ORDER BY id;

-- Show Many-to-Many relationships
SELECT 'MANY-TO-MANY RELATIONSHIPS:' as '';
SELECT 
    s.id as student_id,
    CONCAT(s.firstName, ' ', s.lastName) as student_name,
    COUNT(sb.book_id) as total_books,
    GROUP_CONCAT(b.title ORDER BY b.title SEPARATOR ' | ') as books
FROM STUDENTS s
LEFT JOIN STUDENT_BOOKS sb ON s.id = sb.student_id
LEFT JOIN BOOKS b ON sb.book_id = b.id
GROUP BY s.id, s.firstName, s.lastName
ORDER BY s.id;

-- Show table structures
SELECT 'TABLE STRUCTURES:' as '';
SHOW TABLES;
