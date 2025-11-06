package scr.entity;

public class Admin extends User {
    private String adminLevel;
    
    public Admin(String userId, String name, String email, String phone, String password, String adminLevel) {
        super(userId, name, email, phone, password, UserRole.ADMIN);
        this.adminLevel = adminLevel;
    }

    public String getAdminLevel() {
        return adminLevel;
    }

    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", adminLevel='" + adminLevel + '\'' +
                ", isActive=" + isActive +
                '}';
    }
} 