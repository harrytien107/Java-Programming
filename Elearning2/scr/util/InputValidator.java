package scr.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^[\\+]?[1-9]?[0-9]{7,15}$");
    
    private static final Pattern NAME_PATTERN = 
        Pattern.compile("^[a-zA-Z\\s]{2,50}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidUserId(String userId) {
        return userId != null && userId.trim().length() >= 3 && userId.trim().length() <= 20;
    }

    public static boolean isValidPrice(String priceStr) {
        try {
            double price = Double.parseDouble(priceStr);
            return price >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidInteger(String intStr, int min, int max) {
        try {
            int value = Integer.parseInt(intStr);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidDateTime(String dateTimeStr) {
        try {
            LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidPercentage(String percentageStr) {
        try {
            double percentage = Double.parseDouble(percentageStr);
            return percentage >= 0 && percentage <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidMembershipType(String membershipType) {
        if (membershipType == null) return false;
        String type = membershipType.toLowerCase();
        return type.equals("monthly") || type.equals("quarterly") || type.equals("yearly");
    }

    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'&]", "");
    }

    public static boolean isValidChoice(String choice, int min, int max) {
        try {
            int choiceInt = Integer.parseInt(choice);
            return choiceInt >= min && choiceInt <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String formatValidationError(String field, String requirement) {
        return String.format("Invalid %s: %s", field, requirement);
    }

    // Validation messages
    public static final String INVALID_EMAIL = "Please enter a valid email address";
    public static final String INVALID_PHONE = "Please enter a valid phone number (7-15 digits)";
    public static final String INVALID_NAME = "Name must be 2-50 characters and contain only letters and spaces";
    public static final String INVALID_USER_ID = "User ID must be 3-20 characters long";
    public static final String INVALID_PRICE = "Price must be a positive number";
    public static final String INVALID_DATE = "Please enter date in YYYY-MM-DD format";
    public static final String INVALID_DATETIME = "Please enter date and time in YYYY-MM-DDTHH:MM:SS format";
    public static final String INVALID_PERCENTAGE = "Percentage must be between 0 and 100";
    public static final String INVALID_MEMBERSHIP_TYPE = "Membership type must be 'monthly', 'quarterly', or 'yearly'";
    public static final String EMPTY_FIELD = "This field cannot be empty";
} 