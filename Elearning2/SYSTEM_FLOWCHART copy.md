# Gym Management System - User Interaction Flowchart

## System Overview Flowchart

```
┌─────────────────────────────────────────────────────────────┐
│                    GYM MANAGEMENT SYSTEM                   │
│                        START                                │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────────────────────┐
│              INITIALIZE SYSTEM                              │
│  • Load existing data from CSV files                       │
│  • Create data directory if not exists                     │
│  • Initialize managers (User, Workout, Attendance, Report) │
│  • Create default admin if none exists                     │
└─────────────────┬───────────────────────────────────────────┘
                  │
                  ▼
┌───────────────────────────────────────────────────────────────────────────┐
│                                  LOGIN SCREEN                             │
│                                                                           │
│  ┌─────────────────┐    ┌─────────────────────────┐    ┌─────────────────┐│
│  │    1. Login     │    │    2. Register as Member│    │    3. Exit      ││
│  └─────────────────┘    └─────────────────────────┘    └─────────────────┘│
└─────────────────┬───────────────────────────┬─────────────────────────────┘
                  │                           │
                  ▼                           ▼
┌─────────────────────────────────────┐   ┌─────────────────┐
│         LOGIN PROCESS               │   │   EXIT SYSTEM   │
│  • Enter User ID                    │   │  • Save data    │
│  • Enter Email                      │   │  • Goodbye msg  │
│  • Validate credentials             │   │  • System.exit  │
│  • Check user role                  │   └─────────────────┘
│  • Check if account is active       │
└─────────────────┬───────────────────┘
                  │
         ┌────────┴────────┐
         │                 │
    ✅ SUCCESS        ❌ FAILURE
         │                 │
         ▼                 ▼
┌─────────────────┐  ┌─────────────────┐
│   SET CURRENT   │  │  DISPLAY ERROR  │
│      USER       │  │   MESSAGE AND   │
│                 │  │  RETURN TO LOGIN│
└─────────┬───────┘  └─────────────────┘
          │
          ▼
┌───────────────────────────────────────────────────────────┐
│                ROLE-BASED MENU ROUTING                    │
└─────────────┬─────────────┬─────────────┬─────────────────┘
              │             │             │
        ┌─────▼──────┐ ┌────▼────┐ ┌──────▼──────┐
        │   ADMIN    │ │ TRAINER │ │   MEMBER    │
        │ DASHBOARD  │ │DASHBOARD│ │  DASHBOARD  │
        └────────────┘ └─────────┘ └─────────────┘
```

## Admin Dashboard Flowchart

```
┌─────────────────────────────────────────────────────────────┐
│                    ADMIN DASHBOARD                          │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │1.Dashboard  │ │2.Members    │ │3.Trainers   │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │4.Attendance │ │5.Reports    │ │6.Sub Plans  │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐                            │
│  │7.Workouts   │ │8.Logout     │                            │
│  └─────────────┘ └─────────────┘                            │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
│   MEMBER MGMT       │ │ TRAINER MGMT │ │ ATTENDANCE MGMT │
│                     │ │              │ │                 │
│ • Add Member        │ │ • Add        │ │ • Check In      │
│ • View All Members  │ │ • View All   │ │ • Check Out     │
│ • Update Member     │ │ • Update     │ │ • View Today    │
│ • Delete Member     │ │ • Delete     │ │ • History       │
│ • Assign Trainer    │ │ • Performance│ │ • Reports       │
│ • Member Details    │ │              │ │ • Export        │
│ • Expired Members   │ │              │ │                 │
└─────────────────────┘ └──────────────┘ └─────────────────┘

              │               │               │
              ▼               ▼               ▼
┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
│  REPORTS & ANALYTICS│ │ SUBSCRIPTION │ │ WORKOUT MGMT    │
│                     │ │   PLANS      │ │                 │
│ • Revenue Report    │ │              │ │ • Create        │
│ • Membership Report │ │ • View Plans │ │ • View All      │
│ • Performance Report│ │ • Add New    │ │ • Update        │
│ • Trainer Report    │ │   Plan       │ │ • Details       │
│ • Export Revenue    │ │              │ │ • Cancel        │
│ • Export Membership │ │              │ │                 │
└─────────────────────┘ └──────────────┘ └─────────────────┘
```

## Trainer Dashboard Flowchart

