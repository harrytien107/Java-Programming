### Key Classes and Methods

#### UserManager
- `login(userId, email)` - Xác thực users
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
