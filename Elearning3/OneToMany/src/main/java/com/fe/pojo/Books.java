package com.fe.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "BOOKS")
public class Books {    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "title", length = 30)
    private String title;
    
    private String author;
    
    private String isbn;
    // Many books can belong to one student (Many-to-One relationship)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;
    
    // Default constructor
    public Books() {
        super();
    }
    
    // Constructor without student
    public Books(String title, String author, String isbn, int pages, double price) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
    
    // Constructor with student
    public Books(String title, String author, String isbn, int pages, double price, Student student) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.student = student;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", student=" + (student != null ? student.getFirstName() + " " + student.getLastName() : "null") +
                '}';
    }
}
