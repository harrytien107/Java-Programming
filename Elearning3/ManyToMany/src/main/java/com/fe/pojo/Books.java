package com.fe.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "BOOKS")
public class Books {    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "title", length = 30)
    private String title;
      private String author;
    
    private String isbn;
    
    // Many books can be associated with many students (Many-to-Many relationship)
    @ManyToMany(mappedBy = "books")
    private Set<Student> students = new HashSet<>();
    
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
    
    public Set<Student> getStudents() {
        return students;
    }
      public void setStudents(Set<Student> students) {
        this.students = students;
    }
    
    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", studentCount=" + (students != null ? students.size() : 0) +
                '}';
    }
}
