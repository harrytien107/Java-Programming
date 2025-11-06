package _079205025447.SuDucTien.KM2301B.elearning5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import _079205025447.SuDucTien.KM2301B.elearning5.config.AppConstants;
import _079205025447.SuDucTien.KM2301B.elearning5.service.IStudentService;
import _079205025447.SuDucTien.KM2301B.elearning5.service.StudentService;
import _079205025447.SuDucTien.KM2301B.elearning5.pojo.Student;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {
    private IStudentService iStudentService;

    public HomeController() {
        iStudentService = new StudentService(AppConstants.PERSISTENCE_UNIT_NAME);
    }

    @RequestMapping(value = "/")
    public ModelAndView showStudent(HttpServletRequest request) throws IOException {
        ModelAndView modelAndView = new ModelAndView(AppConstants.HOME_VIEW);
        List<Student> students = iStudentService.findAll();
        modelAndView.addObject(AppConstants.ATTR_STUDENT_LIST, students);
        return modelAndView;
    }

    @PostMapping(value = "/manageStudent")
    public String manageStudent(HttpServletRequest request) throws IOException {
        System.out.println("=== manageStudent called ===");
        String type = request.getParameter(AppConstants.PARAM_MANAGE_STUDENT);
        System.out.println("Action type: " + type);
        
        // Print all parameters for debugging
        System.out.println("All parameters:");
        request.getParameterMap().forEach((key, values) -> {
            System.out.println("  " + key + " = " + String.join(", ", values));
        });
        
        if (type != null) {
            switch (type) {
                case AppConstants.ACTION_ADD:
                    System.out.println("Calling handleAddStudent");
                    return handleAddStudent(request);
                case AppConstants.ACTION_UPDATE:
                    System.out.println("Calling handleUpdateStudent");
                    return handleUpdateStudent(request);
                case AppConstants.ACTION_DELETE:
                    System.out.println("Calling handleDeleteStudent");
                    return handleDeleteStudent(request);
                default:
                    System.out.println("Unknown action type: " + type);
            }
        } else {
            System.out.println("No action type found");
        }
        return AppConstants.REDIRECT_HOME;
    }

    private String handleAddStudent(HttpServletRequest request) {
        try {
            String idParam = request.getParameter(AppConstants.PARAM_ID);
            String firstName = request.getParameter(AppConstants.PARAM_FIRST_NAME);
            String lastName = request.getParameter(AppConstants.PARAM_LAST_NAME);
            String marksParam = request.getParameter(AppConstants.PARAM_MARKS);
            
            System.out.println("Add Student - ID: " + idParam + ", FirstName: " + firstName + 
                             ", LastName: " + lastName + ", Marks: " + marksParam);
            
            if (idParam == null || idParam.trim().isEmpty()) {
                System.out.println("Error: ID is empty");
                return String.format(AppConstants.REDIRECT_HOME_ERROR, AppConstants.ERROR_ID_EMPTY);
            }
            
            if (firstName == null || firstName.trim().isEmpty()) {
                System.out.println("Error: First name is empty");
                return AppConstants.REDIRECT_HOME;
            }
            
            if (lastName == null || lastName.trim().isEmpty()) {
                System.out.println("Error: Last name is empty");
                return AppConstants.REDIRECT_HOME;
            }
            
            if (marksParam == null || marksParam.trim().isEmpty()) {
                System.out.println("Error: Marks is empty");
                return AppConstants.REDIRECT_HOME;
            }
            
            int addId = Integer.parseInt(idParam);
            double marks = Double.parseDouble(marksParam);
            
            // Check if student with this ID already exists
            Student existingStudent = iStudentService.findById(addId);
            if (existingStudent != null) {
                System.out.println("Error: Student with ID " + addId + " already exists");
                return String.format(AppConstants.REDIRECT_HOME_ERROR, 
                    String.format(AppConstants.ERROR_ID_EXISTS, addId));
            }
            
            Student student = new Student(firstName, lastName, marks);
            student.setId(addId);
            iStudentService.save(student);
            
            System.out.println("Student added successfully: " + student);
            return AppConstants.REDIRECT_HOME;
            
        } catch (NumberFormatException e) {
            System.out.println("Error parsing numbers: " + e.getMessage());
            return AppConstants.REDIRECT_HOME;
        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
            e.printStackTrace();
            return AppConstants.REDIRECT_HOME;
        }
    }

    private String handleUpdateStudent(HttpServletRequest request) {
        String originalIdParam = request.getParameter(AppConstants.PARAM_ORIGINAL_ID);
        String newIdParam = request.getParameter(AppConstants.PARAM_ID);
        
        if (originalIdParam == null || originalIdParam.trim().isEmpty()) {
            return String.format(AppConstants.REDIRECT_HOME_ERROR, AppConstants.ERROR_SELECT_STUDENT);
        }
        
        int originalId = Integer.parseInt(originalIdParam);
        int newId = Integer.parseInt(newIdParam);
        
        String updateFirstName = request.getParameter(AppConstants.PARAM_FIRST_NAME);
        String updateLastName = request.getParameter(AppConstants.PARAM_LAST_NAME);
        double updateMarks = Double.parseDouble(request.getParameter(AppConstants.PARAM_MARKS));
        
        // If ID is changing, we need to handle it specially
        if (originalId != newId) {
            // Check if new ID already exists
            Student existingStudent = iStudentService.findById(newId);
            if (existingStudent != null) {
                return String.format(AppConstants.REDIRECT_HOME_ERROR, 
                    String.format(AppConstants.ERROR_ID_EXISTS, newId));
            }
            
            // Delete old record and create new one with new ID
            iStudentService.delete(originalId);
            Student newStudent = new Student(updateFirstName, updateLastName, updateMarks);
            newStudent.setId(newId);
            iStudentService.save(newStudent);
        } else {
            // Same ID, just update fields
            Student updateStudent = new Student(updateFirstName, updateLastName, updateMarks);
            updateStudent.setId(newId);
            iStudentService.update(updateStudent);
        }
        
        return AppConstants.REDIRECT_HOME;
    }

    private String handleDeleteStudent(HttpServletRequest request) {
        int studentID = Integer.parseInt(request.getParameter(AppConstants.PARAM_ID));
        iStudentService.delete(studentID);
        return AppConstants.REDIRECT_HOME;
    }
}
