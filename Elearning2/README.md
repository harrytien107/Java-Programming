# Gym Management System (GMS)

A comprehensive Java-based Gym Management System designed to efficiently manage gym operations including member registration, trainer assignments, workout scheduling, attendance tracking, and financial reporting.

## 🏋️‍♂️ Features

### Core Functionalities

#### 1. **Role-Based Access Control**
- **Admin**: Full system access including member/trainer management, reports, and system configuration
- **Trainer**: Manage assigned members, create workout schedules, track progress
- **Member**: View schedules, update progress, track attendance, renew membership

#### 2. **Member Management**
- Add, update, and delete member profiles
- Track membership types (Monthly, Quarterly, Yearly)
- Monitor membership expiration dates
- Assign trainers to members
- Calculate member progress scores

#### 3. **Trainer Management**
- Manage trainer profiles with specializations
- Track trainer experience and performance
- Assign members to trainers
- Monitor trainer workload and effectiveness

#### 4. **Workout Schedule Management**
- Create customized workout schedules
- Add exercises with sets, reps, and weights
- Support for cardio and strength training
- Track workout progress and completion
- Schedule workout sessions with specific times

#### 5. **Attendance Tracking**
- Real-time check-in/check-out system
- Track daily attendance patterns
- Calculate attendance percentages
- Generate attendance reports
- Monitor peak usage hours

#### 6. **Revenue & Analytics**
- Generate comprehensive revenue reports
- Track membership revenue by type
- Calculate projected monthly income
- Export financial data to CSV
- Performance analytics and metrics

#### 7. **Data Persistence**
- CSV-based data storage
- Automatic data backup and recovery
- Export reports to CSV format
- Data integrity validation

## 🚀 Getting Started

### Prerequisites
- Java 8 or higher
- Command line interface (Terminal/PowerShell)

### Installation & Setup

1. **Clone or Download the Project**
   ```bash
   # Navigate to your project directory
   cd /path/to/your/project
   ```

2. **Compile the Java Files**
   ```bash
   # From the project root directory
   javac -d . scr/**/*.java
   ```

3. **Run the Application**
   ```bash
   java scr.Main
   ```

### Default Login Credentials

On first startup, the system creates a default admin account:
- **User ID**: `admin`
- **Email**: `admin@gym.com`
- **Password**: `admin123`

## 📱 User Interface Flow

### Login Process
1. Enter User ID and Email
2. System validates credentials and role
3. Redirects to appropriate dashboard

### Admin Dashboard
```
=== ADMIN DASHBOARD ===
1. Dashboard (Overview & Analytics)
2. Member Management
3. Trainer Management
4. Attendance Management
5. Reports & Analytics
6. Subscription Plans
7. Workout Schedules
8. Logout
```

### Trainer Dashboard
```
=== TRAINER DASHBOARD ===
1. View Assigned Members
2. Create Workout Schedule
3. View Workout Schedules
4. Update Workout Progress
5. View Member Attendance
6. Add Exercise to Schedule
7. View Member Performance
8. Logout
```

### Member Dashboard
```
=== MEMBER DASHBOARD ===
1. View My Profile
2. View My Workout Schedules
3. Update Workout Progress
4. View My Attendance
5. Renew Membership
6. View Available Subscription Plans
7. Logout
```

## 🏗️ System Architecture

### Package Structure
```
scr/
├── entity/                 # Data models
│   ├── User.java          # Base user class
│   ├── Admin.java         # Admin user class
│   ├── Trainer.java       # Trainer user class
│   ├── Member.java        # Member user class
│   ├── WorkoutSchedule.java # Workout scheduling
│   ├── Attendance.java    # Attendance tracking
│   └── SubscriptionPlan.java # Subscription plans
├── manager/               # Business logic
│   ├── UserManager.java   # User management
│   ├── WorkoutManager.java # Workout management
│   ├── AttendanceManager.java # Attendance management
│   └── ReportManager.java # Report generation
├── util/                  # Utilities
│   ├── DataPersistence.java # Data storage
│   └── InputValidator.java # Input validation
├── GymManagementSystem.java # Main application
└── Main.java             # Entry point
```

### Key Classes and Methods

#### UserManager
- `login(userId, email)` - Authenticate users
- `addMember()`, `updateMember()`, `deleteMember()` - Member CRUD operations
- `addTrainer()`, `updateTrainer()`, `deleteTrainer()` - Trainer CRUD operations
- `assignTrainerToMember()` - Trainer assignment

#### WorkoutManager
- `createWorkoutSchedule()` - Create new workout plans
- `addExerciseToSchedule()` - Add exercises to workouts
- `updateWorkoutProgress()` - Track workout completion
- `getTopPerformingMembers()` - Performance analytics

#### AttendanceManager
- `checkInMember()`, `checkOutMember()` - Attendance tracking
- `getMemberAttendanceStats()` - Calculate attendance metrics
- `exportAttendanceReport()` - Generate CSV reports

