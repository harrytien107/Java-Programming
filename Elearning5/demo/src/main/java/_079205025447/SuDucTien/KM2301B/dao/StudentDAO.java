package _079205025447.SuDucTien.KM2301B.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import _079205025447.SuDucTien.KM2301B.pojo.Student;
import java.util.List;

public class StudentDAO {
    private static EntityManager em;
    private static EntityManagerFactory emf;

    public StudentDAO(String persistanceName) {
        emf = Persistence.createEntityManagerFactory(persistanceName);
    }

    public void save(Student student) {
        try {
        em = emf.createEntityManager();
        em.getTransaction().begin();
            
            // Check if student with this ID already exists
            Student existingStudent = em.find(Student.class, student.getId());
            if (existingStudent == null) {
                // Insert new student with specified ID
                em.persist(student);
            } else {
                // Update existing student
                existingStudent.setFirstName(student.getFirstName());
                existingStudent.setLastName(student.getLastName());
                existingStudent.setMarks(student.getMarks());
            }
            
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
            }
            System.out.println("Error: " + ex.getMessage());
        } finally {
            if (em != null) {
            em.close();
            }
        }
    }

    public List<Student> getStudents() {
        List<Student> students = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            students = em.createQuery("from Student", Student.class).getResultList();
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error: " + ex.getMessage());
        } finally {
            if (em != null) {
            em.close();
            }
        }
        return students;
    }

    public void delete(int studentID) {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            Student s = em.find(Student.class, studentID);
            if (s != null) {
            em.remove(s);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error: " + ex.getMessage());
        } finally {
            if (em != null) {
            em.close();
            }
        }
    }

    public Student findById(int studentID) {
        Student student = null;
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
            student = em.find(Student.class, studentID);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error: " + ex.getMessage());
        } finally {
            if (em != null) {
            em.close();
            }
        }
        return student;
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
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error: " + ex.getMessage());
        } finally {
            if (em != null) {
            em.close();
            }
        }
    }
}
