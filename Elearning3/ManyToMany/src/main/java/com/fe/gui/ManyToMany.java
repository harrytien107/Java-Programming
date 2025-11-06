package com.fe.gui;

import java.util.List;

import com.fe.pojo.Books;
import com.fe.pojo.Student;
import com.fe.service.IStudentService;
import com.fe.service.StudentService;

public class ManyToMany {
    public static void main(String[] args) {
        String fileName = "JPAs";
        IStudentService studentService = new StudentService(fileName);
        
        // Create a student
        Student student = new Student("Lam", "Nguyen", 9);
        
        // Create a book
        Books book = new Books("Java Persistence with Spring", "Catalin Tudose", "9781617299186", 500, 49.99);
        
        // Add book to student (Many-to-Many relationship)
        student.addBook(book);
        
        // Save student (this will also save the book due to cascade)
        studentService.save(student);
        
        // Retrieve and display all students
        List<Student> students = studentService.findAll();
        for (Student st : students) {
            System.out.println("Student: " + st.getFirstName() + " " + st.getLastName() + 
                              ", Books: " + st.getBooks().size());
        }
    }
}
