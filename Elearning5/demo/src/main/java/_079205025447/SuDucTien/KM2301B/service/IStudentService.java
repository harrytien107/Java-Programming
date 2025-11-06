package _079205025447.SuDucTien.KM2301B.service;

import java.util.List;
import _079205025447.SuDucTien.KM2301B.pojo.Student;

public interface IStudentService {
    public List<Student> findAll();

    public void save(Student student);

    public void delete(int studentID);

    public Student findById(int studentID);

    public void update(Student student);
}
