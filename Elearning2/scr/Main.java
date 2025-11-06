package scr;

/**
 * Main class to start the Gym Management System
 * 
 * @author Gym Management System Team
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Gym Management System...");
        System.out.println("====================================");
        
        try {
            GymManagementSystem gms = new GymManagementSystem();
            gms.start();
        } catch (Exception e) {
            System.err.println("Failed to start Gym Management System: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
