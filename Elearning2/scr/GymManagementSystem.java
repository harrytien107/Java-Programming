package scr;

import scr.entity.*;
import scr.manager.*;
import scr.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class GymManagementSystem {
    private UserManager userManager;
    private WorkoutManager workoutManager;
    private AttendanceManager attendanceManager;
    private ReportManager reportManager;
    private Scanner scanner;
    private User currentUser;

    public GymManagementSystem() {
        // Initialize data directory
        DataPersistence.initializeDataDirectory();
        
        // Initialize managers
        this.userManager = new UserManager();
        this.workoutManager = new WorkoutManager(userManager);
        this.attendanceManager = new AttendanceManager(userManager);
        this.reportManager = new ReportManager(userManager, attendanceManager, workoutManager);
        this.scanner = new Scanner(System.in);
        
        // Create default admin if none exists
        createDefaultAdminIfNeeded();
    }

    public void start() {
        System.out.println("Welcome to Gym Management System");
        System.out.println("=================================");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n=== LOGIN MENU ===");
        System.out.println("1. Login");
        System.out.println("2. Register as Member");
        System.out.println("3. Exit");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                registerMember();
                break;
            case "3":
                System.out.println("Thank you for using Gym Management System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

    private void login() {
        System.out.print("Enter User ID: ");
        String userId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Enter Password: ");
        String password = InputValidator.sanitizeInput(scanner.nextLine());
        
        currentUser = userManager.login(userId, password);
        
        if (currentUser == null) {
            System.out.println("Login failed! Invalid credentials or inactive account.");
        } else {
            System.out.println("Login successful! Welcome, " + currentUser.getName());
            System.out.println("Role: " + currentUser.getRole());
        }
    }

    private void registerMember() {
        System.out.println("\n=== MEMBER REGISTRATION ===");
        
        System.out.print("Enter User ID: ");
        String userId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Enter Name: ");
        String name = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Enter Email: ");
        String email = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Enter Phone: ");
        String phone = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Enter Password: ");
        String password = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.println("\nAvailable Membership Types:");
        System.out.println("1. monthly");
        System.out.println("2. quarterly");
        System.out.println("3. yearly");
        System.out.print("Choose membership type: ");
        String membershipChoice = scanner.nextLine().trim();
        
        String membershipType;
        switch (membershipChoice) {
            case "1": membershipType = "monthly"; break;
            case "2": membershipType = "quarterly"; break;
            case "3": membershipType = "yearly"; break;
            default:
                System.out.println("Invalid choice! Defaulting to monthly.");
                membershipType = "monthly";
        }
        
        if (userManager.registerMember(userId, name, email, phone, password, membershipType)) {
            System.out.println("Registration successful! You can now login with your credentials.");
        } else {
            System.out.println("Registration failed! Please check your input and try again.");
        }
    }

    private void showMainMenu() {
        switch (currentUser.getRole()) {
            case ADMIN:
                showAdminMenu();
                break;
            case TRAINER:
                showTrainerMenu();
                break;
            case MEMBER:
                showMemberMenu();
                break;
        }
    }

    // ADMIN MENU SYSTEM
    private void showAdminMenu() {
        System.out.println("\n=== ADMIN DASHBOARD ===");
        System.out.println("1. Dashboard");
        System.out.println("2. Member Management");
        System.out.println("3. Trainer Management");
        System.out.println("4. Attendance Management");
        System.out.println("5. Reports & Analytics");
        System.out.println("6. Subscription Plans");
        System.out.println("7. Workout Schedules");
        System.out.println("8. Logout");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": reportManager.displayDashboard(); break;
            case "2": showMemberManagementMenu(); break;
            case "3": showTrainerManagementMenu(); break;
            case "4": showAttendanceManagementMenu(); break;
            case "5": showReportsMenu(); break;
            case "6": showSubscriptionPlanMenu(); break;
            case "7": showWorkoutScheduleManagement(); break;
            case "8": logout(); break;
            default: System.out.println("Invalid choice!");
        }
    }

    private void showMemberManagementMenu() {
        System.out.println("\n=== MEMBER MANAGEMENT ===");
        System.out.println("1. Add Member");
        System.out.println("2. View All Members");
        System.out.println("3. Update Member");
        System.out.println("4. Delete Member");
        System.out.println("5. Assign Trainer to Member");
        System.out.println("6. View Member Details");
        System.out.println("7. View Expired Members");
        System.out.println("8. Back to Main Menu");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": addMember(); break;
            case "2": viewAllMembers(); break;
            case "3": updateMember(); break;
            case "4": deleteMember(); break;
            case "5": assignTrainerToMember(); break;
            case "6": viewMemberDetails(); break;
            case "7": viewExpiredMembers(); break;
            case "8": return;
            default: System.out.println("Invalid choice!");
        }
    }

    private void showTrainerManagementMenu() {
        System.out.println("\n=== TRAINER MANAGEMENT ===");
        System.out.println("1. Add Trainer");
        System.out.println("2. View All Trainers");
        System.out.println("3. Update Trainer");
        System.out.println("4. Delete Trainer");
        System.out.println("5. View Trainer Performance");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": addTrainer(); break;
            case "2": viewAllTrainers(); break;
            case "3": updateTrainer(); break;
            case "4": deleteTrainer(); break;
            case "5": viewTrainerPerformance(); break;
            case "6": return;
            default: System.out.println("Invalid choice!");
        }
    }

    private void showAttendanceManagementMenu() {
        System.out.println("\n=== ATTENDANCE MANAGEMENT ===");
        System.out.println("1. Check In Member");
        System.out.println("2. Check Out Member");
        System.out.println("3. View Today's Attendance");
        System.out.println("4. View Member Attendance History");
        System.out.println("5. Generate Attendance Report");
        System.out.println("6. Export Attendance Report");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": checkInMember(); break;
            case "2": checkOutMember(); break;
            case "3": viewTodayAttendance(); break;
            case "4": viewMemberAttendanceHistory(); break;
            case "5": generateAttendanceReport(); break;
            case "6": exportAttendanceReport(); break;
            case "7": return;
            default: System.out.println("Invalid choice!");
        }
    }

    private void showReportsMenu() {
        System.out.println("\n=== REPORTS & ANALYTICS ===");
        System.out.println("1. Revenue Report");
        System.out.println("2. Membership Report");
        System.out.println("3. Performance Report");
        System.out.println("4. Trainer Performance Report");
        System.out.println("5. Export Revenue Report");
        System.out.println("6. Export Membership Report");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": showRevenueReport(); break;
            case "2": showMembershipReport(); break;
            case "3": showPerformanceReport(); break;
            case "4": showTrainerPerformanceReport(); break;
            case "5": exportRevenueReport(); break;
            case "6": exportMembershipReport(); break;
            case "7": return;
            default: System.out.println("Invalid choice!");
        }
    }

    // TRAINER MENU SYSTEM
    private void showTrainerMenu() {
        System.out.println("\n=== TRAINER DASHBOARD ===");
        System.out.println("1. View Assigned Members");
        System.out.println("2. Create Workout Schedule");
        System.out.println("3. View Workout Schedules");
        System.out.println("4. Update Workout Progress");
        System.out.println("5. View Member Attendance");
        System.out.println("6. Add Exercise to Schedule");
        System.out.println("7. View Member Performance");
        System.out.println("8. Logout");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": viewAssignedMembers(); break;
            case "2": createWorkoutSchedule(); break;
            case "3": viewTrainerWorkoutSchedules(); break;
            case "4": updateWorkoutProgress(); break;
            case "5": viewMemberAttendanceAsTrainer(); break;
            case "6": addExerciseToSchedule(); break;
            case "7": viewMemberPerformanceAsTrainer(); break;
            case "8": logout(); break;
            default: System.out.println("Invalid choice!");
        }
    }

    // MEMBER MENU SYSTEM
    private void showMemberMenu() {
        System.out.println("\n=== MEMBER DASHBOARD ===");
        System.out.println("1. View My Profile");
        System.out.println("2. View My Workout Schedules");
        System.out.println("3. Update Workout Progress");
        System.out.println("4. View My Attendance");
        System.out.println("5. Renew Membership");
        System.out.println("6. View Available Subscription Plans");
        System.out.println("7. Logout");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": viewMyProfile(); break;
            case "2": viewMyWorkoutSchedules(); break;
            case "3": updateMyWorkoutProgress(); break;
            case "4": viewMyAttendance(); break;
            case "5": renewMembership(); break;
            case "6": viewSubscriptionPlans(); break;
            case "7": logout(); break;
            default: System.out.println("Invalid choice!");
        }
    }

    // MEMBER MANAGEMENT METHODS
    private void addMember() {
        System.out.println("\n=== ADD NEW MEMBER ===");
        
        System.out.print("User ID: ");
        String userId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Name: ");
        String name = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Email: ");
        String email = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Phone: ");
        String phone = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Password: ");
        String password = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Membership Type (monthly/quarterly/yearly): ");
        String membershipType = InputValidator.sanitizeInput(scanner.nextLine());
        
        userManager.addMember(userId, name, email, phone, password, membershipType);
    }

    private void viewAllMembers() {
        List<Member> members = userManager.getAllActiveMembers();
        
        System.out.println("\n=== ALL ACTIVE MEMBERS ===");
        if (members.isEmpty()) {
            System.out.println("No active members found.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-30s %-15s %-15s %-12s%n", 
            "User ID", "Name", "Email", "Membership", "End Date", "Progress");
        System.out.println("-".repeat(115));
        
        for (Member member : members) {
            System.out.printf("%-15s %-25s %-30s %-15s %-15s %.1f%%%n",
                member.getUserId(),
                member.getName(),
                member.getEmail(),
                member.getMembershipType(),
                member.getMembershipEndDate(),
                member.getProgressScore()
            );
        }
    }

    private void updateMember() {
        System.out.print("Enter Member ID to update: ");
        String userId = scanner.nextLine().trim();
        
        Member member = userManager.findMemberById(userId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        System.out.println("Current details: " + member);
        System.out.print("New Name (press enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = member.getName();
        
        System.out.print("New Email (press enter to keep current): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = member.getEmail();
        
        System.out.print("New Phone (press enter to keep current): ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = member.getPhone();
        
        userManager.updateMember(userId, name, email, phone);
    }

    private void deleteMember() {
        System.out.print("Enter Member ID to delete: ");
        String userId = scanner.nextLine().trim();
        
        userManager.deleteMember(userId);
    }

    private void assignTrainerToMember() {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        
        System.out.print("Enter Trainer ID: ");
        String trainerId = scanner.nextLine().trim();
        
        userManager.assignTrainerToMember(trainerId, memberId);
    }

    private void viewMemberDetails() {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        
        Member member = userManager.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }
        
        System.out.println("\n=== MEMBER DETAILS ===");
        System.out.println(member);
        
        // Show attendance stats
        attendanceManager.displayMemberAttendance(memberId, 30);
        
        // Show workout schedules
        List<WorkoutSchedule> schedules = workoutManager.getSchedulesByMember(memberId);
        if (!schedules.isEmpty()) {
            System.out.println("\nWorkout Schedules:");
            for (WorkoutSchedule schedule : schedules) {
                System.out.println("  " + schedule);
            }
        }
    }

    private void viewExpiredMembers() {
        List<Member> expiredMembers = userManager.getAllExpiredMembers();
        
        System.out.println("\n=== EXPIRED MEMBERS ===");
        if (expiredMembers.isEmpty()) {
            System.out.println("No expired members found.");
            return;
        }
        
        for (Member member : expiredMembers) {
            System.out.println(member);
        }
    }

    // TRAINER MANAGEMENT METHODS
    private void addTrainer() {
        System.out.println("\n=== ADD NEW TRAINER ===");
        
        System.out.print("User ID: ");
        String userId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Name: ");
        String name = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Email: ");
        String email = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Phone: ");
        String phone = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Password: ");
        String password = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Specialization: ");
        String specialization = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Experience (years): ");
        String experienceStr = scanner.nextLine().trim();
        
        if (!InputValidator.isValidInteger(experienceStr, 0, 50)) {
            System.out.println("Invalid experience! Must be 0-50 years.");
            return;
        }
        
        int experience = Integer.parseInt(experienceStr);
        userManager.addTrainer(userId, name, email, phone, password, specialization, experience);
    }

    private void viewAllTrainers() {
        List<Trainer> trainers = userManager.getAllActiveTrainers();
        
        System.out.println("\n=== ALL ACTIVE TRAINERS ===");
        if (trainers.isEmpty()) {
            System.out.println("No active trainers found.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-30s %-20s %-10s %-15s%n", 
            "User ID", "Name", "Email", "Specialization", "Experience", "Assigned Members");
        System.out.println("-".repeat(120));
        
        for (Trainer trainer : trainers) {
            System.out.printf("%-15s %-25s %-30s %-20s %-10d %-15d%n",
                trainer.getUserId(),
                trainer.getName(),
                trainer.getEmail(),
                trainer.getSpecialization(),
                trainer.getExperience(),
                trainer.getAssignedMemberIds().size()
            );
        }
    }

    private void updateTrainer() {
        System.out.print("Enter Trainer ID to update: ");
        String userId = scanner.nextLine().trim();
        
        Trainer trainer = userManager.findTrainerById(userId);
        if (trainer == null) {
            System.out.println("Trainer not found!");
            return;
        }
        
        System.out.println("Current details: " + trainer);
        
        System.out.print("New Name (press enter to keep current): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = trainer.getName();
        
        System.out.print("New Email (press enter to keep current): ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) email = trainer.getEmail();
        
        System.out.print("New Phone (press enter to keep current): ");
        String phone = scanner.nextLine().trim();
        if (phone.isEmpty()) phone = trainer.getPhone();
        
        System.out.print("New Specialization (press enter to keep current): ");
        String specialization = scanner.nextLine().trim();
        if (specialization.isEmpty()) specialization = trainer.getSpecialization();
        
        System.out.print("New Experience (press enter to keep current): ");
        String experienceStr = scanner.nextLine().trim();
        int experience = trainer.getExperience();
        if (!experienceStr.isEmpty() && InputValidator.isValidInteger(experienceStr, 0, 50)) {
            experience = Integer.parseInt(experienceStr);
        }
        
        userManager.updateTrainer(userId, name, email, phone, specialization, experience);
    }

    private void deleteTrainer() {
        System.out.print("Enter Trainer ID to delete: ");
        String userId = scanner.nextLine().trim();
        
        userManager.deleteTrainer(userId);
    }

    private void viewTrainerPerformance() {
        var performanceReport = reportManager.generateTrainerPerformanceReport();
        
        System.out.println("\n=== TRAINER PERFORMANCE REPORT ===");
        
        @SuppressWarnings("unchecked")
        List<java.util.Map<String, Object>> trainerDetails = 
            (List<java.util.Map<String, Object>>) performanceReport.get("trainerPerformanceDetails");
        
        if (trainerDetails.isEmpty()) {
            System.out.println("No trainer performance data available.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-20s %-15s %-15s %-15s%n", 
            "Trainer ID", "Name", "Specialization", "Assigned Members", "Avg Progress", "Completion Rate");
        System.out.println("-".repeat(125));
        
        for (var trainer : trainerDetails) {
            System.out.printf("%-15s %-25s %-20s %-15s %-15.1f %-15.1f%%%n",
                trainer.get("userId"),
                trainer.get("name"),
                trainer.get("specialization"),
                trainer.get("assignedMembers"),
                (Double) trainer.get("averageMemberProgressScore"),
                (Double) trainer.get("scheduleCompletionRate")
            );
        }
    }

    // ATTENDANCE METHODS
    private void checkInMember() {
        System.out.print("Enter Member ID to check in: ");
        String memberId = scanner.nextLine().trim();
        
        attendanceManager.checkInMember(memberId);
    }

    private void checkOutMember() {
        System.out.print("Enter Member ID to check out: ");
        String memberId = scanner.nextLine().trim();
        
        attendanceManager.checkOutMember(memberId);
    }

    private void viewTodayAttendance() {
        List<Attendance> todayAttendance = attendanceManager.getTodayAttendance();
        
        System.out.println("\n=== TODAY'S ATTENDANCE ===");
        if (todayAttendance.isEmpty()) {
            System.out.println("No attendance records for today.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-12s %-12s %-10s%n", 
            "Member ID", "Name", "Check In", "Check Out", "Status");
        System.out.println("-".repeat(80));
        
        for (Attendance attendance : todayAttendance) {
            System.out.printf("%-15s %-25s %-12s %-12s %-10s%n",
                attendance.getMemberId(),
                attendance.getMemberName(),
                attendance.getCheckInTime() != null ? 
                    attendance.getCheckInTime().toLocalTime().toString() : "-",
                attendance.getCheckOutTime() != null ? 
                    attendance.getCheckOutTime().toLocalTime().toString() : "-",
                attendance.getStatus()
            );
        }
    }

    private void viewMemberAttendanceHistory() {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        
        System.out.print("Enter number of days to view (default 30): ");
        String daysStr = scanner.nextLine().trim();
        int days = 30;
        
        if (!daysStr.isEmpty() && InputValidator.isValidInteger(daysStr, 1, 365)) {
            days = Integer.parseInt(daysStr);
        }
        
        attendanceManager.displayMemberAttendance(memberId, days);
    }

    private void generateAttendanceReport() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine().trim();
        
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine().trim();
        
        if (!InputValidator.isValidDate(startDateStr) || !InputValidator.isValidDate(endDateStr)) {
            System.out.println("Invalid date format!");
            return;
        }
        
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        var attendanceReport = reportManager.generateAttendanceReport(startDate, endDate);
        
        System.out.println("\n=== ATTENDANCE REPORT ===");
        System.out.println("Report Period: " + startDate + " to " + endDate);
        System.out.println("Total Visits: " + attendanceReport.get("totalVisits"));
        System.out.println("Unique Members: " + attendanceReport.get("uniqueMembers"));
        System.out.println("Average Visits per Day: " + 
            String.format("%.1f", (Double) attendanceReport.get("averageVisitsPerDay")));
        System.out.println("Average Workout Duration: " + 
            String.format("%.1f minutes", (Double) attendanceReport.get("averageWorkoutDuration")));
        System.out.println("Peak Hour: " + attendanceReport.get("peakHour") + ":00");
    }

    private void exportAttendanceReport() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine().trim();
        
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine().trim();
        
        System.out.print("Enter filename (without extension): ");
        String filename = scanner.nextLine().trim() + ".csv";
        
        if (!InputValidator.isValidDate(startDateStr) || !InputValidator.isValidDate(endDateStr)) {
            System.out.println("Invalid date format!");
            return;
        }
        
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        attendanceManager.exportAttendanceReport(startDate, endDate, filename);
    }

    // REPORT METHODS
    private void showRevenueReport() {
        System.out.print("Enter start date (YYYY-MM-DD, or press enter for last 30 days): ");
        String startDateStr = scanner.nextLine().trim();
        
        System.out.print("Enter end date (YYYY-MM-DD, or press enter for today): ");
        String endDateStr = scanner.nextLine().trim();
        
        LocalDate startDate = startDateStr.isEmpty() ? 
            LocalDate.now().minusDays(30) : LocalDate.parse(startDateStr);
        LocalDate endDate = endDateStr.isEmpty() ? 
            LocalDate.now() : LocalDate.parse(endDateStr);
        
        reportManager.displayRevenueReport(startDate, endDate);
    }

    private void showMembershipReport() {
        var membershipReport = reportManager.generateMembershipReport();
        
        System.out.println("\n=== MEMBERSHIP REPORT ===");
        System.out.println("Total Members: " + membershipReport.get("totalMembers"));
        System.out.println("Active Members: " + membershipReport.get("activeMembers"));
        System.out.println("Expired Members: " + membershipReport.get("expiredMembers"));
        System.out.println("Members Expiring Soon (30 days): " + membershipReport.get("membersExpiringSoon"));
        System.out.println("Average Membership Duration: " + 
            String.format("%.1f days", (Double) membershipReport.get("averageMembershipDuration")));
        
        @SuppressWarnings("unchecked")
        java.util.Map<String, Long> typeBreakdown = 
            (java.util.Map<String, Long>) membershipReport.get("membershipTypeBreakdown");
        
        System.out.println("\nMembership Type Breakdown:");
        typeBreakdown.forEach((type, count) -> 
            System.out.println("  " + type + ": " + count + " members"));
    }

    private void showPerformanceReport() {
        var performanceReport = reportManager.generatePerformanceReport();
        
        System.out.println("\n=== PERFORMANCE REPORT ===");
        System.out.println("Average Progress Score: " + 
            String.format("%.1f%%", (Double) performanceReport.get("averageProgressScore")));
        System.out.println("Total Workouts Completed: " + performanceReport.get("totalWorkoutsCompleted"));
        System.out.println("Total Workouts Scheduled: " + performanceReport.get("totalWorkoutsScheduled"));
        System.out.println("Overall Completion Rate: " + 
            String.format("%.1f%%", (Double) performanceReport.get("overallWorkoutCompletionRate")));
        
        @SuppressWarnings("unchecked")
        List<java.util.Map<String, Object>> topPerformers = 
            (List<java.util.Map<String, Object>>) performanceReport.get("topPerformingMembers");
        
        if (!topPerformers.isEmpty()) {
            System.out.println("\nTop Performing Members:");
            System.out.printf("%-15s %-25s %-15s %-15s%n", 
                "Member ID", "Name", "Progress Score", "Attendance %");
            System.out.println("-".repeat(75));
            
            for (var member : topPerformers.subList(0, Math.min(5, topPerformers.size()))) {
                System.out.printf("%-15s %-25s %-15.1f %-15.1f%n",
                    member.get("userId"),
                    member.get("name"),
                    (Double) member.get("progressScore"),
                    (Double) member.get("attendancePercentage")
                );
            }
        }
    }

    private void showTrainerPerformanceReport() {
        viewTrainerPerformance(); // Reuse the same method
    }

    private void exportRevenueReport() {
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine().trim();
        
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine().trim();
        
        System.out.print("Enter filename (without extension): ");
        String filename = scanner.nextLine().trim() + ".csv";
        
        if (!InputValidator.isValidDate(startDateStr) || !InputValidator.isValidDate(endDateStr)) {
            System.out.println("Invalid date format!");
            return;
        }
        
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        reportManager.exportRevenueReportToCsv(startDate, endDate, filename);
    }

    private void exportMembershipReport() {
        System.out.print("Enter filename (without extension): ");
        String filename = scanner.nextLine().trim() + ".csv";
        
        reportManager.exportMembershipReportToCsv(filename);
    }

    // WORKOUT SCHEDULE MANAGEMENT
    private void showWorkoutScheduleManagement() {
        System.out.println("\n=== WORKOUT SCHEDULE MANAGEMENT ===");
        System.out.println("1. Create Workout Schedule");
        System.out.println("2. View All Schedules");
        System.out.println("3. Update Schedule");
        System.out.println("4. View Schedule Details");
        System.out.println("5. Cancel Schedule");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": createWorkoutSchedule(); break;
            case "2": viewAllWorkoutSchedules(); break;
            case "3": updateWorkoutScheduleAsAdmin(); break;
            case "4": viewScheduleDetails(); break;
            case "5": cancelWorkoutSchedule(); break;
            case "6": return;
            default: System.out.println("Invalid choice!");
        }
    }

    private void createWorkoutSchedule() {
        System.out.println("\n=== CREATE WORKOUT SCHEDULE ===");
        
        System.out.print("Schedule ID: ");
        String scheduleId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Member ID: ");
        String memberId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Trainer ID: ");
        String trainerId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Workout Name: ");
        String workoutName = InputValidator.sanitizeInput(scanner.nextLine());
        
        if (workoutManager.createWorkoutSchedule(scheduleId, memberId, trainerId, workoutName)) {
            System.out.print("Schedule workout time? (y/n): ");
            String scheduleTime = scanner.nextLine().trim().toLowerCase();
            
            if (scheduleTime.equals("y") || scheduleTime.equals("yes")) {
                System.out.print("Enter date and time (YYYY-MM-DDTHH:MM:SS): ");
                String dateTimeStr = scanner.nextLine().trim();
                workoutManager.scheduleWorkout(scheduleId, dateTimeStr);
            }
        }
    }

    private void viewAllWorkoutSchedules() {
        List<WorkoutSchedule> schedules = workoutManager.getAllWorkoutSchedules();
        
        System.out.println("\n=== ALL WORKOUT SCHEDULES ===");
        if (schedules.isEmpty()) {
            System.out.println("No workout schedules found.");
            return;
        }
        
        System.out.printf("%-15s %-15s %-15s %-25s %-20s %-12s%n", 
            "Schedule ID", "Member ID", "Trainer ID", "Workout Name", "Scheduled Time", "Status");
        System.out.println("-".repeat(120));
        
        for (WorkoutSchedule schedule : schedules) {
            System.out.printf("%-15s %-15s %-15s %-25s %-20s %-12s%n",
                schedule.getScheduleId(),
                schedule.getMemberId(),
                schedule.getTrainerId(),
                schedule.getWorkoutName(),
                schedule.getScheduledTime() != null ? 
                    schedule.getScheduledTime().format(DateTimeFormatter.ofPattern("MM-dd HH:mm")) : "Not scheduled",
                schedule.getStatus()
            );
        }
    }

    private void updateWorkoutScheduleAsAdmin() {
        System.out.print("Enter Schedule ID to update: ");
        String scheduleId = scanner.nextLine().trim();
        
        WorkoutSchedule schedule = workoutManager.findScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return;
        }
        
        System.out.println("Current schedule: " + schedule);
        
        System.out.print("New workout name (press enter to keep current): ");
        String workoutName = scanner.nextLine().trim();
        
        System.out.print("New description (press enter to keep current): ");
        String description = scanner.nextLine().trim();
        
        System.out.print("New duration in minutes (press enter to keep current): ");
        String durationStr = scanner.nextLine().trim();
        int duration = -1;
        if (!durationStr.isEmpty() && InputValidator.isValidInteger(durationStr, 1, 300)) {
            duration = Integer.parseInt(durationStr);
        }
        
        workoutManager.updateWorkoutSchedule(scheduleId, 
            workoutName.isEmpty() ? null : workoutName,
            description.isEmpty() ? null : description,
            null, // scheduledTime
            duration,
            null  // workoutType
        );
    }

    private void viewScheduleDetails() {
        System.out.print("Enter Schedule ID: ");
        String scheduleId = scanner.nextLine().trim();
        
        workoutManager.displayScheduleDetails(scheduleId);
    }

    private void cancelWorkoutSchedule() {
        System.out.print("Enter Schedule ID to cancel: ");
        String scheduleId = scanner.nextLine().trim();
        
        System.out.print("Reason for cancellation: ");
        String reason = scanner.nextLine().trim();
        
        workoutManager.cancelWorkout(scheduleId, reason);
    }

    // SUBSCRIPTION PLAN METHODS
    private void showSubscriptionPlanMenu() {
        System.out.println("\n=== SUBSCRIPTION PLAN MANAGEMENT ===");
        System.out.println("1. View Active Plans");
        System.out.println("2. Add New Plan");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose option: ");
        
        String choice = scanner.nextLine().trim();
        
        switch (choice) {
            case "1": viewSubscriptionPlans(); break;
            case "2": addSubscriptionPlan(); break;
            case "3": return;
            default: System.out.println("Invalid choice!");
        }
    }

    private void viewSubscriptionPlans() {
        List<SubscriptionPlan> plans = reportManager.getActiveSubscriptionPlans();
        
        System.out.println("\n=== SUBSCRIPTION PLANS ===");
        if (plans.isEmpty()) {
            System.out.println("No subscription plans available.");
            return;
        }
        
        for (SubscriptionPlan plan : plans) {
            System.out.println("\n" + plan);
            System.out.println("  Benefits: " + plan.getBenefits());
        }
    }

    private void addSubscriptionPlan() {
        System.out.println("\n=== ADD SUBSCRIPTION PLAN ===");
        
        System.out.print("Plan ID: ");
        String planId = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Plan Name: ");
        String planName = InputValidator.sanitizeInput(scanner.nextLine());
        
        System.out.print("Price: ");
        String priceStr = scanner.nextLine().trim();
        
        System.out.print("Duration (months): ");
        String durationStr = scanner.nextLine().trim();
        
        System.out.print("Plan Type (BASIC/STANDARD/PREMIUM/UNLIMITED): ");
        String planTypeStr = scanner.nextLine().trim().toUpperCase();
        
        if (!InputValidator.isValidPrice(priceStr)) {
            System.out.println("Invalid price!");
            return;
        }
        
        if (!InputValidator.isValidInteger(durationStr, 1, 36)) {
            System.out.println("Invalid duration! Must be 1-36 months.");
            return;
        }
        
        try {
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);
            SubscriptionPlan.PlanType planType = SubscriptionPlan.PlanType.valueOf(planTypeStr);
            
            reportManager.addSubscriptionPlan(planId, planName, price, duration, planType);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid plan type! Must be BASIC, STANDARD, PREMIUM, or UNLIMITED.");
        }
    }

    // TRAINER-SPECIFIC METHODS
    private void viewAssignedMembers() {
        Trainer trainer = (Trainer) currentUser;
        List<String> assignedMemberIds = trainer.getAssignedMemberIds();
        
        System.out.println("\n=== MY ASSIGNED MEMBERS ===");
        if (assignedMemberIds.isEmpty()) {
            System.out.println("No members assigned to you.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-30s %-15s %-12s%n", 
            "Member ID", "Name", "Email", "Membership", "Progress");
        System.out.println("-".repeat(105));
        
        for (String memberId : assignedMemberIds) {
            Member member = userManager.findMemberById(memberId);
            if (member != null) {
                System.out.printf("%-15s %-25s %-30s %-15s %.1f%%%n",
                    member.getUserId(),
                    member.getName(),
                    member.getEmail(),
                    member.getMembershipType(),
                    member.getProgressScore()
                );
            }
        }
    }

    private void viewTrainerWorkoutSchedules() {
        Trainer trainer = (Trainer) currentUser;
        List<WorkoutSchedule> schedules = workoutManager.getSchedulesByTrainer(trainer.getUserId());
        
        System.out.println("\n=== MY WORKOUT SCHEDULES ===");
        if (schedules.isEmpty()) {
            System.out.println("No workout schedules found.");
            return;
        }
        
        for (WorkoutSchedule schedule : schedules) {
            Member member = userManager.findMemberById(schedule.getMemberId());
            System.out.println("\nSchedule: " + schedule.getScheduleId() + 
                             " | Member: " + (member != null ? member.getName() : "Unknown") +
                             " | Status: " + schedule.getStatus() +
                             " | Progress: " + schedule.getCompletionPercentage() + "%");
        }
    }

    private void updateWorkoutProgress() {
        System.out.print("Enter Schedule ID: ");
        String scheduleId = scanner.nextLine().trim();
        
        WorkoutSchedule schedule = workoutManager.findScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return;
        }
        
        // Check if current user is the trainer for this schedule
        if (currentUser.getRole() == User.UserRole.TRAINER && 
            !schedule.getTrainerId().equals(currentUser.getUserId())) {
            System.out.println("You can only update schedules assigned to you!");
            return;
        }
        
        System.out.print("Completion percentage (0-100): ");
        String percentageStr = scanner.nextLine().trim();
        
        System.out.print("Progress notes: ");
        String notes = scanner.nextLine().trim();
        
        if (!InputValidator.isValidPercentage(percentageStr)) {
            System.out.println("Invalid percentage!");
            return;
        }
        
        double percentage = Double.parseDouble(percentageStr);
        workoutManager.updateWorkoutProgress(scheduleId, percentage, notes);
    }

    private void viewMemberAttendanceAsTrainer() {
        System.out.print("Enter Member ID: ");
        String memberId = scanner.nextLine().trim();
        
        // Check if the member is assigned to this trainer
        Trainer trainer = (Trainer) currentUser;
        if (!trainer.getAssignedMemberIds().contains(memberId)) {
            System.out.println("This member is not assigned to you!");
            return;
        }
        
        attendanceManager.displayMemberAttendance(memberId, 30);
    }

    private void addExerciseToSchedule() {
        System.out.print("Enter Schedule ID: ");
        String scheduleId = scanner.nextLine().trim();
        
        WorkoutSchedule schedule = workoutManager.findScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return;
        }
        
        if (!schedule.getTrainerId().equals(currentUser.getUserId())) {
            System.out.println("You can only modify schedules assigned to you!");
            return;
        }
        
        System.out.print("Exercise name: ");
        String exerciseName = scanner.nextLine().trim();
        
        System.out.print("Is this a cardio exercise? (y/n): ");
        String isCardio = scanner.nextLine().trim().toLowerCase();
        
        if (isCardio.equals("y") || isCardio.equals("yes")) {
            System.out.print("Duration in seconds: ");
            String durationStr = scanner.nextLine().trim();
            
            if (InputValidator.isValidInteger(durationStr, 1, 7200)) {
                int duration = Integer.parseInt(durationStr);
                workoutManager.addCardioExerciseToSchedule(scheduleId, exerciseName, duration);
            } else {
                System.out.println("Invalid duration!");
            }
        } else {
            System.out.print("Number of sets: ");
            String setsStr = scanner.nextLine().trim();
            
            System.out.print("Number of reps: ");
            String repsStr = scanner.nextLine().trim();
            
            System.out.print("Weight (kg, optional): ");
            String weightStr = scanner.nextLine().trim();
            
            if (InputValidator.isValidInteger(setsStr, 1, 10) && 
                InputValidator.isValidInteger(repsStr, 1, 100)) {
                
                int sets = Integer.parseInt(setsStr);
                int reps = Integer.parseInt(repsStr);
                double weight = 0.0;
                
                if (!weightStr.isEmpty() && InputValidator.isValidPrice(weightStr)) {
                    weight = Double.parseDouble(weightStr);
                }
                
                workoutManager.addExerciseToSchedule(scheduleId, exerciseName, sets, reps, weight);
            } else {
                System.out.println("Invalid sets or reps!");
            }
        }
    }

    private void viewMemberPerformanceAsTrainer() {
        Trainer trainer = (Trainer) currentUser;
        
        System.out.println("\n=== MEMBER PERFORMANCE SUMMARY ===");
        if (trainer.getAssignedMemberIds().isEmpty()) {
            System.out.println("No members assigned to you.");
            return;
        }
        
        System.out.printf("%-15s %-25s %-15s %-15s %-15s%n", 
            "Member ID", "Name", "Progress Score", "Attendance %", "Workouts Done");
        System.out.println("-".repeat(90));
        
        for (String memberId : trainer.getAssignedMemberIds()) {
            Member member = userManager.findMemberById(memberId);
            if (member != null) {
                System.out.printf("%-15s %-25s %-15.1f %-15.1f %-15d%n",
                    member.getUserId(),
                    member.getName(),
                    member.getProgressScore(),
                    member.getAttendancePercentage(),
                    member.getAttendedWorkouts()
                );
            }
        }
    }

    // MEMBER-SPECIFIC METHODS
    private void viewMyProfile() {
        Member member = (Member) currentUser;
        
        System.out.println("\n=== MY PROFILE ===");
        System.out.println("Name: " + member.getName());
        System.out.println("Email: " + member.getEmail());
        System.out.println("Phone: " + member.getPhone());
        System.out.println("Membership Type: " + member.getMembershipType());
        System.out.println("Membership Start: " + member.getMembershipStartDate());
        System.out.println("Membership End: " + member.getMembershipEndDate());
        System.out.println("Status: " + (member.isMembershipExpired() ? "EXPIRED" : "ACTIVE"));
        System.out.println("Progress Score: " + String.format("%.1f%%", member.getProgressScore()));
        System.out.println("Attendance Percentage: " + String.format("%.1f%%", member.getAttendancePercentage()));
        System.out.println("Total Workouts: " + member.getTotalWorkouts());
        System.out.println("Completed Workouts: " + member.getAttendedWorkouts());
        
        if (member.getAssignedTrainerId() != null) {
            Trainer trainer = userManager.findTrainerById(member.getAssignedTrainerId());
            System.out.println("Assigned Trainer: " + 
                (trainer != null ? trainer.getName() + " (" + trainer.getSpecialization() + ")" : "Unknown"));
        }
    }

    private void viewMyWorkoutSchedules() {
        Member member = (Member) currentUser;
        List<WorkoutSchedule> schedules = workoutManager.getSchedulesByMember(member.getUserId());
        
        System.out.println("\n=== MY WORKOUT SCHEDULES ===");
        if (schedules.isEmpty()) {
            System.out.println("No workout schedules found.");
            return;
        }
        
        for (WorkoutSchedule schedule : schedules) {
            Trainer trainer = userManager.findTrainerById(schedule.getTrainerId());
            System.out.println("\n" + schedule.getWorkoutName() + " (ID: " + schedule.getScheduleId() + ")");
            System.out.println("  Trainer: " + (trainer != null ? trainer.getName() : "Unknown"));
            System.out.println("  Status: " + schedule.getStatus());
            System.out.println("  Progress: " + schedule.getCompletionPercentage() + "%");
            System.out.println("  Scheduled: " + 
                (schedule.getScheduledTime() != null ? schedule.getScheduledTime() : "Not scheduled"));
            
            if (!schedule.getExercises().isEmpty()) {
                System.out.println("  Exercises:");
                for (WorkoutSchedule.Exercise exercise : schedule.getExercises()) {
                    System.out.println("    - " + exercise);
                }
            }
        }
    }

    private void updateMyWorkoutProgress() {
        Member member = (Member) currentUser;
        List<WorkoutSchedule> schedules = workoutManager.getSchedulesByMember(member.getUserId());
        
        if (schedules.isEmpty()) {
            System.out.println("No workout schedules found.");
            return;
        }
        
        System.out.println("\n=== MY ACTIVE SCHEDULES ===");
        for (int i = 0; i < schedules.size(); i++) {
            WorkoutSchedule schedule = schedules.get(i);
            System.out.println((i + 1) + ". " + schedule.getWorkoutName() + 
                             " (Progress: " + schedule.getCompletionPercentage() + "%)");
        }
        
        System.out.print("Enter schedule number to update: ");
        String choiceStr = scanner.nextLine().trim();
        
        if (!InputValidator.isValidInteger(choiceStr, 1, schedules.size())) {
            System.out.println("Invalid choice!");
            return;
        }
        
        int choice = Integer.parseInt(choiceStr) - 1;
        WorkoutSchedule schedule = schedules.get(choice);
        
        System.out.print("Completion percentage (0-100): ");
        String percentageStr = scanner.nextLine().trim();
        
        System.out.print("Progress notes: ");
        String notes = scanner.nextLine().trim();
        
        if (!InputValidator.isValidPercentage(percentageStr)) {
            System.out.println("Invalid percentage!");
            return;
        }
        
        double percentage = Double.parseDouble(percentageStr);
        workoutManager.updateWorkoutProgress(schedule.getScheduleId(), percentage, notes);
    }

    private void viewMyAttendance() {
        Member member = (Member) currentUser;
        attendanceManager.displayMemberAttendance(member.getUserId(), 30);
    }

    private void renewMembership() {
        Member member = (Member) currentUser;
        
        System.out.println("\n=== RENEW MEMBERSHIP ===");
        System.out.println("Current membership expires on: " + member.getMembershipEndDate());
        
        System.out.print("New membership type (monthly/quarterly/yearly): ");
        String membershipType = scanner.nextLine().trim().toLowerCase();
        
        if (!InputValidator.isValidMembershipType(membershipType)) {
            System.out.println("Invalid membership type!");
            return;
        }
        
        member.renewMembership(membershipType);
        userManager.saveAllUsers();
        
        System.out.println("Membership renewed successfully!");
        System.out.println("New expiry date: " + member.getMembershipEndDate());
    }

    // UTILITY METHODS
    private void logout() {
        userManager.logout();
        currentUser = null;
    }

    private void createDefaultAdminIfNeeded() {
        if (userManager.getAdmins().isEmpty()) {
            System.out.println("Creating default admin account...");
            userManager.addAdmin("admin", "System Administrator", "admin@gym.com", "1234567890", "admin123", "Super Admin");
            System.out.println("Default admin created:");
            System.out.println("  User ID: admin");
            System.out.println("  Password: admin123");
            System.out.println("  Email: admin@gym.com");
        }
    }

    public static void main(String[] args) {
        try {
            GymManagementSystem gms = new GymManagementSystem();
            gms.start();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 