#### ReportManager
- `generateRevenueReport()` - Financial analytics
- `generateMembershipReport()` - Membership statistics
- `generatePerformanceReport()` - Member performance data
- `displayDashboard()` - System overview

## 📊 Data Models

### User Hierarchy
```java
User (Abstract)
├── Admin
├── Trainer
└── Member
```

### Key Entities
- **Member**: Membership details, progress tracking, attendance
- **Trainer**: Specialization, experience, assigned members
- **WorkoutSchedule**: Exercise plans, progress tracking
- **Attendance**: Check-in/out times, duration, status
- **SubscriptionPlan**: Pricing, benefits, duration

## 📈 Features in Detail

### Membership Management
- Support for multiple membership types
- Automatic expiration tracking
- Renewal functionality
- Progress score calculation (0-100%)

### Workout Scheduling
- Flexible exercise types (Cardio, Strength, Mixed)
- Detailed exercise specifications (sets, reps, weight, duration)
- Progress tracking with completion percentages
- Notes and comments system

### Attendance System
- Real-time check-in/out tracking
- Automatic duration calculation
- Late arrival detection
- Comprehensive reporting

### Revenue Tracking
- Multiple membership pricing tiers
- Projected revenue calculations
- Revenue breakdown by membership type
- Export capabilities for accounting

### Analytics & Reporting
- Member performance rankings
- Trainer effectiveness metrics
- Attendance pattern analysis
- Peak hour identification
- Revenue trend analysis

## 🔧 Configuration

### Membership Types & Pricing
Default pricing structure:
- **Monthly**: $50/month
- **Quarterly**: $135/3 months (10% discount)
- **Yearly**: $480/12 months (20% discount)

### Subscription Plans
- **Basic**: 3 workouts/week, gym access
- **Standard**: 5 workouts/week, classes, nutrition consultation
- **Premium**: Unlimited access, personal trainer, nutrition plan
- **Unlimited**: 24/7 access, dedicated trainer, VIP services

## 📁 Data Storage

The system uses CSV files for data persistence:
- `data/members.csv` - Member information
- `data/trainers.csv` - Trainer profiles
- `data/admins.csv` - Admin accounts
- `data/attendance.csv` - Attendance records
- `data/subscription_plans.csv` - Available plans

## 🚨 Error Handling

### Input Validation
- Email format validation
- Phone number validation (7-15 digits)
- Name validation (letters and spaces only)
- Date/time format validation
- Percentage validation (0-100%)

### Security Features
- Input sanitization
- Role-based access control
- Session management
- Data integrity checks

## 📝 Sample Usage Scenarios

### Scenario 1: Adding a New Member (Admin)
1. Login as admin
2. Navigate to Member Management
3. Select "Add Member"
4. Enter member details
5. System validates input and creates account
6. Member can now login and access system

### Scenario 2: Creating a Workout Plan (Trainer)
1. Login as trainer
2. Select "Create Workout Schedule"
3. Assign to specific member
4. Add exercises with specifications
5. Schedule workout time
6. Member receives workout plan

### Scenario 3: Tracking Progress (Member)
1. Login as member
2. View workout schedules
3. Select active workout
4. Update completion percentage
5. Add progress notes
6. System updates overall progress score

## 🎯 Advanced Features

### Progress Tracking Algorithm
The system calculates member progress scores using:
- Workout completion rate (80% weight)
- Attendance percentage (20% weight)
- Maximum score: 100%

### Attendance Analytics
- Peak hour identification
- Member retention analysis
- Attendance pattern recognition
- Predictive attendance modeling

### Revenue Optimization
- Membership type recommendations
- Revenue forecasting
- Pricing strategy analytics
- Member lifetime value calculation

## 🔮 Future Enhancements

### Potential Additions
- Mobile application interface
- Payment processing integration
- Equipment booking system
- Nutrition tracking
- Social features and challenges
- Integration with fitness wearables
- Advanced reporting dashboard
- Multi-location support

### Technical Improvements
- Database integration (MySQL/PostgreSQL)
- REST API development
- Web-based interface
- Cloud deployment options
- Real-time notifications
- Backup and disaster recovery

## 🤝 Contributing

### Development Guidelines
1. Follow Java naming conventions
2. Implement comprehensive error handling
3. Add input validation for all user inputs
4. Include JavaDoc comments
5. Write unit tests for new features
6. Maintain backwards compatibility

### Code Quality Standards
- Use meaningful variable names
- Keep methods focused and concise
- Implement proper exception handling
- Follow SOLID design principles
- Maintain consistent code formatting

## 📞 Support

For technical support or feature requests:
- Review the documentation thoroughly
- Check existing issues and solutions
- Test with sample data first
- Provide detailed error descriptions

## 📄 License

This project is developed for educational purposes as part of a Java programming assignment.

---

**Developed with ❤️ for efficient gym management** 