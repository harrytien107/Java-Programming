package com.fe.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.fe.pojo.Student;

public class StudentDAO {
    
    private SessionFactory sessionFactory = null;
    private Configuration cf = null;

    public StudentDAO(String persistenceName) {
        cf = new Configuration();
        cf = cf.configure("hibernate.cfg.xml");
        sessionFactory = cf.buildSessionFactory();
    }

    public void save(Student student) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.save(student);
            t.commit();
            System.out.println("Student saved successfully");
        } catch (Exception ex) {
            t.rollback();
            System.out.println("Error saving student: " + ex.getMessage());
        } finally {
            session.close();
        }
    }    public List<Student> getStudents() {
        List<Student> students = null;
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            // Use LEFT JOIN FETCH to eagerly load books
            students = session.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.books", Student.class).getResultList();
            t.commit();
            System.out.println("Students retrieved successfully");
        } catch (Exception ex) {
            t.rollback();
            System.out.println("Error retrieving students: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return students;
    }

    public Student findByID(Long studentID) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(Student.class, studentID);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            session.close();
        }
    }

    public void delete(Long studentID) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Student student = session.get(Student.class, studentID);
            if (student != null) {
                session.delete(student);
                tx.commit();
                System.out.println("Student deleted successfully");
            } else {
                System.out.println("Student not found with ID: " + studentID);
                tx.rollback();
            }
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public void update(Student student) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.update(student);
            t.commit();
            System.out.println("Student updated successfully");
        } catch (Exception ex) {
            t.rollback();
            System.out.println("Error updating student: " + ex.getMessage());
        } finally {
            session.close();
        }
    }
}