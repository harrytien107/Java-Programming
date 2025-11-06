package com.fe.main;

import java.util.List;
import java.util.Scanner;

import com.fe.dao.StudentDAO;
import com.fe.pojo.Student;
import com.fe.pojo.Book;

public class StudentMain {
    
    public static void main(String[] args) {
        System.out.println("------ Student-Book Management System (Many-to-Many Demo) ---");
        System.out.println("1. Add Student");
        System.out.println("2. Delete Student");
        System.out.println("3. Update Student");
        System.out.println("4. Get a Student");
        System.out.println("5. Get All Students");
        System.out.println("6. Demo Many-to-Many Relationship");
        System.out.println("0. Exit");
        System.out.println("------ End of Menu ---");

        int inputKey = -1;
        Scanner console = new Scanner(System.in);
        StudentDAO studentDAO = new StudentDAO("hibernate.cfg.xml"); // Create DAO once
        
        while (inputKey != 0) {
            System.out.print("Enter your choice: ");
            try {
                inputKey = console.nextInt();
                console.nextLine(); // Consume newline
                
                switch (inputKey) {
                    case 0:
                        System.out.println("Exiting the program.");
                        break;
                    case 1:
                        System.out.print("Enter first name: ");
                        String firstName = console.nextLine();
                        System.out.print("Enter last name: ");
                        String lastName = console.nextLine();
                        System.out.print("Enter marks: ");
                        int marks = console.nextInt();
                        console.nextLine(); // Consume newline
                        
                        Student student = new Student(firstName, lastName, marks);
                        studentDAO.save(student);
                        System.out.println("Student added successfully!");
                        break;
                    case 2:
                        System.out.print("Enter student ID to delete: ");
                        try {
                            int deleteId = console.nextInt();
                            console.nextLine(); // Consume newline
                            studentDAO.delete((long) deleteId);
                            System.out.println("Student deleted successfully!");
                        } catch (Exception e) {
                            System.out.println("Invalid ID. Please enter a valid number.");
                            console.nextLine(); // Clear invalid input
                        }
                        break;
                    case 3:
                        System.out.print("Enter student ID to update: ");
                        try {
                            int updateId = console.nextInt();
                            console.nextLine(); // Consume newline
                            System.out.print("Enter new first name: ");
                            String newFirstName = console.nextLine();
                            System.out.print("Enter new last name: ");
                            String newLastName = console.nextLine();
                            System.out.print("Enter new marks: ");
                            int newMarks = console.nextInt();
                            console.nextLine(); // Consume newline
                            
                            Student updateStudent = new Student(newFirstName, newLastName, newMarks);
                            updateStudent.setId((long) updateId);
                            studentDAO.update(updateStudent);
                            System.out.println("Student updated successfully!");
                        } catch (Exception e) {
                            System.out.println("Error updating student: " + e.getMessage());
                            console.nextLine(); // Clear invalid input
                        }
                        break;
                    case 4:
                        System.out.print("Enter student ID to retrieve: ");
                        try {
                            int findId = console.nextInt();
                            console.nextLine(); // Consume newline
                            Student foundStudent = studentDAO.findByID((long) findId);
                            if (foundStudent != null) {
                                System.out.println("Student found: " + foundStudent.toString());
                                if (foundStudent.getBooks() != null && !foundStudent.getBooks().isEmpty()) {
                                    System.out.println("Books enrolled:");
                                    for (Book book : foundStudent.getBooks()) {
                                        System.out.println("  - " + book.toString());
                                    }
                                } else {
                                    System.out.println("  No books enrolled.");
                                }
                            } else {
                                System.out.println("Student not found.");
                            }
                        } catch (Exception e) {
                            System.out.println("Invalid ID. Please enter a valid number.");
                            console.nextLine(); // Clear invalid input
                        }
                        break;
                    case 5:
                        System.out.println("=== All Students ===");
                        List<Student> allStudents = studentDAO.getStudents();
                        if (allStudents != null && !allStudents.isEmpty()) {
                            for (Student s : allStudents) {
                                System.out.println(s.toString());
                            }
                            System.out.println("Total students: " + allStudents.size());
                        } else {
                            System.out.println("No students found in the database.");
                        }
                        break;
                    case 6:
                        // Demo Many-to-Many relationship
                        demoManyToManyRelationship(studentDAO);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                console.nextLine(); // Clear invalid input
                inputKey = -1; // Continue loop
            }
        }
        
        console.close();
    }
    
    private static void demoManyToManyRelationship(StudentDAO studentDAO) {
        System.out.println("\n=== Demonstrating Many-to-Many Relationship ===");
        
        // Create students
        Student student1 = new Student("John", "Doe", 85);
        Student student2 = new Student("Jane", "Smith", 92);
        
        // Create books
        Book book1 = new Book("Java Programming", "James Gosling", "978-0123456789");
        Book book2 = new Book("Spring Framework", "Rod Johnson", "978-0987654321");
        Book book3 = new Book("Hibernate Guide", "Gavin King", "978-0456789123");
        
        // Establish Many-to-Many relationships
        student1.addBook(book1);
        student1.addBook(book2);
        
        student2.addBook(book1); // Same book for multiple students
        student2.addBook(book3);
        
        // Save students (books will be saved due to cascade)
        studentDAO.save(student1);
        studentDAO.save(student2);
        
        System.out.println("Created students with books:");
        System.out.println("Student 1: " + student1.getFirstName() + " " + student1.getLastName() + 
                          " enrolled in " + student1.getBooks().size() + " books");
        System.out.println("Student 2: " + student2.getFirstName() + " " + student2.getLastName() + 
                          " enrolled in " + student2.getBooks().size() + " books");
        
        System.out.println("\nBooks and their enrolled students:");
        for (Book book : student1.getBooks()) {
            System.out.println("Book: " + book.getTitle() + " has " + book.getStudents().size() + " students");
        }
        for (Book book : student2.getBooks()) {
            if (!student1.getBooks().contains(book)) {
                System.out.println("Book: " + book.getTitle() + " has " + book.getStudents().size() + " students");
            }
        }
        
        System.out.println("=== Many-to-Many Demo Complete ===\n");
    }
}


