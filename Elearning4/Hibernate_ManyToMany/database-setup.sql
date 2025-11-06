-- Database setup script for Many-to-Many Hibernate demo
-- Run this script in your MySQL database to create the required database

-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS manytomany_hibernate_db;

-- Use the database
USE manytomany_hibernate_db;

-- Create user if needed (optional - you can use root)
-- CREATE USER IF NOT EXISTS 'hibernate_user'@'localhost' IDENTIFIED BY '1066';
-- GRANT ALL PRIVILEGES ON manytomany_hibernate_db.* TO 'hibernate_user'@'localhost';

-- Note: The tables (STUDENTS, BOOKS, STUDENT_BOOKS) will be created automatically 
-- by Hibernate due to the hibernate.hbm2ddl.auto=update property

-- Verify connection
SELECT 'Database manytomany_hibernate_db is ready for Hibernate Many-to-Many demo!' as Status;