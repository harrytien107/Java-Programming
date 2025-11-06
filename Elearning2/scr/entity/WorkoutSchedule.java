package scr.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorkoutSchedule {
    private String scheduleId;
    private String memberId;
    private String trainerId;
    private String workoutName;
    private String description;
    private LocalDateTime scheduledTime;
    private int durationMinutes;
    private WorkoutType workoutType;
    private List<Exercise> exercises;
    private ScheduleStatus status;
    private String progressNotes;
    private double completionPercentage;

    public enum WorkoutType {
        CARDIO, STRENGTH, FLEXIBILITY, MIXED, REHABILITATION
    }

    public enum ScheduleStatus {
        SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, MISSED
    }

    public WorkoutSchedule(String scheduleId, String memberId, String trainerId, String workoutName) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.trainerId = trainerId;
        this.workoutName = workoutName;
        this.exercises = new ArrayList<>();
        this.status = ScheduleStatus.SCHEDULED;
        this.completionPercentage = 0.0;
        this.durationMinutes = 60; // default
        this.workoutType = WorkoutType.MIXED; // default
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void removeExercise(Exercise exercise) {
        exercises.remove(exercise);
    }

    public void updateProgress(double completionPercentage, String notes) {
        this.completionPercentage = Math.max(0, Math.min(100, completionPercentage));
        this.progressNotes = notes;
        
        if (completionPercentage >= 100) {
            this.status = ScheduleStatus.COMPLETED;
        } else if (completionPercentage > 0) {
            this.status = ScheduleStatus.IN_PROGRESS;
        }
    }

    // Getters and Setters
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public WorkoutType getWorkoutType() { return workoutType; }
    public void setWorkoutType(WorkoutType workoutType) { this.workoutType = workoutType; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }

    public ScheduleStatus getStatus() { return status; }
    public void setStatus(ScheduleStatus status) { this.status = status; }

    public String getProgressNotes() { return progressNotes; }
    public void setProgressNotes(String progressNotes) { this.progressNotes = progressNotes; }

    public double getCompletionPercentage() { return completionPercentage; }
    public void setCompletionPercentage(double completionPercentage) { this.completionPercentage = completionPercentage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutSchedule that = (WorkoutSchedule) o;
        return Objects.equals(scheduleId, that.scheduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId);
    }

    @Override
    public String toString() {
        return "WorkoutSchedule{" +
                "scheduleId='" + scheduleId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", trainerId='" + trainerId + '\'' +
                ", workoutName='" + workoutName + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", workoutType=" + workoutType +
                ", status=" + status +
                ", completionPercentage=" + completionPercentage + "%" +
                '}';
    }

    // Inner class for Exercise
    public static class Exercise {
        private String name;
        private int sets;
        private int reps;
        private double weight; // in kg
        private int durationSeconds;
        private String instructions;

        public Exercise(String name, int sets, int reps) {
            this.name = name;
            this.sets = sets;
            this.reps = reps;
        }

        public Exercise(String name, int durationSeconds) {
            this.name = name;
            this.durationSeconds = durationSeconds;
            this.sets = 1;
            this.reps = 1;
        }

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getSets() { return sets; }
        public void setSets(int sets) { this.sets = sets; }

        public int getReps() { return reps; }
        public void setReps(int reps) { this.reps = reps; }

        public double getWeight() { return weight; }
        public void setWeight(double weight) { this.weight = weight; }

        public int getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

        public String getInstructions() { return instructions; }
        public void setInstructions(String instructions) { this.instructions = instructions; }

        @Override
        public String toString() {
            if (durationSeconds > 0) {
                return name + " - " + durationSeconds + "s";
            } else {
                return name + " - " + sets + " sets x " + reps + " reps" + (weight > 0 ? " @ " + weight + "kg" : "");
            }
        }
    }
} 