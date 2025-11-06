package scr.manager;

import scr.entity.*;
import scr.util.DataPersistence;
import scr.util.InputValidator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AttendanceManager {
    private List<Attendance> attendanceRecords;
    private UserManager userManager;

    public AttendanceManager(UserManager userManager) {
        this.userManager = userManager;
        this.attendanceRecords = new ArrayList<>();
        loadAttendanceRecords();
    }

    // Check in member
    public boolean checkInMember(String memberId) {
        Member member = userManager.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return false;
        }

        if (!member.isActive() || member.isMembershipExpired()) {
            System.out.println("Member is inactive or membership has expired!");
            return false;
        }

        // Check if member is already checked in today
        Optional<Attendance> todayAttendance = getTodayAttendance(memberId);
        if (todayAttendance.isPresent() && 
            todayAttendance.get().getStatus() == Attendance.AttendanceStatus.CHECKED_IN) {
            System.out.println("Member is already checked in today!");
            return false;
        }

        String attendanceId = generateAttendanceId();
        Attendance attendance = new Attendance(attendanceId, memberId, member.getName());
        attendance.checkIn();
        
        attendanceRecords.add(attendance);
        saveAttendanceRecords();
        
        System.out.println("Member " + member.getName() + " checked in successfully at " + 
                          attendance.getCheckInTime().toLocalTime());
        return true;
    }

    // Check out member
    public boolean checkOutMember(String memberId) {
        Optional<Attendance> todayAttendance = getTodayAttendance(memberId);
        
        if (!todayAttendance.isPresent()) {
            System.out.println("No check-in record found for today!");
            return false;
        }

        Attendance attendance = todayAttendance.get();
        if (attendance.getStatus() == Attendance.AttendanceStatus.CHECKED_OUT) {
            System.out.println("Member is already checked out!");
            return false;
        }

        attendance.checkOut();
        saveAttendanceRecords();
        
        System.out.println("Member checked out successfully at " + 
                          attendance.getCheckOutTime().toLocalTime() + 
                          " (Duration: " + attendance.getWorkoutDurationMinutes() + " minutes)");
        return true;
    }

    // Mark member as late
    public boolean markMemberLate(String memberId, LocalDateTime scheduledTime) {
        Optional<Attendance> todayAttendance = getTodayAttendance(memberId);
        
        if (!todayAttendance.isPresent()) {
            System.out.println("No attendance record found for today!");
            return false;
        }

        Attendance attendance = todayAttendance.get();
        if (attendance.isLate(scheduledTime)) {
            attendance.setStatus(Attendance.AttendanceStatus.LATE);
            saveAttendanceRecords();
            System.out.println("Member marked as late for scheduled workout");
            return true;
        }

        return false;
    }

    // Mark member as missed (no show)
    public boolean markMemberMissed(String memberId, String workoutScheduleId) {
        Member member = userManager.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return false;
        }

        String attendanceId = generateAttendanceId();
        Attendance attendance = new Attendance(attendanceId, memberId, member.getName());
        attendance.setStatus(Attendance.AttendanceStatus.MISSED);
        attendance.setWorkoutScheduleId(workoutScheduleId);
        attendance.setNotes("No show for scheduled workout");
        
        attendanceRecords.add(attendance);
        saveAttendanceRecords();
        
        System.out.println("Member " + member.getName() + " marked as missed for workout");
        return true;
    }

    // Get today's attendance for a member
    private Optional<Attendance> getTodayAttendance(String memberId) {
        LocalDate today = LocalDate.now();
        return attendanceRecords.stream()
            .filter(attendance -> attendance.getMemberId().equals(memberId) && 
                                 attendance.getDate().equals(today))
            .findFirst();
    }

    // Get attendance records by member
    public List<Attendance> getAttendanceByMember(String memberId) {
        return attendanceRecords.stream()
            .filter(attendance -> attendance.getMemberId().equals(memberId))
            .sorted(Comparator.comparing(Attendance::getDate).reversed())
            .collect(Collectors.toList());
    }

    // Get attendance records by date range
    public List<Attendance> getAttendanceByDateRange(LocalDate startDate, LocalDate endDate) {
        return attendanceRecords.stream()
            .filter(attendance -> !attendance.getDate().isBefore(startDate) && 
                                 !attendance.getDate().isAfter(endDate))
            .sorted(Comparator.comparing(Attendance::getDate).reversed())
            .collect(Collectors.toList());
    }

    // Get attendance records for today
    public List<Attendance> getTodayAttendance() {
        LocalDate today = LocalDate.now();
        return attendanceRecords.stream()
            .filter(attendance -> attendance.getDate().equals(today))
            .sorted(Comparator.comparing(attendance -> attendance.getCheckInTime(), 
                                      Comparator.nullsLast(Comparator.naturalOrder())))
            .collect(Collectors.toList());
    }

    // Get attendance statistics for a member
    public Map<String, Object> getMemberAttendanceStats(String memberId, int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<Attendance> memberAttendance = getAttendanceByMember(memberId).stream()
            .filter(attendance -> !attendance.getDate().isBefore(startDate))
            .collect(Collectors.toList());

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDays", days);
        stats.put("attendedDays", memberAttendance.stream()
            .filter(a -> a.getStatus() == Attendance.AttendanceStatus.CHECKED_IN ||
                        a.getStatus() == Attendance.AttendanceStatus.CHECKED_OUT ||
                        a.getStatus() == Attendance.AttendanceStatus.LATE)
            .count());
        stats.put("missedDays", memberAttendance.stream()
            .filter(a -> a.getStatus() == Attendance.AttendanceStatus.MISSED)
            .count());
        stats.put("lateDays", memberAttendance.stream()
            .filter(a -> a.getStatus() == Attendance.AttendanceStatus.LATE)
            .count());
        
        long attendedDays = (Long) stats.get("attendedDays");
        stats.put("attendancePercentage", days > 0 ? (attendedDays * 100.0) / days : 0.0);
        
        OptionalDouble avgWorkoutDuration = memberAttendance.stream()
            .filter(a -> a.getWorkoutDurationMinutes() > 0)
            .mapToLong(Attendance::getWorkoutDurationMinutes)
            .average();
        stats.put("averageWorkoutDuration", avgWorkoutDuration.orElse(0.0));

        return stats;
    }

    // Get overall gym attendance statistics
    public Map<String, Object> getGymAttendanceStats(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);
        
        List<Attendance> recentAttendance = getAttendanceByDateRange(startDate, endDate);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalVisits", recentAttendance.size());
        stats.put("uniqueMembers", recentAttendance.stream()
            .map(Attendance::getMemberId)
            .distinct()
            .count());
        stats.put("averageVisitsPerDay", days > 0 ? (double) recentAttendance.size() / days : 0.0);
        
        OptionalDouble avgWorkoutDuration = recentAttendance.stream()
            .filter(a -> a.getWorkoutDurationMinutes() > 0)
            .mapToLong(Attendance::getWorkoutDurationMinutes)
            .average();
        stats.put("averageWorkoutDuration", avgWorkoutDuration.orElse(0.0));

        // Peak hours analysis
        Map<Integer, Long> hourlyVisits = recentAttendance.stream()
            .filter(a -> a.getCheckInTime() != null)
            .collect(Collectors.groupingBy(
                a -> a.getCheckInTime().getHour(),
                Collectors.counting()
            ));
        
        Optional<Map.Entry<Integer, Long>> peakHour = hourlyVisits.entrySet().stream()
            .max(Map.Entry.comparingByValue());
        
        stats.put("peakHour", peakHour.isPresent() ? peakHour.get().getKey() : "N/A");
        stats.put("peakHourVisits", peakHour.isPresent() ? peakHour.get().getValue() : 0);

        return stats;
    }

    // Get attendance summary report
    public Map<String, Object> getAttendanceSummaryReport(LocalDate startDate, LocalDate endDate) {
        List<Attendance> periodAttendance = getAttendanceByDateRange(startDate, endDate);

        Map<String, Object> summary = new HashMap<>();
        summary.put("reportPeriod", startDate + " to " + endDate);
        summary.put("totalRecords", periodAttendance.size());
        
        // Group by status
        Map<Attendance.AttendanceStatus, Long> statusCounts = periodAttendance.stream()
            .collect(Collectors.groupingBy(Attendance::getStatus, Collectors.counting()));
        
        summary.put("checkedIn", statusCounts.getOrDefault(Attendance.AttendanceStatus.CHECKED_IN, 0L));
        summary.put("checkedOut", statusCounts.getOrDefault(Attendance.AttendanceStatus.CHECKED_OUT, 0L));
        summary.put("late", statusCounts.getOrDefault(Attendance.AttendanceStatus.LATE, 0L));
        summary.put("missed", statusCounts.getOrDefault(Attendance.AttendanceStatus.MISSED, 0L));

        // Top attending members
        Map<String, Long> memberVisits = periodAttendance.stream()
            .filter(a -> a.getStatus() != Attendance.AttendanceStatus.MISSED)
            .collect(Collectors.groupingBy(Attendance::getMemberId, Collectors.counting()));
        
        List<String> topMembers = memberVisits.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                Member member = userManager.findMemberById(entry.getKey());
                return (member != null ? member.getName() : entry.getKey()) + " (" + entry.getValue() + " visits)";
            })
            .collect(Collectors.toList());
        
        summary.put("topAttendingMembers", topMembers);

        return summary;
    }

    // Display member attendance details
    public void displayMemberAttendance(String memberId, int days) {
        Member member = userManager.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.println("\n=== Attendance Report for " + member.getName() + " (Last " + days + " days) ===");
        
        Map<String, Object> stats = getMemberAttendanceStats(memberId, days);
        System.out.println("Attendance Percentage: " + String.format("%.1f%%", (Double) stats.get("attendancePercentage")));
        System.out.println("Days Attended: " + stats.get("attendedDays"));
        System.out.println("Days Missed: " + stats.get("missedDays"));
        System.out.println("Late Arrivals: " + stats.get("lateDays"));
        System.out.println("Average Workout Duration: " + String.format("%.1f minutes", (Double) stats.get("averageWorkoutDuration")));

        List<Attendance> recentAttendance = getAttendanceByMember(memberId).stream()
            .limit(10)
            .collect(Collectors.toList());

        if (!recentAttendance.isEmpty()) {
            System.out.println("\nRecent Attendance Records:");
            for (Attendance attendance : recentAttendance) {
                System.out.printf("%s - %s", attendance.getDate(), attendance.getStatus());
                if (attendance.getCheckInTime() != null) {
                    System.out.printf(" (In: %s", attendance.getCheckInTime().toLocalTime());
                    if (attendance.getCheckOutTime() != null) {
                        System.out.printf(", Out: %s, Duration: %d min", 
                                        attendance.getCheckOutTime().toLocalTime(),
                                        attendance.getWorkoutDurationMinutes());
                    }
                    System.out.print(")");
                }
                System.out.println();
            }
        }
    }

    // Generate unique attendance ID
    private String generateAttendanceId() {
        return "ATT" + System.currentTimeMillis() + "_" + (attendanceRecords.size() + 1);
    }

    // Export attendance report to CSV
    public void exportAttendanceReport(LocalDate startDate, LocalDate endDate, String filename) {
        List<Attendance> reportData = getAttendanceByDateRange(startDate, endDate);
        
        String[] headers = {"Date", "Member ID", "Member Name", "Check In", "Check Out", 
                           "Duration (min)", "Status", "Notes"};
        
        List<String[]> csvData = reportData.stream()
            .map(attendance -> new String[]{
                attendance.getDate().toString(),
                attendance.getMemberId(),
                attendance.getMemberName(),
                attendance.getCheckInTime() != null ? attendance.getCheckInTime().toString() : "",
                attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().toString() : "",
                String.valueOf(attendance.getWorkoutDurationMinutes()),
                attendance.getStatus().toString(),
                attendance.getNotes() != null ? attendance.getNotes() : ""
            })
            .collect(Collectors.toList());
        
        DataPersistence.exportReportToCsv(filename, headers, csvData);
    }

    // Data persistence
    private void loadAttendanceRecords() {
        attendanceRecords = DataPersistence.loadAttendance();
    }

    private void saveAttendanceRecords() {
        DataPersistence.saveAttendance(attendanceRecords);
    }

    // Getters
    public List<Attendance> getAllAttendanceRecords() {
        return new ArrayList<>(attendanceRecords);
    }
} 