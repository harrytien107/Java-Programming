package com.fe.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.fe.pojo.Student;

public class StudentDAO {
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private String persistenceName;

    public StudentDAO(String persistenceName) {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(persistenceName);
        }
    }
    public void save(Student student) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            em.persist(student);
            em.getTransaction().commit(); 
        } catch (Exception ex) {
            em.getTransaction().rollback();
            System.out.println("Error saving student: " + ex.getMessage());
        } finally {
            em.close();
        }   
    }

    public List<Student> getStudents() {
        List<Student> students = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            students = em.createQuery("from Student").getResultList();
        } catch (Exception ex) {
            System.out.println("Error retrieving students: " + ex.getMessage());
        } finally {
            em.close();
        }
        return students;
    }

    public Student findByID(Long studentID) {
        Student student = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            student = em.find(Student.class, studentID);
        } catch (Exception ex) {
            System.out.println("Error finding student by ID: " + ex.getMessage());
        } finally {
            em.close();
        }
        return student;
    }

    public void delete(Long studentID) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Student s = em.find(Student.class, studentID);
            em.remove(s);
            em.getTransaction().commit();
        } catch (Exception ex) {
            System.out.println("Error deleting student: " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public void update(Student student) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Student s = em.find(Student.class, student.getId());
            if (s != null) {
                s.setFirstName(student.getFirstName());
                s.setLastName(student.getLastName());
                s.setMarks(student.getMarks());
                em.getTransaction().commit();
            }
        } catch (Exception ex) {
            System.out.println("Error updating student: " + ex.getMessage());
        } finally {
            em.close();
        }
    }
}