package com.fe.pojo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENTS")
public class Student {    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "firstName", nullable = false, unique= false)
    private String firstName;

    @Column(name = "lastName")
    private String lastName;    
    
    @Column(name = "marks")
    private int marks;
    
    // One student can have many books (One-to-Many relationship)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Books> books = new ArrayList<>();

    public Student() {
    }

    public Student(String firstName, String lastName, int marks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.marks = marks;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }

    // Helper method to add a book to the student
    public void addBook(Books book) {
        if (books == null) {
            books = new ArrayList<>();
        }
        books.add(book);
        book.setStudent(this);
    }
    
    // Helper method to remove a book from the student
    public void removeBook(Books book) {
        books.remove(book);
        book.setStudent(null);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", marks=" + marks +
                '}';
    }
}

