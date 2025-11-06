package com.fe.repository;

import java.util.List;

import com.fe.pojo.Student;

public interface IStudentRepository {
    public List<Student> findAll();
    public void save(Student student);
    public Student findById(Long studentId);
    public void delete(Long studentId);
    public void update(Student student);
}
