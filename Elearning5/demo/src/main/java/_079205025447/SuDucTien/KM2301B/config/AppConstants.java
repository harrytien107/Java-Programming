package _079205025447.SuDucTien.KM2301B.config;

public class AppConstants {
    
    // Database Configuration
    public static final String PERSISTENCE_UNIT_NAME = "JPAs";
    public static final String DATABASE_NAME = "elearning5";
    
    // View Names
    public static final String HOME_VIEW = "home";
    public static final String ERROR_VIEW = "error";
    
    // Request Parameters
    public static final String PARAM_MANAGE_STUDENT = "btnManageStudent";
    public static final String PARAM_ID = "txtID";
    public static final String PARAM_ORIGINAL_ID = "originalID";
    public static final String PARAM_FIRST_NAME = "txtFirstName";
    public static final String PARAM_LAST_NAME = "txtLastName";
    public static final String PARAM_MARKS = "txtMarks";
    
    // Action Types
    public static final String ACTION_ADD = "Add";
    public static final String ACTION_UPDATE = "Update";
    public static final String ACTION_DELETE = "Delete";
    
    // Model Attributes
    public static final String ATTR_STUDENT_LIST = "studentList";
    
    // Error Messages
    public static final String ERROR_ID_EMPTY = "ID cannot be empty";
    public static final String ERROR_SELECT_STUDENT = "Please select a student to update";
    public static final String ERROR_ID_EXISTS = "Student with ID %d already exists";
    
    // Redirect URLs
    public static final String REDIRECT_HOME = "redirect:/";
    public static final String REDIRECT_HOME_ERROR = "redirect:/?error=%s";
    
    // Success Messages
    public static final String SUCCESS_STUDENT_ADDED = "Student added successfully";
    public static final String SUCCESS_STUDENT_UPDATED = "Student updated successfully";
    public static final String SUCCESS_STUDENT_DELETED = "Student deleted successfully";
    
    private AppConstants() {
        // Private constructor to prevent instantiation
    }
} 