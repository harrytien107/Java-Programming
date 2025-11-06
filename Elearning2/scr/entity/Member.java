package scr.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member extends User {
    private String membershipType;
    private LocalDate membershipStartDate;
    private LocalDate membershipEndDate;
    private boolean isActive;
    private String assignedTrainerId;
    private List<String> workoutScheduleIds;
    private double progressScore; // 0-100
    private int totalWorkouts;
    private int attendedWorkouts;
    
    public Member(String userId, String name, String email, String phone, String password, String membershipType) {
        super(userId, name, email, phone, password, UserRole.MEMBER);
        this.membershipType = membershipType;
        this.membershipStartDate = LocalDate.now();
        this.membershipEndDate = calculateEndDate(membershipType);
        this.isActive = true;
        this.workoutScheduleIds = new ArrayList<>();
        this.progressScore = 0.0;
        this.totalWorkouts = 0;
        this.attendedWorkouts = 0;
    }

    private LocalDate calculateEndDate(String membershipType) {
        LocalDate start = LocalDate.now();
        switch (membershipType.toLowerCase()) {
            case "monthly":
                return start.plusMonths(1);
            case "quarterly":
                return start.plusMonths(3);
            case "yearly":
                return start.plusYears(1);
            default:
                return start.plusMonths(1); // default to monthly
        }
    }

    public boolean isMembershipExpired() {
        return LocalDate.now().isAfter(membershipEndDate);
    }

    public void renewMembership(String membershipType) {
        this.membershipType = membershipType;
        this.membershipStartDate = LocalDate.now();
        this.membershipEndDate = calculateEndDate(membershipType);
        this.isActive = true;
    }

    public double getAttendancePercentage() {
        if (totalWorkouts == 0) return 0.0;
        return (double) attendedWorkouts / totalWorkouts * 100;
    }

    public void markAttendance() {
        this.attendedWorkouts++;
    }

    public void addWorkout() {
        this.totalWorkouts++;
    }

    // Getters and Setters
    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    public LocalDate getMembershipStartDate() { return membershipStartDate; }
    public void setMembershipStartDate(LocalDate membershipStartDate) { this.membershipStartDate = membershipStartDate; }

    public LocalDate getMembershipEndDate() { return membershipEndDate; }
    public void setMembershipEndDate(LocalDate membershipEndDate) { this.membershipEndDate = membershipEndDate; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getAssignedTrainerId() { return assignedTrainerId; }
    public void setAssignedTrainerId(String assignedTrainerId) { this.assignedTrainerId = assignedTrainerId; }

    public List<String> getWorkoutScheduleIds() { return workoutScheduleIds; }
    public void setWorkoutScheduleIds(List<String> workoutScheduleIds) { this.workoutScheduleIds = workoutScheduleIds; }

    public double getProgressScore() { return progressScore; }
    public void setProgressScore(double progressScore) { this.progressScore = progressScore; }

    public int getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }

    public int getAttendedWorkouts() { return attendedWorkouts; }
    public void setAttendedWorkouts(int attendedWorkouts) { this.attendedWorkouts = attendedWorkouts; }

    @Override
    public String toString() {
        return "Member{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", membershipType='" + membershipType + '\'' +
                ", membershipEndDate=" + membershipEndDate +
                ", isActive=" + isActive +
                ", progressScore=" + progressScore +
                ", attendancePercentage=" + String.format("%.1f", getAttendancePercentage()) + "%" +
                '}';
    }
} 