package com.fe.service;

import java.util.List;

import com.fe.pojo.Student;

public interface IStudentService {
    public List<Student> findAll();
    public void save(Student student);
    public Student findById(Long studentId);
    public void delete(Long studentId);
    public void update(Student student);
}
