package scr.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Attendance {
    private String attendanceId;
    private String memberId;
    private String memberName;
    private LocalDate date;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String workoutScheduleId;
    private AttendanceStatus status;
    private String notes;

    public enum AttendanceStatus {
        CHECKED_IN, CHECKED_OUT, MISSED, LATE
    }

    public Attendance(String attendanceId, String memberId, String memberName) {
        this.attendanceId = attendanceId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.date = LocalDate.now();
        this.status = AttendanceStatus.CHECKED_IN;
    }

    public void checkIn() {
        this.checkInTime = LocalDateTime.now();
        this.status = AttendanceStatus.CHECKED_IN;
    }

    public void checkOut() {
        this.checkOutTime = LocalDateTime.now();
        this.status = AttendanceStatus.CHECKED_OUT;
    }

    public long getWorkoutDurationMinutes() {
        if (checkInTime != null && checkOutTime != null) {
            return java.time.Duration.between(checkInTime, checkOutTime).toMinutes();
        }
        return 0;
    }

    public boolean isLate(LocalDateTime scheduledTime) {
        if (checkInTime != null && scheduledTime != null) {
            return checkInTime.isAfter(scheduledTime.plusMinutes(15)); // 15 minutes grace period
        }
        return false;
    }

    // Getters and Setters
    public String getAttendanceId() { return attendanceId; }
    public void setAttendanceId(String attendanceId) { this.attendanceId = attendanceId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDateTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }

    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }

    public String getWorkoutScheduleId() { return workoutScheduleId; }
    public void setWorkoutScheduleId(String workoutScheduleId) { this.workoutScheduleId = workoutScheduleId; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(attendanceId, that.attendanceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendanceId);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "attendanceId='" + attendanceId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", date=" + date +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", status=" + status +
                ", durationMinutes=" + getWorkoutDurationMinutes() +
                '}';
    }
} 