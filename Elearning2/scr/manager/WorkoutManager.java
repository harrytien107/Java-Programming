package scr.manager;

import scr.entity.*;
import scr.util.DataPersistence;
import scr.util.InputValidator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class WorkoutManager {
    private List<WorkoutSchedule> workoutSchedules;
    private UserManager userManager;

    public WorkoutManager(UserManager userManager) {
        this.workoutSchedules = new ArrayList<>();
        this.userManager = userManager;
        // Không load schedules vào memory nữa, sẽ load từ file khi cần
    }

    // Create workout schedule
    public boolean createWorkoutSchedule(String scheduleId, String memberId, String trainerId, String workoutName) {
        if (!InputValidator.isValidUserId(scheduleId)) {
            System.out.println("Invalid schedule ID");
            return false;
        }

        if (findScheduleById(scheduleId) != null) {
            System.out.println("Schedule ID already exists!");
            return false;
        }

        Member member = userManager.findMemberById(memberId);
        Trainer trainer = userManager.findTrainerById(trainerId);

        if (member == null) {
            System.out.println("Member not found!");
            return false;
        }

        if (trainer == null) {
            System.out.println("Trainer not found!");
            return false;
        }

        WorkoutSchedule schedule = new WorkoutSchedule(scheduleId, memberId, trainerId, workoutName);
        
        // Load existing schedules, add new one, then save
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        allSchedules.add(schedule);
        DataPersistence.saveWorkoutSchedules(allSchedules);
        
        // Add schedule ID to member's workout list
        member.getWorkoutScheduleIds().add(scheduleId);
        member.addWorkout(); // Increment total workouts
        
        userManager.saveAllUsers();
        
        System.out.println("Workout schedule created successfully!");
        return true;
    }

    // Update workout schedule
    public boolean updateWorkoutSchedule(String scheduleId, String workoutName, String description, 
                                       LocalDateTime scheduledTime, int durationMinutes, 
                                       WorkoutSchedule.WorkoutType workoutType) {
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        WorkoutSchedule schedule = allSchedules.stream()
            .filter(s -> s.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
            
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return false;
        }

        if (InputValidator.isNotEmpty(workoutName)) {
            schedule.setWorkoutName(workoutName);
        }
        if (InputValidator.isNotEmpty(description)) {
            schedule.setDescription(description);
        }
        if (scheduledTime != null) {
            schedule.setScheduledTime(scheduledTime);
        }
        if (durationMinutes > 0) {
            schedule.setDurationMinutes(durationMinutes);
        }
        if (workoutType != null) {
            schedule.setWorkoutType(workoutType);
        }

        DataPersistence.saveWorkoutSchedules(allSchedules);
        System.out.println("Workout schedule updated successfully!");
        return true;
    }

    // Add exercise to workout schedule
    public boolean addExerciseToSchedule(String scheduleId, String exerciseName, int sets, int reps, double weight) {
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        WorkoutSchedule schedule = allSchedules.stream()
            .filter(s -> s.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
            
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return false;
        }

        WorkoutSchedule.Exercise exercise = new WorkoutSchedule.Exercise(exerciseName, sets, reps);
        exercise.setWeight(weight);
        schedule.addExercise(exercise);
        
        DataPersistence.saveWorkoutSchedules(allSchedules);
        System.out.println("Exercise added to workout schedule!");
        return true;
    }

    // Add cardio exercise to workout schedule
    public boolean addCardioExerciseToSchedule(String scheduleId, String exerciseName, int durationSeconds) {
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        WorkoutSchedule schedule = allSchedules.stream()
            .filter(s -> s.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
            
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return false;
        }

        WorkoutSchedule.Exercise exercise = new WorkoutSchedule.Exercise(exerciseName, durationSeconds);
        schedule.addExercise(exercise);
        
        DataPersistence.saveWorkoutSchedules(allSchedules);
        System.out.println("Cardio exercise added to workout schedule!");
        return true;
    }

    // Update workout progress
    public boolean updateWorkoutProgress(String scheduleId, double completionPercentage, String notes) {
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        WorkoutSchedule schedule = allSchedules.stream()
            .filter(s -> s.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
            
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return false;
        }

        if (!InputValidator.isValidPercentage(String.valueOf(completionPercentage))) {
            System.out.println("Invalid completion percentage! Must be between 0 and 100.");
            return false;
        }

        schedule.updateProgress(completionPercentage, notes);
        
        // Update member's progress score
        Member member = userManager.findMemberById(schedule.getMemberId());
        if (member != null) {
            updateMemberProgressScore(member);
            if (completionPercentage >= 100) {
                member.markAttendance(); // Mark as attended if completed
            }
            userManager.saveAllUsers();
        }

        DataPersistence.saveWorkoutSchedules(allSchedules);
        System.out.println("Workout progress updated successfully!");
        return true;
    }

    // Complete workout
    public boolean completeWorkout(String scheduleId, String notes) {
        return updateWorkoutProgress(scheduleId, 100.0, notes);
    }

    // Cancel workout
    public boolean cancelWorkout(String scheduleId, String reason) {
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        WorkoutSchedule schedule = allSchedules.stream()
            .filter(s -> s.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
            
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return false;
        }

        schedule.setStatus(WorkoutSchedule.ScheduleStatus.CANCELLED);
        schedule.setProgressNotes(reason);
        
        DataPersistence.saveWorkoutSchedules(allSchedules);
        System.out.println("Workout cancelled successfully!");
        return true;
    }

    // Get schedules by member
    public List<WorkoutSchedule> getSchedulesByMember(String memberId) {
        return DataPersistence.loadWorkoutSchedules().stream()
            .filter(schedule -> schedule.getMemberId().equals(memberId))
            .collect(Collectors.toList());
    }

    // Get schedules by trainer
    public List<WorkoutSchedule> getSchedulesByTrainer(String trainerId) {
        return DataPersistence.loadWorkoutSchedules().stream()
            .filter(schedule -> schedule.getTrainerId().equals(trainerId))
            .collect(Collectors.toList());
    }

    // Get schedules by status
    public List<WorkoutSchedule> getSchedulesByStatus(WorkoutSchedule.ScheduleStatus status) {
        return DataPersistence.loadWorkoutSchedules().stream()
            .filter(schedule -> schedule.getStatus() == status)
            .collect(Collectors.toList());
    }

    // Get upcoming schedules
    public List<WorkoutSchedule> getUpcomingSchedules() {
        LocalDateTime now = LocalDateTime.now();
        return DataPersistence.loadWorkoutSchedules().stream()
            .filter(schedule -> schedule.getScheduledTime() != null && 
                               schedule.getScheduledTime().isAfter(now) &&
                               schedule.getStatus() == WorkoutSchedule.ScheduleStatus.SCHEDULED)
            .sorted(Comparator.comparing(WorkoutSchedule::getScheduledTime))
            .collect(Collectors.toList());
    }

    // Get overdue schedules
    public List<WorkoutSchedule> getOverdueSchedules() {
        LocalDateTime now = LocalDateTime.now();
        return DataPersistence.loadWorkoutSchedules().stream()
            .filter(schedule -> schedule.getScheduledTime() != null && 
                               schedule.getScheduledTime().isBefore(now) &&
                               schedule.getStatus() == WorkoutSchedule.ScheduleStatus.SCHEDULED)
            .collect(Collectors.toList());
    }

    // Get workout statistics for a member
    public Map<String, Object> getMemberWorkoutStats(String memberId) {
        List<WorkoutSchedule> memberSchedules = getSchedulesByMember(memberId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSchedules", memberSchedules.size());
        stats.put("completedWorkouts", memberSchedules.stream()
            .mapToLong(s -> s.getStatus() == WorkoutSchedule.ScheduleStatus.COMPLETED ? 1 : 0)
            .sum());
        stats.put("cancelledWorkouts", memberSchedules.stream()
            .mapToLong(s -> s.getStatus() == WorkoutSchedule.ScheduleStatus.CANCELLED ? 1 : 0)
            .sum());
        stats.put("averageCompletion", memberSchedules.stream()
            .mapToDouble(WorkoutSchedule::getCompletionPercentage)
            .average()
            .orElse(0.0));
        
        return stats;
    }

    // Get top performing members
    public List<Member> getTopPerformingMembers(int limit) {
        return userManager.getAllActiveMembers().stream()
            .sorted((m1, m2) -> Double.compare(m2.getProgressScore(), m1.getProgressScore()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    // Update member's overall progress score
    private void updateMemberProgressScore(Member member) {
        List<WorkoutSchedule> memberSchedules = getSchedulesByMember(member.getUserId());
        
        if (memberSchedules.isEmpty()) {
            member.setProgressScore(0.0);
            return;
        }

        double averageCompletion = memberSchedules.stream()
            .mapToDouble(WorkoutSchedule::getCompletionPercentage)
            .average()
            .orElse(0.0);

        double attendanceBonus = member.getAttendancePercentage() * 0.2; // 20% weight for attendance
        double completionWeight = averageCompletion * 0.8; // 80% weight for completion

        member.setProgressScore(Math.min(100.0, completionWeight + attendanceBonus));
    }

    // Schedule a workout with specific time
    public boolean scheduleWorkout(String scheduleId, String dateTimeStr) {
        List<WorkoutSchedule> allSchedules = DataPersistence.loadWorkoutSchedules();
        WorkoutSchedule schedule = allSchedules.stream()
            .filter(s -> s.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
            
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return false;
        }

        try {
            LocalDateTime scheduledTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            schedule.setScheduledTime(scheduledTime);
            DataPersistence.saveWorkoutSchedules(allSchedules);
            System.out.println("Workout scheduled successfully for: " + scheduledTime);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid date/time format! Use YYYY-MM-DDTHH:MM:SS");
            return false;
        }
    }

    // Find schedule by ID
    public WorkoutSchedule findScheduleById(String scheduleId) {
        return DataPersistence.loadWorkoutSchedules().stream()
            .filter(schedule -> schedule.getScheduleId().equals(scheduleId))
            .findFirst()
            .orElse(null);
    }

    // Display workout schedule details
    public void displayScheduleDetails(String scheduleId) {
        WorkoutSchedule schedule = findScheduleById(scheduleId);
        if (schedule == null) {
            System.out.println("Workout schedule not found!");
            return;
        }

        Member member = userManager.findMemberById(schedule.getMemberId());
        Trainer trainer = userManager.findTrainerById(schedule.getTrainerId());

        System.out.println("\n=== Workout Schedule Details ===");
        System.out.println("Schedule ID: " + schedule.getScheduleId());
        System.out.println("Workout Name: " + schedule.getWorkoutName());
        System.out.println("Member: " + (member != null ? member.getName() : "Unknown"));
        System.out.println("Trainer: " + (trainer != null ? trainer.getName() : "Unknown"));
        System.out.println("Scheduled Time: " + (schedule.getScheduledTime() != null ? schedule.getScheduledTime() : "Not scheduled"));
        System.out.println("Duration: " + schedule.getDurationMinutes() + " minutes");
        System.out.println("Type: " + schedule.getWorkoutType());
        System.out.println("Status: " + schedule.getStatus());
        System.out.println("Completion: " + schedule.getCompletionPercentage() + "%");
        System.out.println("Description: " + (schedule.getDescription() != null ? schedule.getDescription() : "No description"));
        
        if (!schedule.getExercises().isEmpty()) {
            System.out.println("\nExercises:");
            for (int i = 0; i < schedule.getExercises().size(); i++) {
                System.out.println((i + 1) + ". " + schedule.getExercises().get(i));
            }
        }
        
        if (schedule.getProgressNotes() != null) {
            System.out.println("\nProgress Notes: " + schedule.getProgressNotes());
        }
    }

    // Getters
    public List<WorkoutSchedule> getAllWorkoutSchedules() {
        return DataPersistence.loadWorkoutSchedules();
    }
} 