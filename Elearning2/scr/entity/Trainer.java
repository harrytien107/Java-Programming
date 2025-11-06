package scr.entity;

import java.util.ArrayList;
import java.util.List;

public class Trainer extends User {
    private String specialization;
    private int experience; // years of experience
    private List<String> assignedMemberIds;
    
    public Trainer(String userId, String name, String email, String phone, String password, String specialization, int experience) {
        super(userId, name, email, phone, password, UserRole.TRAINER);
        this.specialization = specialization;
        this.experience = experience;
        this.assignedMemberIds = new ArrayList<>();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public List<String> getAssignedMemberIds() {
        return assignedMemberIds;
    }

    public void setAssignedMemberIds(List<String> assignedMemberIds) {
        this.assignedMemberIds = assignedMemberIds;
    }

    public void assignMember(String memberId) {
        if (!assignedMemberIds.contains(memberId)) {
            assignedMemberIds.add(memberId);
        }
    }

    public void unassignMember(String memberId) {
        assignedMemberIds.remove(memberId);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", experience=" + experience +
                ", assignedMembers=" + assignedMemberIds.size() +
                ", isActive=" + isActive +
                '}';
    }
} 