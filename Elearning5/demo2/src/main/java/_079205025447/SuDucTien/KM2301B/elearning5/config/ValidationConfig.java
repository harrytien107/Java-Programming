package _079205025447.SuDucTien.KM2301B.elearning5.config;

public class ValidationConfig {
    
    // Student validation rules
    public static final int MIN_STUDENT_ID = 1;
    public static final int MAX_STUDENT_ID = 999999;
    
    public static final int MIN_MARKS = 0;
    public static final int MAX_MARKS = 10;
    
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 50;
    
    // Validation regex patterns
    public static final String NAME_PATTERN = "^[a-zA-ZÀ-ÿĂăÂâĐđÊêÔôÔơỨựỪừỬứÝýỴỵÞß\\s]+$";
    public static final String PHONE_PATTERN = "^[0-9]{10,11}$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    // Error messages for validation
    public static final String INVALID_ID = "Student ID must be between %d and %d";
    public static final String INVALID_MARKS = "Marks must be between %d and %d";
    public static final String INVALID_NAME = "Name must contain only letters and be between %d and %d characters";
    public static final String INVALID_PHONE = "Phone number must be 10-11 digits";
    public static final String INVALID_EMAIL = "Email format is invalid";
    
    // Grade categories (thang điểm 10)
    public static final int EXCELLENT_THRESHOLD = 9;
    public static final int GOOD_THRESHOLD = 7;
    public static final int AVERAGE_THRESHOLD = 5;
    
    public static final String GRADE_EXCELLENT = "Excellent";
    public static final String GRADE_GOOD = "Good";
    public static final String GRADE_AVERAGE = "Average";
    public static final String GRADE_POOR = "Poor";
    
    private ValidationConfig() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Validate student ID
     */
    public static boolean isValidStudentId(int id) {
        return id >= MIN_STUDENT_ID && id <= MAX_STUDENT_ID;
    }
    
    /**
     * Validate marks
     */
    public static boolean isValidMarks(double marks) {
        return marks >= MIN_MARKS && marks <= MAX_MARKS;
    }
    
    /**
     * Validate name
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmedName = name.trim();
        return trimmedName.length() >= MIN_NAME_LENGTH && 
               trimmedName.length() <= MAX_NAME_LENGTH &&
               trimmedName.matches(NAME_PATTERN);
    }
    
    /**
     * Get grade category based on marks (thang điểm 10)
     */
    public static String getGradeCategory(double marks) {
        if (marks >= EXCELLENT_THRESHOLD) {
            return GRADE_EXCELLENT;
        } else if (marks >= GOOD_THRESHOLD) {
            return GRADE_GOOD;
        } else if (marks >= AVERAGE_THRESHOLD) {
            return GRADE_AVERAGE;
        } else {
            return GRADE_POOR;
        }
    }
    
    /**
     * Get CSS class for grade badge (thang điểm 10)
     */
    public static String getGradeCssClass(double marks) {
        if (marks >= EXCELLENT_THRESHOLD) {
            return "bg-success";
        } else if (marks >= GOOD_THRESHOLD) {
            return "bg-info";
        } else if (marks >= AVERAGE_THRESHOLD) {
            return "bg-warning";
        } else {
            return "bg-danger";
        }
    }
} 