```
┌─────────────────────────────────────────────────────────────┐
│                   TRAINER DASHBOARD                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │1.Assigned   │ │2.Create     │ │3.View       │            │
│  │  Members    │ │  Workout    │ │  Schedules  │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │4.Update     │ │5.View Member│ │6.Add        │            │
│  │  Progress   │ │  Attendance │ │  Exercise   │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐                            │
│  │7.View Perf. │ │8.Logout     │                            │
│  └─────────────┘ └─────────────┘                            │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
│   MEMBER MANAGEMENT │ │ WORKOUT MGMT │ │ PROGRESS TRACK  │
│                     │ │              │ │                 │
│ • View assigned     │ │ • Create new │ │ • Update %      │
│   members list      │ │   schedule   │ │ • Add notes     │
│ • Check member      │ │ • Add exer-  │ │ • Mark complete │
│   details           │ │   cises      │ │ • View history  │
│ • Monitor progress  │ │ • Schedule   │ │                 │
│ • Performance stats │ │   times      │ │                 │
└─────────────────────┘ └──────────────┘ └─────────────────┘

                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
│   ACCESS CONTROL    │ │  VALIDATION  │ │   REPORTING     │
│                     │ │              │ │                 │
│ • Can only modify   │ │ • Schedule   │ │ • Member perf.  │
│   assigned members  │ │   ownership  │ │ • Attendance    │
│ • View only own     │ │ • Input val. │ │ • Progress sum. │
│   schedules         │ │ • Data integ.│ │                 │
└─────────────────────┘ └──────────────┘ └─────────────────┘
```

## Member Dashboard Flowchart

```
┌─────────────────────────────────────────────────────────────┐
│                    MEMBER DASHBOARD                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │1.My Profile │ │2.My Workout │ │3.Update     │            │
│  │             │ │  Schedules  │ │  Progress   │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐            │
│  │4.My         │ │5.Renew      │ │6.View Sub   │            │
│  │  Attendance │ │  Membership │ │  Plans      │            │
│  └─────────────┘ └─────────────┘ └─────────────┘            │
│  ┌─────────────┐                                            │
│  │7.Logout     │                                            │
│  └─────────────┘                                            │
└─────────────────────────────────────────────────────────────┘
                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
│   PROFILE VIEW      │ │ WORKOUT VIEW │ │ PROGRESS UPDATE │
│                     │ │              │ │                 │
│ • Personal info     │ │ • Scheduled  │ │ • Select workout│
│ • Membership status │ │   workouts   │ │ • Enter % done  │
│ • Progress score    │ │ • Exercise   │ │ • Add notes     │
│ • Attendance %      │ │   details    │ │ • Submit update │
│ • Assigned trainer  │ │ • Progress   │ │                 │
│ • Expiry date       │ │   tracking   │ │                 │
└─────────────────────┘ └──────────────┘ └─────────────────┘

                              │
              ┌───────────────┼───────────────┐
              │               │               │
              ▼               ▼               ▼
┌─────────────────────┐ ┌──────────────┐ ┌─────────────────┐
│  ATTENDANCE VIEW    │ │   RENEWAL    │ │  PLAN VIEWING   │
│                     │ │              │ │                 │
│ • Last 30 days      │ │ • Check exp. │ │ • Available     │
│ • Attendance %      │ │   date       │ │   plans         │
│ • Duration stats    │ │ • Select new │ │ • Pricing       │
│ • Recent visits     │ │   type       │ │ • Benefits      │
│                     │ │ • Update     │ │ • Features      │
└─────────────────────┘ └──────────────┘ └─────────────────┘
```

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      DATA FLOW                              │
└─────────────────────────────────────────────────────────────┘

USER INPUT
    │
    ▼
┌─────────────────┐
│ INPUT VALIDATOR │ ── Validates format, sanitizes input
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   MANAGERS      │ ── Business logic processing
│                 │
│ • UserManager   │ ── User operations (CRUD)
│ • WorkoutMgr    │ ── Workout scheduling
│ • AttendanceMgr │ ── Check-in/out operations
│ • ReportMgr     │ ── Analytics & reporting
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   ENTITIES      │ ── Data models
│                 │
│ • Member        │ ── Member data & methods
│ • Trainer       │ ── Trainer data & methods
│ • Admin         │ ── Admin data & methods
│ • WorkoutSched. │ ── Workout data & methods
│ • Attendance    │ ── Attendance data & methods
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│ DATA PERSIST.   │ ── CSV file operations
│                 │
│ • Save data     │ ── Write to CSV files
│ • Load data     │ ── Read from CSV files
│ • Export reports│ ── Generate CSV reports
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   CSV FILES     │ ── Persistent storage
│                 │
│ • members.csv   │
│ • trainers.csv  │
│ • admins.csv    │
│ • attendance.csv│
│ • sub_plans.csv │
└─────────────────┘
```

## User Authentication Flow

```
┌─────────────────────────────────────────────────────────────┐
│                 AUTHENTICATION FLOW                         │
└─────────────────────────────────────────────────────────────┘

