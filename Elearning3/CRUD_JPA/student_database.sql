-- ============================================
-- Student Database SQL Script
-- Created: June 2, 2025
-- Database: student_db
-- Description: Complete SQL script to recreate the student database
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

-- Drop table if exists (for fresh start)
DROP TABLE IF EXISTS STUDENTS;

-- Create STUDENTS table
CREATE TABLE STUDENTS (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255),
    marks INT,
    INDEX idx_firstName (firstName),
    INDEX idx_lastName (lastName),
    INDEX idx_marks (marks)
);

-- Insert sample Vietnamese students
INSERT INTO STUDENTS (firstName, lastName, marks) VALUES 
('Nguyen', 'Van An', 85),
('Tran', 'Thi Binh', 92),
('Le', 'Van Cuong', 78),
('Pham', 'Thi Duyen', 88),
('Hoang', 'Van Em', 95),
('Vo', 'Thi Hoa', 82),
('Dang', 'Van Giang', 89),
('Bui', 'Thi Huong', 91),
('Ngo', 'Van Khoa', 87),
('Ly', 'Thi Linh', 93);

-- Additional test students (added through application)
INSERT INTO STUDENTS (firstName, lastName, marks) VALUES 
('Lam', 'Nguyen', 9),
('Sang', 'Nguyen', 9);

