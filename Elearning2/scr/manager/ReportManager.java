package scr.manager;

import scr.entity.*;
import scr.util.DataPersistence;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ReportManager {
    private UserManager userManager;
    private AttendanceManager attendanceManager;
    private WorkoutManager workoutManager;
    private List<SubscriptionPlan> subscriptionPlans;

    public ReportManager(UserManager userManager, AttendanceManager attendanceManager, WorkoutManager workoutManager) {
        this.userManager = userManager;
        this.attendanceManager = attendanceManager;
        this.workoutManager = workoutManager;
        this.subscriptionPlans = new ArrayList<>();
        loadSubscriptionPlans();
    }

    // Revenue Reports
    public Map<String, Object> generateRevenueReport(LocalDate startDate, LocalDate endDate) {
        List<Member> activeMembers = userManager.getAllActiveMembers();
        
        Map<String, Object> revenueReport = new HashMap<>();
        revenueReport.put("reportPeriod", startDate + " to " + endDate);
        
        // Calculate total revenue from active memberships
        double totalRevenue = 0.0;
        Map<String, Double> revenueByType = new HashMap<>();
        Map<String, Integer> membersByType = new HashMap<>();
        
        for (Member member : activeMembers) {
            String membershipType = member.getMembershipType();
            double memberRevenue = calculateMemberRevenue(member, startDate, endDate);
            
            totalRevenue += memberRevenue;
            revenueByType.merge(membershipType, memberRevenue, Double::sum);
            membersByType.merge(membershipType, 1, Integer::sum);
        }
        
        revenueReport.put("totalRevenue", totalRevenue);
        revenueReport.put("revenueByMembershipType", revenueByType);
        revenueReport.put("membersByType", membersByType);
        revenueReport.put("totalActiveMembers", activeMembers.size());
        revenueReport.put("averageRevenuePerMember", activeMembers.size() > 0 ? totalRevenue / activeMembers.size() : 0.0);
        
        // Calculate projected monthly revenue
        double monthlyRevenue = calculateMonthlyRevenue();
        revenueReport.put("projectedMonthlyRevenue", monthlyRevenue);
        
        return revenueReport;
    }

    private double calculateMemberRevenue(Member member, LocalDate startDate, LocalDate endDate) {
        // Simplified revenue calculation based on membership type
        String membershipType = member.getMembershipType().toLowerCase();
        
        // Check if membership overlaps with report period
        if (member.getMembershipEndDate().isBefore(startDate) || 
            member.getMembershipStartDate().isAfter(endDate)) {
            return 0.0;
        }
        
        // Base pricing (you can modify these as needed)
        switch (membershipType) {
            case "monthly":
                return 50.0;
            case "quarterly":
                return 135.0; // 10% discount
            case "yearly":
                return 480.0; // 20% discount
            default:
                return 50.0;
        }
    }

    private double calculateMonthlyRevenue() {
        List<Member> activeMembers = userManager.getAllActiveMembers();
        double monthlyRevenue = 0.0;
        
        for (Member member : activeMembers) {
            String membershipType = member.getMembershipType().toLowerCase();
            switch (membershipType) {
                case "monthly":
                    monthlyRevenue += 50.0;
                    break;
                case "quarterly":
                    monthlyRevenue += 45.0; // 135/3
                    break;
                case "yearly":
                    monthlyRevenue += 40.0; // 480/12
                    break;
            }
        }
        
        return monthlyRevenue;
    }

    // Membership Reports
    public Map<String, Object> generateMembershipReport() {
        List<Member> allMembers = userManager.getMembers();
        List<Member> activeMembers = userManager.getAllActiveMembers();
        List<Member> expiredMembers = userManager.getAllExpiredMembers();
        
        Map<String, Object> membershipReport = new HashMap<>();
        membershipReport.put("totalMembers", allMembers.size());
        membershipReport.put("activeMembers", activeMembers.size());
        membershipReport.put("expiredMembers", expiredMembers.size());
        membershipReport.put("inactiveMembers", allMembers.size() - activeMembers.size());
        
        // Membership types breakdown
        Map<String, Long> membershipTypes = activeMembers.stream()
            .collect(Collectors.groupingBy(Member::getMembershipType, Collectors.counting()));
        membershipReport.put("membershipTypeBreakdown", membershipTypes);
        
        // Members expiring soon (next 30 days)
        LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
        List<Member> expiringSoon = activeMembers.stream()
            .filter(member -> member.getMembershipEndDate().isBefore(thirtyDaysFromNow))
            .collect(Collectors.toList());
        membershipReport.put("membersExpiringSoon", expiringSoon.size());
        
        // Average membership duration
        OptionalDouble avgDuration = allMembers.stream()
            .filter(member -> member.getMembershipStartDate() != null && member.getMembershipEndDate() != null)
            .mapToLong(member -> member.getMembershipStartDate().until(member.getMembershipEndDate()).getDays())
            .average();
        membershipReport.put("averageMembershipDuration", avgDuration.orElse(0.0));
        
        return membershipReport;
    }

    // Attendance Reports
    public Map<String, Object> generateAttendanceReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> attendanceStats = attendanceManager.getGymAttendanceStats(
            (int) startDate.until(endDate).getDays());
        Map<String, Object> attendanceSummary = attendanceManager.getAttendanceSummaryReport(startDate, endDate);
        
        Map<String, Object> attendanceReport = new HashMap<>();
        attendanceReport.putAll(attendanceStats);
        attendanceReport.putAll(attendanceSummary);
        
        // Calculate attendance trends
        List<Member> activeMembers = userManager.getAllActiveMembers();
        double totalAttendancePercentage = 0.0;
        int membersWithAttendance = 0;
        
        for (Member member : activeMembers) {
            double memberAttendance = member.getAttendancePercentage();
            if (memberAttendance > 0) {
                totalAttendancePercentage += memberAttendance;
                membersWithAttendance++;
            }
        }
        
        double averageAttendancePercentage = membersWithAttendance > 0 ? 
            totalAttendancePercentage / membersWithAttendance : 0.0;
        attendanceReport.put("averageAttendancePercentage", averageAttendancePercentage);
        
        return attendanceReport;
    }

    // Performance Reports
    public Map<String, Object> generatePerformanceReport() {
        List<Member> topPerformers = workoutManager.getTopPerformingMembers(10);
        List<Member> activeMembers = userManager.getAllActiveMembers();
        
        Map<String, Object> performanceReport = new HashMap<>();
        
        // Top performing members
        List<Map<String, Object>> topPerformerDetails = topPerformers.stream()
            .map(member -> {
                Map<String, Object> memberInfo = new HashMap<>();
                memberInfo.put("name", member.getName());
                memberInfo.put("userId", member.getUserId());
                memberInfo.put("progressScore", member.getProgressScore());
                memberInfo.put("attendancePercentage", member.getAttendancePercentage());
                memberInfo.put("totalWorkouts", member.getTotalWorkouts());
                memberInfo.put("attendedWorkouts", member.getAttendedWorkouts());
                return memberInfo;
            })
            .collect(Collectors.toList());
        
        performanceReport.put("topPerformingMembers", topPerformerDetails);
        
        // Overall performance statistics
        OptionalDouble avgProgressScore = activeMembers.stream()
            .mapToDouble(Member::getProgressScore)
            .average();
        performanceReport.put("averageProgressScore", avgProgressScore.orElse(0.0));
        
        long totalWorkoutsCompleted = activeMembers.stream()
            .mapToLong(Member::getAttendedWorkouts)
            .sum();
        performanceReport.put("totalWorkoutsCompleted", totalWorkoutsCompleted);
        
        long totalWorkoutsScheduled = activeMembers.stream()
            .mapToLong(Member::getTotalWorkouts)
            .sum();
        performanceReport.put("totalWorkoutsScheduled", totalWorkoutsScheduled);
        
        double overallCompletionRate = totalWorkoutsScheduled > 0 ? 
            (double) totalWorkoutsCompleted / totalWorkoutsScheduled * 100 : 0.0;
        performanceReport.put("overallWorkoutCompletionRate", overallCompletionRate);
        
        return performanceReport;
    }

    // Trainer Performance Report
    public Map<String, Object> generateTrainerPerformanceReport() {
        List<Trainer> activeTrainers = userManager.getAllActiveTrainers();
        
        Map<String, Object> trainerReport = new HashMap<>();
        List<Map<String, Object>> trainerDetails = new ArrayList<>();
        
        for (Trainer trainer : activeTrainers) {
            Map<String, Object> trainerInfo = new HashMap<>();
            trainerInfo.put("name", trainer.getName());
            trainerInfo.put("userId", trainer.getUserId());
            trainerInfo.put("specialization", trainer.getSpecialization());
            trainerInfo.put("experience", trainer.getExperience());
            trainerInfo.put("assignedMembers", trainer.getAssignedMemberIds().size());
            
            // Calculate trainer's member performance average
            double avgMemberProgress = trainer.getAssignedMemberIds().stream()
                .mapToDouble(memberId -> {
                    Member member = userManager.findMemberById(memberId);
                    return member != null ? member.getProgressScore() : 0.0;
                })
                .average()
                .orElse(0.0);
            
            trainerInfo.put("averageMemberProgressScore", avgMemberProgress);
            
            // Get workout schedules managed by this trainer
            List<WorkoutSchedule> trainerSchedules = workoutManager.getSchedulesByTrainer(trainer.getUserId());
            trainerInfo.put("totalSchedulesManaged", trainerSchedules.size());
            
            long completedSchedules = trainerSchedules.stream()
                .filter(schedule -> schedule.getStatus() == WorkoutSchedule.ScheduleStatus.COMPLETED)
                .count();
            
            double completionRate = trainerSchedules.size() > 0 ? 
                (double) completedSchedules / trainerSchedules.size() * 100 : 0.0;
            trainerInfo.put("scheduleCompletionRate", completionRate);
            
            trainerDetails.add(trainerInfo);
        }
        
        // Sort trainers by performance (average member progress score)
        trainerDetails.sort((t1, t2) -> Double.compare(
            (Double) t2.get("averageMemberProgressScore"),
            (Double) t1.get("averageMemberProgressScore")
        ));
        
        trainerReport.put("trainerPerformanceDetails", trainerDetails);
        trainerReport.put("totalActiveTrainers", activeTrainers.size());
        
        double avgTrainerPerformance = trainerDetails.stream()
            .mapToDouble(trainer -> (Double) trainer.get("averageMemberProgressScore"))
            .average()
            .orElse(0.0);
        trainerReport.put("averageTrainerPerformanceScore", avgTrainerPerformance);
        
        return trainerReport;
    }

    // Comprehensive Dashboard Report
    public Map<String, Object> generateDashboardReport() {
        Map<String, Object> dashboard = new HashMap<>();
        
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);
        
        // Quick stats
        dashboard.put("totalMembers", userManager.getMembers().size());
        dashboard.put("activeMembers", userManager.getAllActiveMembers().size());
        dashboard.put("totalTrainers", userManager.getAllActiveTrainers().size());
        dashboard.put("todayAttendance", attendanceManager.getTodayAttendance().size());
        
        // Revenue summary
        Map<String, Object> revenueReport = generateRevenueReport(thirtyDaysAgo, today);
        dashboard.put("monthlyRevenue", revenueReport.get("projectedMonthlyRevenue"));
        dashboard.put("totalRevenue", revenueReport.get("totalRevenue"));
        
        // Performance summary
        Map<String, Object> performanceReport = generatePerformanceReport();
        dashboard.put("averageProgressScore", performanceReport.get("averageProgressScore"));
        dashboard.put("workoutCompletionRate", performanceReport.get("overallWorkoutCompletionRate"));
        
        // Upcoming schedules
        List<WorkoutSchedule> upcomingSchedules = workoutManager.getUpcomingSchedules();
        dashboard.put("upcomingWorkouts", Math.min(5, upcomingSchedules.size()));
        
        // Members expiring soon
        LocalDate thirtyDaysFromNow = today.plusDays(30);
        long expiringSoon = userManager.getAllActiveMembers().stream()
            .filter(member -> member.getMembershipEndDate().isBefore(thirtyDaysFromNow))
            .count();
        dashboard.put("membershipsExpiringSoon", expiringSoon);
        
        return dashboard;
    }

    // Export Reports
    public void exportRevenueReportToCsv(LocalDate startDate, LocalDate endDate, String filename) {
        Map<String, Object> revenueReport = generateRevenueReport(startDate, endDate);
        
        String[] headers = {"Metric", "Value"};
        List<String[]> csvData = new ArrayList<>();
        
        csvData.add(new String[]{"Report Period", startDate + " to " + endDate});
        csvData.add(new String[]{"Total Revenue", String.format("%.2f", (Double) revenueReport.get("totalRevenue"))});
        csvData.add(new String[]{"Total Active Members", String.valueOf(revenueReport.get("totalActiveMembers"))});
        csvData.add(new String[]{"Average Revenue Per Member", String.format("%.2f", (Double) revenueReport.get("averageRevenuePerMember"))});
        csvData.add(new String[]{"Projected Monthly Revenue", String.format("%.2f", (Double) revenueReport.get("projectedMonthlyRevenue"))});
        
        DataPersistence.exportReportToCsv(filename, headers, csvData);
    }

    public void exportMembershipReportToCsv(String filename) {
        Map<String, Object> membershipReport = generateMembershipReport();
        
        String[] headers = {"Metric", "Value"};
        List<String[]> csvData = new ArrayList<>();
        
        csvData.add(new String[]{"Total Members", String.valueOf(membershipReport.get("totalMembers"))});
        csvData.add(new String[]{"Active Members", String.valueOf(membershipReport.get("activeMembers"))});
        csvData.add(new String[]{"Expired Members", String.valueOf(membershipReport.get("expiredMembers"))});
        csvData.add(new String[]{"Members Expiring Soon", String.valueOf(membershipReport.get("membersExpiringSoon"))});
        csvData.add(new String[]{"Average Membership Duration (days)", String.format("%.1f", (Double) membershipReport.get("averageMembershipDuration"))});
        
        DataPersistence.exportReportToCsv(filename, headers, csvData);
    }

    // Display Reports
    public void displayDashboard() {
        Map<String, Object> dashboard = generateDashboardReport();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           GYM MANAGEMENT DASHBOARD");
        System.out.println("=".repeat(50));
        System.out.println("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        System.out.println();
        
        System.out.println("QUICK STATS:");
        System.out.println("  - Total Members: " + dashboard.get("totalMembers"));
        System.out.println("  - Active Members: " + dashboard.get("activeMembers"));
        System.out.println("  - Active Trainers: " + dashboard.get("totalTrainers"));
        System.out.println("  - Today's Attendance: " + dashboard.get("todayAttendance"));
        System.out.println();
        
        System.out.println("REVENUE:");
        System.out.println("  - Monthly Revenue: $" + String.format("%.2f", (Double) dashboard.get("monthlyRevenue")));
        System.out.println("  - Total Revenue (30 days): $" + String.format("%.2f", (Double) dashboard.get("totalRevenue")));
        System.out.println();
        
        System.out.println("PERFORMANCE:");
        System.out.println("  - Average Progress Score: " + String.format("%.1f%%", (Double) dashboard.get("averageProgressScore")));
        System.out.println("  - Workout Completion Rate: " + String.format("%.1f%%", (Double) dashboard.get("workoutCompletionRate")));
        System.out.println();
        
        System.out.println("ALERTS:");
        System.out.println("  - Upcoming Workouts (today): " + dashboard.get("upcomingWorkouts"));
        System.out.println("  - Memberships Expiring Soon: " + dashboard.get("membershipsExpiringSoon"));
        
        System.out.println("=".repeat(50));
    }

    public void displayRevenueReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = generateRevenueReport(startDate, endDate);
        
        System.out.println("\n=== REVENUE REPORT (" + startDate + " to " + endDate + ") ===");
        System.out.println("Total Revenue: $" + String.format("%.2f", (Double) report.get("totalRevenue")));
        System.out.println("Total Active Members: " + report.get("totalActiveMembers"));
        System.out.println("Average Revenue per Member: $" + String.format("%.2f", (Double) report.get("averageRevenuePerMember")));
        System.out.println("Projected Monthly Revenue: $" + String.format("%.2f", (Double) report.get("projectedMonthlyRevenue")));
        
        @SuppressWarnings("unchecked")
        Map<String, Double> revenueByType = (Map<String, Double>) report.get("revenueByMembershipType");
        System.out.println("\nRevenue by Membership Type:");
        revenueByType.forEach((type, revenue) -> 
            System.out.println("  " + type + ": $" + String.format("%.2f", revenue)));
        
        @SuppressWarnings("unchecked")
        Map<String, Integer> membersByType = (Map<String, Integer>) report.get("membersByType");
        System.out.println("\nMembers by Type:");
        membersByType.forEach((type, count) -> 
            System.out.println("  " + type + ": " + count + " members"));
    }

    // Subscription Plan Management
    public void addSubscriptionPlan(String planId, String planName, double price, int durationMonths, SubscriptionPlan.PlanType planType) {
        SubscriptionPlan plan = new SubscriptionPlan(planId, planName, price, durationMonths, planType);
        subscriptionPlans.add(plan);
        saveSubscriptionPlans();
        System.out.println("Subscription plan added successfully!");
    }

    public List<SubscriptionPlan> getActiveSubscriptionPlans() {
        return subscriptionPlans.stream()
            .filter(SubscriptionPlan::isActive)
            .collect(Collectors.toList());
    }

    private void loadSubscriptionPlans() {
        subscriptionPlans = DataPersistence.loadSubscriptionPlans();
        
        // Create default plans if none exist
        if (subscriptionPlans.isEmpty()) {
            createDefaultSubscriptionPlans();
        }
    }

    private void createDefaultSubscriptionPlans() {
        addSubscriptionPlan("BASIC_MONTHLY", "Basic Monthly", 50.0, 1, SubscriptionPlan.PlanType.BASIC);
        addSubscriptionPlan("STANDARD_MONTHLY", "Standard Monthly", 75.0, 1, SubscriptionPlan.PlanType.STANDARD);
        addSubscriptionPlan("PREMIUM_MONTHLY", "Premium Monthly", 100.0, 1, SubscriptionPlan.PlanType.PREMIUM);
        addSubscriptionPlan("BASIC_YEARLY", "Basic Yearly", 480.0, 12, SubscriptionPlan.PlanType.BASIC);
        addSubscriptionPlan("PREMIUM_YEARLY", "Premium Yearly", 960.0, 12, SubscriptionPlan.PlanType.PREMIUM);
    }

    private void saveSubscriptionPlans() {
        DataPersistence.saveSubscriptionPlans(subscriptionPlans);
    }
} 