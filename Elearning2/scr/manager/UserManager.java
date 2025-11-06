package scr.manager;

import scr.entity.*;
import scr.util.DataPersistence;
import scr.util.InputValidator;
import java.util.*;
import java.util.stream.Collectors;

public class UserManager {
    private List<Member> members;
    private List<Trainer> trainers;
    private List<Admin> admins;
    private User currentUser;

    public UserManager() {
        this.members = new ArrayList<>();
        this.trainers = new ArrayList<>();
        this.admins = new ArrayList<>();
        loadAllUsers();
    }

    // Authentication and session management
    public User login(String userId, String password) {
        // Check all user types
        Optional<User> user = findUserByCredentials(userId, password);
        if (user.isPresent() && user.get().isActive()) {
            currentUser = user.get();
            return currentUser;
        }
        return null;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Goodbye, " + currentUser.getName());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    private Optional<User> findUserByCredentials(String userId, String password) {
        // Check admins
        Optional<Admin> admin = admins.stream()
            .filter(a -> a.getUserId().equals(userId) && a.getPassword().equals(password))
            .findFirst();
        if (admin.isPresent()) return Optional.of(admin.get());

        // Check trainers
        Optional<Trainer> trainer = trainers.stream()
            .filter(t -> t.getUserId().equals(userId) && t.getPassword().equals(password))
            .findFirst();
        if (trainer.isPresent()) return Optional.of(trainer.get());

        // Check members
        Optional<Member> member = members.stream()
            .filter(m -> m.getUserId().equals(userId) && m.getPassword().equals(password))
            .findFirst();
        if (member.isPresent()) return Optional.of(member.get());

        return Optional.empty();
    }

    // Member management
    public boolean addMember(String userId, String name, String email, String phone, String password, String membershipType) {
        if (!validateMemberInput(userId, name, email, phone, membershipType)) {
            return false;
        }

        if (isUserIdTaken(userId)) {
            System.out.println("Error: User ID already exists!");
            return false;
        }

        Member member = new Member(userId, name, email, phone, password, membershipType);
        members.add(member);
        saveAllUsers();
        System.out.println("Member added successfully: " + member.getName());
        return true;
    }

    // Public member registration for login menu
    public boolean registerMember(String userId, String name, String email, String phone, String password, String membershipType) {
        return addMember(userId, name, email, phone, password, membershipType);
    }

    public boolean updateMember(String userId, String name, String email, String phone) {
        Member member = findMemberById(userId);
        if (member == null) {
            System.out.println("Member not found!");
            return false;
        }

        if (InputValidator.isValidName(name)) member.setName(name);
        if (InputValidator.isValidEmail(email)) member.setEmail(email);
        if (InputValidator.isValidPhone(phone)) member.setPhone(phone);

        saveAllUsers();
        System.out.println("Member updated successfully!");
        return true;
    }

    public boolean deleteMember(String userId) {
        Member member = findMemberById(userId);
        if (member == null) {
            System.out.println("Member not found!");
            return false;
        }

        member.setActive(false);
        saveAllUsers();
        System.out.println("Member deactivated successfully!");
        return true;
    }

    public Member findMemberById(String userId) {
        return members.stream()
            .filter(m -> m.getUserId().equals(userId))
            .findFirst()
            .orElse(null);
    }

    public List<Member> getAllActiveMembers() {
        return members.stream()
            .filter(Member::isActive)
            .collect(Collectors.toList());
    }

    public List<Member> getAllExpiredMembers() {
        return members.stream()
            .filter(Member::isMembershipExpired)
            .collect(Collectors.toList());
    }

    // Trainer management
    public boolean addTrainer(String userId, String name, String email, String phone, String password, String specialization, int experience) {
        if (!validateTrainerInput(userId, name, email, phone, specialization, experience)) {
            return false;
        }

        if (isUserIdTaken(userId)) {
            System.out.println("Error: User ID already exists!");
            return false;
        }

        Trainer trainer = new Trainer(userId, name, email, phone, password, specialization, experience);
        trainers.add(trainer);
        saveAllUsers();
        System.out.println("Trainer added successfully: " + trainer.getName());
        return true;
    }

    public boolean updateTrainer(String userId, String name, String email, String phone, String specialization, int experience) {
        Trainer trainer = findTrainerById(userId);
        if (trainer == null) {
            System.out.println("Trainer not found!");
            return false;
        }

        if (InputValidator.isValidName(name)) trainer.setName(name);
        if (InputValidator.isValidEmail(email)) trainer.setEmail(email);
        if (InputValidator.isValidPhone(phone)) trainer.setPhone(phone);
        if (InputValidator.isNotEmpty(specialization)) trainer.setSpecialization(specialization);
        if (experience >= 0) trainer.setExperience(experience);

        saveAllUsers();
        System.out.println("Trainer updated successfully!");
        return true;
    }

    public boolean deleteTrainer(String userId) {
        Trainer trainer = findTrainerById(userId);
        if (trainer == null) {
            System.out.println("Trainer not found!");
            return false;
        }

        trainer.setActive(false);
        saveAllUsers();
        System.out.println("Trainer deactivated successfully!");
        return true;
    }

