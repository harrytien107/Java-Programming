package _079205025447.SuDucTien.KM2301B.service;

import java.util.List;
import _079205025447.SuDucTien.KM2301B.pojo.Student;
import _079205025447.SuDucTien.KM2301B.repository.IStudentRepository;
import _079205025447.SuDucTien.KM2301B.repository.StudentRepository;

public class StudentService implements IStudentService {
    private IStudentRepository iStudentRepo = null;

    public StudentService(String fileName) {
        iStudentRepo = new StudentRepository(fileName);
    }

    @Override
    public void save(Student student) {
        iStudentRepo.save(student);
    }

    @Override
    public List<Student> findAll() {
        return iStudentRepo.findAll();
    }

    @Override
    public void delete(int studentID) {
        iStudentRepo.delete(studentID);
    }

    @Override
    public Student findById(int studentID) {
        return iStudentRepo.findById(studentID);
    }

    @Override
    public void update(Student student) {
        iStudentRepo.update(student);
    }

}
