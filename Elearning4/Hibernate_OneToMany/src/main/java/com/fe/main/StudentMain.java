package com.fe.main;

import java.util.List;
import java.util.Scanner;

import com.fe.dao.StudentDAO;
import com.fe.pojo.Student;

public class StudentMain {    public static void main(String[] args) {        System.out.println("------ Student Management System ---");
        System.out.println("1. Add Student");
        System.out.println("2. Delete Student");
        System.out.println("3. Update Student");
        System.out.println("4. Get a Student");
        System.out.println("5. Get All Students");
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
                        Student student = new Student("Lam", "Nguyen", 9);
                        studentDAO.save(student);
                        System.out.println("Student added successfully!");
                        break;
                    case 2:
                        System.out.print("Enter student ID to delete: ");                        try {
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
                        Student updateStudent = new Student("Sang", "Nguyen", 9);
                        updateStudent.setId(1); // Set the ID for update
                        studentDAO.update(updateStudent);
                        System.out.println("Student updated successfully!");
                        break;
                    case 4:
                        System.out.print("Enter student ID to retrieve: ");                        try {
                            int findId = console.nextInt();
                            console.nextLine(); // Consume newline
                            Student foundStudent = studentDAO.findByID((long) findId);
                            if (foundStudent != null) {
                                System.out.println("Student found: " + foundStudent.toString());
                            } else {
                                System.out.println("Student not found.");
                            }                        } catch (Exception e) {
                            System.out.println("Invalid ID. Please enter a valid number.");
                            console.nextLine(); // Clear invalid input
                        }
                        break;                    case 5:
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
}


