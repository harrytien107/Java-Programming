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
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "first_name", nullable=false, unique=false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "marks")
    private int marks;
    
    // Many-to-Many relationship with Books
    // This side owns the relationship and defines the join table
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
        name = "STUDENT_BOOKS", // Join table name
        joinColumns = @JoinColumn(name = "student_id"), // Foreign key for Student
        inverseJoinColumns = @JoinColumn(name = "book_id") // Foreign key for Book
    )
    private Set<Book> books = new HashSet<Book>();
    
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public Student() {
        super();
    }
    
    public Student(String firstName, String lastName, int marks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.marks = marks;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
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
    }
    
    public int getMarks() {
        return marks;
    }
    
    public void setMarks(int marks) {
        this.marks = marks;
    }
    
    // Helper methods to manage the relationship
    public void addBook(Book book) {
        this.books.add(book);
        book.getStudents().add(this);
    }
    
    public void removeBook(Book book) {
        this.books.remove(book);
        book.getStudents().remove(this);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", marks=" + marks +
                ", booksCount=" + (books != null ? books.size() : 0) +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student that = (Student) o;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
} 