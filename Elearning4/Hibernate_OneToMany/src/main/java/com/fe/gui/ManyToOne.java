package com.fe.gui;

import com.fe.pojo.Books;
import com.fe.pojo.Student;
import com.fe.service.IStudentService;
import com.fe.service.StudentService;

public class ManyToOne {
    public static void main(String[] args) {
        String fileName = "JPAs";        IStudentService studentService = new StudentService(fileName);
        Student student = new Student("Lam", "Nguyen", 9);
        Books book = new Books("Java Persistence with Spring", "Catalin Tudose", "9781617299186", 500, 49.99);
        student.addBook(book);
        studentService.save(student);
    }
}