    public Trainer findTrainerById(String userId) {
        return trainers.stream()
            .filter(t -> t.getUserId().equals(userId))
            .findFirst()
            .orElse(null);
    }

    public List<Trainer> getAllActiveTrainers() {
        return trainers.stream()
            .filter(Trainer::isActive)
            .collect(Collectors.toList());
    }

    // Admin management
    public boolean addAdmin(String userId, String name, String email, String phone, String password, String adminLevel) {
        if (!validateAdminInput(userId, name, email, phone, adminLevel)) {
            return false;
        }

        if (isUserIdTaken(userId)) {
            System.out.println("Error: User ID already exists!");
            return false;
        }

        Admin admin = new Admin(userId, name, email, phone, password, adminLevel);
        admins.add(admin);
        saveAllUsers();
        System.out.println("Admin added successfully: " + admin.getName());
        return true;
    }

    public Admin findAdminById(String userId) {
        return admins.stream()
            .filter(a -> a.getUserId().equals(userId))
            .findFirst()
            .orElse(null);
    }

    // Assignment methods
    public boolean assignTrainerToMember(String trainerId, String memberId) {
        Trainer trainer = findTrainerById(trainerId);
        Member member = findMemberById(memberId);

        if (trainer == null || member == null) {
            System.out.println("Trainer or Member not found!");
            return false;
        }

        trainer.assignMember(memberId);
        member.setAssignedTrainerId(trainerId);
        saveAllUsers();
        System.out.println("Trainer assigned successfully!");
        return true;
    }

    public boolean unassignTrainerFromMember(String trainerId, String memberId) {
        Trainer trainer = findTrainerById(trainerId);
        Member member = findMemberById(memberId);

        if (trainer == null || member == null) {
            System.out.println("Trainer or Member not found!");
            return false;
        }

        trainer.unassignMember(memberId);
        member.setAssignedTrainerId(null);
        saveAllUsers();
        System.out.println("Trainer unassigned successfully!");
        return true;
    }

    // Validation methods
    private boolean validateMemberInput(String userId, String name, String email, String phone, String membershipType) {
        if (!InputValidator.isValidUserId(userId)) {
            System.out.println(InputValidator.INVALID_USER_ID);
            return false;
        }
        if (!InputValidator.isValidName(name)) {
            System.out.println(InputValidator.INVALID_NAME);
            return false;
        }
        if (!InputValidator.isValidEmail(email)) {
            System.out.println(InputValidator.INVALID_EMAIL);
            return false;
        }
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println(InputValidator.INVALID_PHONE);
            return false;
        }
        if (!InputValidator.isValidMembershipType(membershipType)) {
            System.out.println(InputValidator.INVALID_MEMBERSHIP_TYPE);
            return false;
        }
        return true;
    }

    private boolean validateTrainerInput(String userId, String name, String email, String phone, String specialization, int experience) {
        if (!InputValidator.isValidUserId(userId)) {
            System.out.println(InputValidator.INVALID_USER_ID);
            return false;
        }
        if (!InputValidator.isValidName(name)) {
            System.out.println(InputValidator.INVALID_NAME);
            return false;
        }
        if (!InputValidator.isValidEmail(email)) {
            System.out.println(InputValidator.INVALID_EMAIL);
            return false;
        }
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println(InputValidator.INVALID_PHONE);
            return false;
        }
        if (!InputValidator.isNotEmpty(specialization)) {
            System.out.println("Specialization cannot be empty");
            return false;
        }
        if (experience < 0) {
            System.out.println("Experience must be a positive number");
            return false;
        }
        return true;
    }

    private boolean validateAdminInput(String userId, String name, String email, String phone, String adminLevel) {
        if (!InputValidator.isValidUserId(userId)) {
            System.out.println(InputValidator.INVALID_USER_ID);
            return false;
        }
        if (!InputValidator.isValidName(name)) {
            System.out.println(InputValidator.INVALID_NAME);
            return false;
        }
        if (!InputValidator.isValidEmail(email)) {
            System.out.println(InputValidator.INVALID_EMAIL);
            return false;
        }
        if (!InputValidator.isValidPhone(phone)) {
            System.out.println(InputValidator.INVALID_PHONE);
            return false;
        }
        if (!InputValidator.isNotEmpty(adminLevel)) {
            System.out.println("Admin level cannot be empty");
            return false;
        }
        return true;
    }

    private boolean isUserIdTaken(String userId) {
        return members.stream().anyMatch(m -> m.getUserId().equals(userId)) ||
               trainers.stream().anyMatch(t -> t.getUserId().equals(userId)) ||
               admins.stream().anyMatch(a -> a.getUserId().equals(userId));
    }

    // Data persistence
    public void loadAllUsers() {
        members = DataPersistence.loadMembers();
        trainers = DataPersistence.loadTrainers();
        admins = DataPersistence.loadAdmins();
    }

    public void saveAllUsers() {
        DataPersistence.saveMembers(members);
        DataPersistence.saveTrainers(trainers);
        DataPersistence.saveAdmins(admins);
    }

    // Getters
    public List<Member> getMembers() { return members; }
    public List<Trainer> getTrainers() { return trainers; }
    public List<Admin> getAdmins() { return admins; }
} 