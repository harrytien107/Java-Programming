package com.fe.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
    
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "STUDENT_BOOKS",
                joinColumns = @JoinColumn(name = "student_id"),
                inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Books> books = new HashSet<>();

    public Set<Books> getBooks() {
        return books;
    }
    public void setBooks(Set<Books> books) {
        this.books = books;
    }
        
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
    }    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    // Helper method to add a book to the student (Many-to-Many)
    public void addBook(Books book) {
        if (books == null) {
            books = new HashSet<>();
        }
        books.add(book);
        book.getStudents().add(this);
    }
    
    // Helper method to remove a book from the student (Many-to-Many)
    public void removeBook(Books book) {
        books.remove(book);
        book.getStudents().remove(this);
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

