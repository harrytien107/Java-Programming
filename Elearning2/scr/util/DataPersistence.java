package scr.util;

import scr.entity.*;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataPersistence {
    private static final String DATA_DIR = "data/";
    private static final String MEMBERS_FILE = DATA_DIR + "members.csv";
    private static final String TRAINERS_FILE = DATA_DIR + "trainers.csv";
    private static final String ADMINS_FILE = DATA_DIR + "admins.csv";
    private static final String SCHEDULES_FILE = DATA_DIR + "schedules.csv";
    private static final String ATTENDANCE_FILE = DATA_DIR + "attendance.csv";
    private static final String SUBSCRIPTION_PLANS_FILE = DATA_DIR + "subscription_plans.csv";

    public static void initializeDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    // Member operations
    public static void saveMembers(List<Member> members) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MEMBERS_FILE))) {
            writer.println("userId,name,email,phone,password,membershipType,membershipStartDate,membershipEndDate,isActive,assignedTrainerId,progressScore,totalWorkouts,attendedWorkouts");
            
            for (Member member : members) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%.2f,%d,%d%n",
                    member.getUserId(),
                    member.getName(),
                    member.getEmail(),
                    member.getPhone(),
                    member.getPassword(),
                    member.getMembershipType(),
                    member.getMembershipStartDate(),
                    member.getMembershipEndDate(),
                    member.isActive(),
                    member.getAssignedTrainerId() != null ? member.getAssignedTrainerId() : "",
                    member.getProgressScore(),
                    member.getTotalWorkouts(),
                    member.getAttendedWorkouts()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    public static List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        File file = new File(MEMBERS_FILE);
        
        if (!file.exists()) {
            return members;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 13) {
                    Member member = new Member(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    member.setMembershipStartDate(LocalDate.parse(parts[6]));
                    member.setMembershipEndDate(LocalDate.parse(parts[7]));
                    member.setActive(Boolean.parseBoolean(parts[8]));
                    if (!parts[9].isEmpty()) {
                        member.setAssignedTrainerId(parts[9]);
                    }
                    member.setProgressScore(Double.parseDouble(parts[10]));
                    member.setTotalWorkouts(Integer.parseInt(parts[11]));
                    member.setAttendedWorkouts(Integer.parseInt(parts[12]));
                    members.add(member);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        
        return members;
    }

    // Trainer operations
    public static void saveTrainers(List<Trainer> trainers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRAINERS_FILE))) {
            writer.println("userId,name,email,phone,password,specialization,experience,isActive,assignedMemberIds");
            
            for (Trainer trainer : trainers) {
                String memberIds = String.join(";", trainer.getAssignedMemberIds());
                String line = String.format("%s,%s,%s,%s,%s,%s,%d,%s,%s",
                    trainer.getUserId(),
                    trainer.getName(),
                    trainer.getEmail(),
                    trainer.getPhone(),
                    trainer.getPassword(),
                    trainer.getSpecialization().replace(",", ";"),
                    trainer.getExperience(),
                    trainer.isActive(),
                    memberIds
                );
                writer.println(line);
            }
            writer.flush(); // Ensure data is written immediately
        } catch (IOException e) {
            System.err.println("Error saving trainers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Trainer> loadTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        File file = new File(TRAINERS_FILE);
        
        if (!file.exists()) {
            return trainers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split(",");
                    if (parts.length >= 9) {
                        // Replace semicolons back to commas in specialization
                        String specialization = parts[5].replace(";", ",");
                        Trainer trainer = new Trainer(parts[0], parts[1], parts[2], parts[3], parts[4], specialization, Integer.parseInt(parts[6].trim()));
                        trainer.setActive(Boolean.parseBoolean(parts[7]));
                        
                        if (!parts[8].isEmpty()) {
                            String[] memberIds = parts[8].split(";");
                            trainer.setAssignedMemberIds(new ArrayList<>(Arrays.asList(memberIds)));
                        }
                        trainers.add(trainer);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing trainer data: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading trainers: " + e.getMessage());
        }
        
        return trainers;
    }

    // Admin operations
    public static void saveAdmins(List<Admin> admins) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ADMINS_FILE))) {
            writer.println("userId,name,email,phone,password,adminLevel,isActive");
            
            for (Admin admin : admins) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s%n",
                    admin.getUserId(),
                    admin.getName(),
                    admin.getEmail(),
                    admin.getPhone(),
                    admin.getPassword(),
                    admin.getAdminLevel(),
                    admin.isActive()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving admins: " + e.getMessage());
        }
    }

    public static List<Admin> loadAdmins() {
        List<Admin> admins = new ArrayList<>();
        File file = new File(ADMINS_FILE);
        
        if (!file.exists()) {
            return admins;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Admin admin = new Admin(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                    admin.setActive(Boolean.parseBoolean(parts[6]));
                    admins.add(admin);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading admins: " + e.getMessage());
        }
        
        return admins;
    }

    // Attendance operations
    public static void saveAttendance(List<Attendance> attendanceList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            writer.println("attendanceId,memberId,memberName,date,checkInTime,checkOutTime,workoutScheduleId,status,notes");
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            
            for (Attendance attendance : attendanceList) {
                writer.printf("%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                    attendance.getAttendanceId(),
                    attendance.getMemberId(),
                    attendance.getMemberName(),
                    attendance.getDate().format(dateFormatter),
                    attendance.getCheckInTime() != null ? attendance.getCheckInTime().format(dateTimeFormatter) : "",
                    attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().format(dateTimeFormatter) : "",
                    attendance.getWorkoutScheduleId() != null ? attendance.getWorkoutScheduleId() : "",
                    attendance.getStatus(),
                    attendance.getNotes() != null ? attendance.getNotes().replace(",", ";") : ""
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving attendance: " + e.getMessage());
        }
    }

    public static List<Attendance> loadAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        File file = new File(ATTENDANCE_FILE);
        
        if (!file.exists()) {
            return attendanceList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    Attendance attendance = new Attendance(parts[0], parts[1], parts[2]);
                    attendance.setDate(LocalDate.parse(parts[3], dateFormatter));
                    
                    if (!parts[4].isEmpty()) {
                        attendance.setCheckInTime(LocalDateTime.parse(parts[4], dateTimeFormatter));
                    }
                    if (!parts[5].isEmpty()) {
                        attendance.setCheckOutTime(LocalDateTime.parse(parts[5], dateTimeFormatter));
                    }
                    if (!parts[6].isEmpty()) {
                        attendance.setWorkoutScheduleId(parts[6]);
                    }
                    
                    attendance.setStatus(Attendance.AttendanceStatus.valueOf(parts[7]));
                    
                    if (!parts[8].isEmpty()) {
                        attendance.setNotes(parts[8].replace(";", ","));
                    }
                    
                    attendanceList.add(attendance);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading attendance: " + e.getMessage());
        }
        
        return attendanceList;
    }

    // Subscription Plans operations
    public static void saveSubscriptionPlans(List<SubscriptionPlan> plans) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SUBSCRIPTION_PLANS_FILE))) {
            writer.println("planId,planName,description,price,durationMonths,planType,isActive,benefits,maxWorkoutsPerWeek,includesPersonalTrainer");
            
            for (SubscriptionPlan plan : plans) {
                writer.printf("%s,%s,%s,%.2f,%d,%s,%s,%s,%d,%s%n",
                    plan.getPlanId(),
                    plan.getPlanName(),
                    plan.getDescription() != null ? plan.getDescription().replace(",", ";") : "",
                    plan.getPrice(),
                    plan.getDurationMonths(),
                    plan.getPlanType(),
                    plan.isActive(),
                    plan.getBenefits().replace(",", ";"),
                    plan.getMaxWorkoutsPerWeek(),
                    plan.isIncludesPersonalTrainer()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving subscription plans: " + e.getMessage());
        }
    }

    public static List<SubscriptionPlan> loadSubscriptionPlans() {
        List<SubscriptionPlan> plans = new ArrayList<>();
        File file = new File(SUBSCRIPTION_PLANS_FILE);
        
        if (!file.exists()) {
            return plans;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    SubscriptionPlan plan = new SubscriptionPlan(
                        parts[0], parts[1], 
                        Double.parseDouble(parts[3]), 
                        Integer.parseInt(parts[4]), 
                        SubscriptionPlan.PlanType.valueOf(parts[5])
                    );
                    
                    if (!parts[2].isEmpty()) {
                        plan.setDescription(parts[2].replace(";", ","));
                    }
                    plan.setActive(Boolean.parseBoolean(parts[6]));
                    plan.setBenefits(parts[7].replace(";", ","));
                    plan.setMaxWorkoutsPerWeek(Integer.parseInt(parts[8]));
                    plan.setIncludesPersonalTrainer(Boolean.parseBoolean(parts[9]));
                    
                    plans.add(plan);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading subscription plans: " + e.getMessage());
        }
        
        return plans;
    }

    // Export reports to CSV
    public static void exportReportToCsv(String filename, String[] headers, List<String[]> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + filename))) {
            writer.println(String.join(",", headers));
            
            for (String[] row : data) {
                writer.println(String.join(",", row));
            }
            
            System.out.println("Report exported to: " + DATA_DIR + filename);
        } catch (IOException e) {
            System.err.println("Error exporting report: " + e.getMessage());
        }
    }

    // Workout Schedule operations
    public static void saveWorkoutSchedules(List<WorkoutSchedule> schedules) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCHEDULES_FILE))) {
            writer.println("scheduleId,memberId,trainerId,workoutName,description,scheduledTime,durationMinutes,workoutType,status,completionPercentage,progressNotes,exercises");
            
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            
            for (WorkoutSchedule schedule : schedules) {
                // Convert exercises to string format
                StringBuilder exercisesStr = new StringBuilder();
                for (WorkoutSchedule.Exercise exercise : schedule.getExercises()) {
                    if (exercisesStr.length() > 0) {
                        exercisesStr.append("|");
                    }
                    exercisesStr.append(exercise.getName()).append(":").append(exercise.getSets())
                            .append(":").append(exercise.getReps()).append(":").append(exercise.getWeight())
                            .append(":").append(exercise.getDurationSeconds());
                }
                
                String line = String.format("%s,%s,%s,%s,%s,%s,%d,%s,%s,%.2f,%s,%s",
                    schedule.getScheduleId(),
                    schedule.getMemberId(),
                    schedule.getTrainerId(),
                    schedule.getWorkoutName(),
                    schedule.getDescription() != null ? schedule.getDescription().replace(",", ";") : "",
                    schedule.getScheduledTime() != null ? schedule.getScheduledTime().format(dateTimeFormatter) : "",
                    schedule.getDurationMinutes(),
                    schedule.getWorkoutType(),
                    schedule.getStatus(),
                    schedule.getCompletionPercentage(),
                    schedule.getProgressNotes() != null ? schedule.getProgressNotes().replace(",", ";") : "",
                    exercisesStr.toString()
                );
                
                writer.println(line);
            }
            
            writer.flush(); // Ensure data is written immediately
        } catch (IOException e) {
            System.err.println("Error saving workout schedules: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<WorkoutSchedule> loadWorkoutSchedules() {
        List<WorkoutSchedule> schedules = new ArrayList<>();
        File file = new File(SCHEDULES_FILE);
        
        if (!file.exists()) {
            return schedules;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    String[] parts = line.split(",", -1); // Use -1 to include trailing empty strings
                    
                    if (parts.length >= 4) { // At least scheduleId, memberId, trainerId, workoutName
                        WorkoutSchedule schedule = new WorkoutSchedule(parts[0], parts[1], parts[2], parts[3]);
                        
                        // Set description
                        if (parts.length > 4 && !parts[4].isEmpty()) {
                            schedule.setDescription(parts[4].replace(";", ","));
                        }
                        
                        // Set scheduled time
                        if (parts.length > 5 && !parts[5].isEmpty()) {
                            schedule.setScheduledTime(LocalDateTime.parse(parts[5], dateTimeFormatter));
                        }
                        
                        // Set duration
                        if (parts.length > 6 && !parts[6].isEmpty()) {
                            schedule.setDurationMinutes(Integer.parseInt(parts[6]));
                        }
                        
                        // Set workout type
                        if (parts.length > 7 && !parts[7].isEmpty()) {
                            schedule.setWorkoutType(WorkoutSchedule.WorkoutType.valueOf(parts[7]));
                        }
                        
                        // Set status
                        if (parts.length > 8 && !parts[8].isEmpty()) {
                            schedule.setStatus(WorkoutSchedule.ScheduleStatus.valueOf(parts[8]));
                        }
                        
                        // Set completion percentage
                        if (parts.length > 9 && !parts[9].isEmpty()) {
                            schedule.setCompletionPercentage(Double.parseDouble(parts[9]));
                        }
                        
                        // Set progress notes
                        if (parts.length > 10 && !parts[10].isEmpty()) {
                            schedule.setProgressNotes(parts[10].replace(";", ","));
                        }
                        
                        // Parse exercises
                        if (parts.length > 11 && !parts[11].isEmpty()) {
                            String[] exerciseStrs = parts[11].split("\\|");
                            for (String exerciseStr : exerciseStrs) {
                                String[] exerciseParts = exerciseStr.split(":");
                                if (exerciseParts.length >= 5) {
                                    WorkoutSchedule.Exercise exercise = new WorkoutSchedule.Exercise(
                                        exerciseParts[0], 
                                        Integer.parseInt(exerciseParts[1]), 
                                        Integer.parseInt(exerciseParts[2])
                                    );
                                    exercise.setWeight(Double.parseDouble(exerciseParts[3]));
                                    exercise.setDurationSeconds(Integer.parseInt(exerciseParts[4]));
                                    schedule.addExercise(exercise);
                                }
                            }
                        }
                        
                        schedules.add(schedule);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing workout schedule line: " + line + " - " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error loading workout schedules: " + e.getMessage());
        }
        
        return schedules;
    }
} 