START LOGIN
    │
    ▼
┌─────────────────┐
│ Collect User ID │
│   and Email     │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│ Input Validation│ ── Check format, sanitize
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│ Search in Users │ ── Check all user types
│ • Admin list    │    (Admin, Trainer, Member)
│ • Trainer list  │
│ • Member list   │
└─────────┬───────┘
          │
    ┌─────┴─────┐
    │           │
FOUND       NOT FOUND
    │           │
    ▼           ▼
┌─────────┐ ┌─────────┐
│ Check   │ │ Login   │
│ Active  │ │ Failed  │
│ Status  │ └─────────┘
└─────┬───┘
      │
 ┌────┴────┐
 │         │
ACTIVE  INACTIVE
 │         │
 ▼         ▼
┌─────┐ ┌─────────┐
│Set  │ │ Login   │
│User │ │ Failed  │
│&    │ │(Inactive│
│Role │ │Account) │
└─────┘ └─────────┘
 │
 ▼
ROUTE TO
DASHBOARD
```

## Attendance Tracking Flow

```
┌─────────────────────────────────────────────────────────────┐
│                 ATTENDANCE TRACKING FLOW                    │
└─────────────────────────────────────────────────────────────┘

MEMBER ARRIVES
    │
    ▼
┌─────────────────┐
│   CHECK-IN      │
│ Enter Member ID │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   VALIDATION    │
│ • Member exists │
│ • Active member │
│ • Not expired   │
│ • Not already   │
│   checked in    │
└─────────┬───────┘
          │
     ┌────┴────┐
     │         │
  VALID    INVALID
     │         │
     ▼         ▼
┌─────────┐ ┌─────────┐
│ Create  │ │ Display │
│Attend.  │ │ Error   │
│Record   │ │ Message │
│         │ └─────────┘
│• Gen ID │
│• Set    │
│  time   │
│• Status │
└─────┬───┘
      │
      ▼
┌─────────────────┐
│ MEMBER WORKS    │
│     OUT         │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   CHECK-OUT     │
│ Enter Member ID │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   VALIDATION    │
│ • Find today's  │
│   record        │
│ • Not already   │
│   checked out   │
└─────────┬───────┘
          │
     ┌────┴────┐
     │         │
  VALID    INVALID
     │         │
     ▼         ▼
┌─────────┐ ┌─────────┐
│ Update  │ │ Display │
│ Record  │ │ Error   │
│         │ │ Message │
│• Set    │ └─────────┘
│  time   │
│• Calc   │
│  dur.   │
│• Status │
└─────┬───┘
      │
      ▼
┌─────────────────┐
│  SAVE RECORD &  │
│ UPDATE MEMBER   │
│   STATISTICS    │
└─────────────────┘
```

## Error Handling Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    ERROR HANDLING FLOW                      │
└─────────────────────────────────────────────────────────────┘

USER INPUT
    │
    ▼
┌─────────────────┐
│ INPUT RECEIVED  │
└─────────┬───────┘
          │
          ▼
┌─────────────────┐
│   VALIDATION    │
│                 │
│ • Format check  │
│ • Range check   │
│ • Type check    │
│ • Business rule │
│   validation    │
└─────────┬───────┘
          │
    ┌─────┴─────┐
    │           │
  VALID      INVALID
    │           │
    ▼           ▼
┌─────────┐ ┌─────────────────┐
│PROCESS  │ │ ERROR HANDLING  │
│INPUT    │ │                 │
└─────────┘ │ • Identify type │
            │ • Format message│
            │ • Log if needed │
            │ • User feedback │
            └─────────┬───────┘
                      │
                      ▼
            ┌─────────────────┐
            │ RECOVERY ACTION │
            │                 │
            │ • Clear input   │
            │ • Show help     │
            │ • Retry prompt  │
            │ • Return to menu│
            └─────────────────┘
```

This comprehensive flowchart documentation shows:

1. **System Initialization** - How the application starts and loads data
2. **User Authentication** - Login process and role-based routing
3. **Role-Specific Dashboards** - Different interfaces for Admin, Trainer, and Member
4. **Data Flow** - How information moves through the system layers
5. **Attendance Tracking** - Check-in/out process with validation
6. **Error Handling** - How the system manages and recovers from errors

The flowchart provides a visual representation of user interactions and system processes, making it easy to understand the application flow and functionality. 