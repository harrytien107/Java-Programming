package com.fe.repository;

import java.util.List;

import com.fe.dao.StudentDAO;
import com.fe.pojo.Student;

public class StudentRepository implements IStudentRepository {
    private StudentDAO studentDAO = null;

    public StudentRepository(String fileConfig) {
        studentDAO = new StudentDAO(fileConfig);
    }
    @Override
    public void save(Student student) {
        studentDAO.save(student);
    }
    @Override
    public List<Student> findAll() {
        return studentDAO.getStudents();
    }
    @Override
    public Student findById(Long studentId) {
        return studentDAO.findByID(studentId);
    }
    @Override
    public void delete(Long studentId) {
        studentDAO.delete(studentId);
    }
    @Override
    public void update(Student student) {
        studentDAO.update(student);
    }
    
}
