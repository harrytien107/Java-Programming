package _079205025447.SuDucTien.KM2301B.repository;

import java.util.List;
import _079205025447.SuDucTien.KM2301B.pojo.Student;

public interface IStudentRepository {
    public List<Student> findAll();

    public void save(Student student);

    public void delete(int studentID);

    public Student findById(int studentID);

    public void update(Student student);
}
