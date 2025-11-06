package scr.entity;

import java.util.Objects;

public class SubscriptionPlan {
    private String planId;
    private String planName;
    private String description;
    private double price;
    private int durationMonths;
    private PlanType planType;
    private boolean isActive;
    private String benefits;
    private int maxWorkoutsPerWeek;
    private boolean includesPersonalTrainer;

    public enum PlanType {
        BASIC, STANDARD, PREMIUM, UNLIMITED
    }

    public SubscriptionPlan(String planId, String planName, double price, int durationMonths, PlanType planType) {
        this.planId = planId;
        this.planName = planName;
        this.price = price;
        this.durationMonths = durationMonths;
        this.planType = planType;
        this.isActive = true;
        setDefaultBenefits();
    }

    private void setDefaultBenefits() {
        switch (planType) {
            case BASIC:
                this.maxWorkoutsPerWeek = 3;
                this.includesPersonalTrainer = false;
                this.benefits = "Access to gym equipment, group classes";
                break;
            case STANDARD:
                this.maxWorkoutsPerWeek = 5;
                this.includesPersonalTrainer = false;
                this.benefits = "Access to gym equipment, all group classes, nutrition consultation";
                break;
            case PREMIUM:
                this.maxWorkoutsPerWeek = 7;
                this.includesPersonalTrainer = true;
                this.benefits = "Unlimited gym access, personal trainer, nutrition plan, priority booking";
                break;
            case UNLIMITED:
                this.maxWorkoutsPerWeek = Integer.MAX_VALUE;
                this.includesPersonalTrainer = true;
                this.benefits = "24/7 gym access, dedicated personal trainer, custom meal plans, VIP services";
                break;
        }
    }

    public double calculateMonthlyPrice() {
        return price / durationMonths;
    }

    public boolean isValidForMember(int workoutsPerWeek) {
        return workoutsPerWeek <= maxWorkoutsPerWeek;
    }

    // Getters and Setters
    public String getPlanId() { return planId; }
    public void setPlanId(String planId) { this.planId = planId; }

    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }

    public PlanType getPlanType() { return planType; }
    public void setPlanType(PlanType planType) { this.planType = planType; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public int getMaxWorkoutsPerWeek() { return maxWorkoutsPerWeek; }
    public void setMaxWorkoutsPerWeek(int maxWorkoutsPerWeek) { this.maxWorkoutsPerWeek = maxWorkoutsPerWeek; }

    public boolean isIncludesPersonalTrainer() { return includesPersonalTrainer; }
    public void setIncludesPersonalTrainer(boolean includesPersonalTrainer) { this.includesPersonalTrainer = includesPersonalTrainer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionPlan that = (SubscriptionPlan) o;
        return Objects.equals(planId, that.planId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planId);
    }

    @Override
    public String toString() {
        return "SubscriptionPlan{" +
                "planId='" + planId + '\'' +
                ", planName='" + planName + '\'' +
                ", price=" + price +
                ", durationMonths=" + durationMonths +
                ", planType=" + planType +
                ", monthlyPrice=" + String.format("%.2f", calculateMonthlyPrice()) +
                ", maxWorkoutsPerWeek=" + maxWorkoutsPerWeek +
                ", includesPersonalTrainer=" + includesPersonalTrainer +
                ", isActive=" + isActive +
                '}';
    }
} 