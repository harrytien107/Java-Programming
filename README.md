# 🎓 Java Programming Repository

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)
[![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)

> A comprehensive collection of Java programming assignments, labs, and full-stack projects covering core Java concepts, Spring Boot, JPA/Hibernate, and React integration.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Repository Structure](#repository-structure)
- [Learning Modules](#learning-modules)
- [Major Projects](#major-projects)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Documentation](#documentation)
- [Contributing](#contributing)

---

## 🌟 Overview

This repository contains coursework, labs, and projects from a Java programming course, demonstrating progression from basic Java concepts to advanced full-stack development with Spring Boot and React.

**Key Learning Outcomes:**
- ✅ Object-Oriented Programming (OOP) in Java
- ✅ Design Patterns (Singleton, Factory, etc.)
- ✅ Spring Boot REST API Development
- ✅ JPA/Hibernate ORM
- ✅ React Frontend Development
- ✅ Full-Stack Integration
- ✅ Docker & Deployment
- ✅ Database Relationships (One-to-Many, Many-to-Many)

---

## 📁 Repository Structure

```
📦 Java-Programming
├── 📂 Buoi2/                          # Session 2: OOP Basics
│   ├── Employee.java                   # Employee management
│   ├── EmployeeReport.java            # Reporting system
│   └── Singleton.java                 # Singleton pattern
│
├── 📂 Elearning1/                     # Lab 1: Java Fundamentals
│   └── src/                           # Source code
│
├── 📂 Elearning2/                     # Lab 2: Gym Management System
│   ├── README.md                      # Project documentation
│   ├── scr/                           # Source code
│   └── data/                          # Database files
│
├── 📂 Elearning3/                     # Lab 3: JPA Relationships
│   ├── CRUD_JPA/                      # CRUD operations with JPA
│   ├── OneToMany/                     # One-to-Many relationships
│   └── ManyToMany/                    # Many-to-Many relationships
│
├── 📂 Elearning4/                     # Lab 4: Hibernate Advanced
│   ├── E-learning4/                   # Main exercises
│   ├── Hibernate_OneToMany/           # OneToMany with Hibernate
│   └── Hibernate_ManyToMany/          # ManyToMany with Hibernate
│
├── 📂 Elearning5/                     # Lab 5: Spring Boot Projects
│   ├── demo/                          # Demo applications
│   └── demo2/                         # Additional demos
│
├── 📂 JAVA-010112213601/              # Main Project: Full-Stack App
│   ├── BackEnd/                       # Spring Boot API
│   ├── FrontEnd/                      # React Application
│   ├── Docker/                        # Docker configurations
│   ├── docker-compose.yml             # Docker orchestration
│   └── 📄 Multiple deployment guides  # Deployment documentation
│
├── 📂 JAVA-010112213601 - Local/      # Local development setup
├── 📂 JAVA-010112213601 - storage/    # Storage variant
├── 📂 Project-Finally/                # Final Project
│   ├── BackEnd/                       # Spring Boot backend
│   └── FrontEnd/                      # React frontend
│
└── 📂 LT_JAVA_010412213603-main/      # Additional coursework
    ├── LS-BackEnd/                    # Backend exercises
    └── LS-FrontEnd/                   # Frontend exercises
```

---

## 📖 Learning Modules

### 🔹 Module 1: Java Fundamentals (`Buoi2/`)
**Topics Covered:**
- Object-Oriented Programming
- Classes and Objects
- Inheritance and Polymorphism
- Design Patterns (Singleton)
- Employee Management System

**Files:**
- `Employee.java` - Employee entity with attributes and methods
- `EmployeeReport.java` - Report generation functionality
- `Singleton.java` - Implementation of Singleton design pattern

---

### 🔹 Module 2: Gym Management System (`Elearning2/`)
**Full-featured Java application with:**
- 👥 **Member Management** - Add, update, delete members
- 🏋️ **Trainer Management** - Assign trainers, track performance
- 📅 **Workout Scheduling** - Create customized workout plans
- 📊 **Attendance Tracking** - Monitor member attendance
- 💰 **Financial Reporting** - Track revenue and payments
- 🔐 **Role-Based Access Control** - Admin, Trainer, Member roles

[📄 View Detailed Documentation](./Elearning2/README.md)

---

### 🔹 Module 3: JPA & Hibernate (`Elearning3/`, `Elearning4/`)
**Database Persistence Topics:**

#### `Elearning3/CRUD_JPA/`
- Basic CRUD operations
- Entity management
- JPA repository pattern

#### `Elearning3/OneToMany/` & `Elearning4/Hibernate_OneToMany/`
- One-to-Many relationships
- Bidirectional mapping
- Cascade operations
- Orphan removal

#### `Elearning3/ManyToMany/` & `Elearning4/Hibernate_ManyToMany/`
- Many-to-Many relationships
- Join tables
- Bidirectional associations
- Complex queries

---

### 🔹 Module 4: Spring Boot (`Elearning5/`)
**Spring Framework Concepts:**
- Spring Boot application setup
- Dependency Injection
- RESTful API development
- Spring Data JPA
- Service layer architecture

---

## 🚀 Major Projects

### 🎯 Project 1: Full-Stack Application (`JAVA-010112213601/`)

The original repo is in [JAVA-010112213601](https://github.com/Glasspham/JAVA-010112213601)

A complete full-stack application with Spring Boot backend and React frontend.

**Features:**
- 🔧 **Backend**: RESTful API with Spring Boot
- 🎨 **Frontend**: Modern React UI
- 🗄️ **Database**: MySQL with JPA/Hibernate
- 🐳 **Deployment**: Docker & Docker Compose
- 🌐 **Architecture**: Microservices-ready

**Tech Stack:**
- Java 17+ / Spring Boot 3.x
- React 18+ / JavaScript/TypeScript
- MySQL 8.0
- Docker & Docker Compose
- Maven

**Available Deployment Guides:**
- [Quick Setup (5-10 minutes)](./JAVA-010112213601/QUICK_SETUP_SEPARATE_SERVERS.md)
- [Separate Servers Guide](./JAVA-010112213601/SEPARATE_SERVERS_GUIDE.md)
- [Ubuntu Deployment](./JAVA-010112213601/UBUNTU_DEPLOYMENT_GUIDE.md)
- [Windows Access Guide](./JAVA-010112213601/WINDOWS_ACCESS_GUIDE.md)
- [Docker & Network Commands](./JAVA-010112213601/DOCKER_AND_NETWORK_COMMANDS.md)

**Quick Start:**
```bash
# Clone the repository
cd JAVA-010112213601

# Start with Docker Compose
docker-compose up -d

# Or use the batch file (Windows)
./docker-start.bat
```

---

### 🎯 Project 2: Final Project (`Project-Finally/`)

Capstone project demonstrating comprehensive full-stack development skills.

**Structure:**
- `BackEnd/` - Spring Boot application
- `FrontEnd/` - React application

---

### 🎯 Project 3: Additional Coursework (`LT_JAVA_010412213603-main/`)

Additional lab exercises and coursework materials.

**Structure:**
- `LS-BackEnd/` - Backend development exercises
- `LS-FrontEnd/` - Frontend development exercises

---

## 🛠️ Technologies Used

### Backend
- **Java** 17+
- **Spring Boot** 3.x
  - Spring Web
  - Spring Data JPA
  - Spring Security (in some projects)
- **Hibernate** ORM
- **MySQL** / **H2** Database
- **Maven** - Build tool
- **Docker** - Containerization

### Frontend
- **React** 18+
- **JavaScript** / **TypeScript**
- **Axios** - HTTP client
- **React Router** - Navigation
- **Bootstrap** / **Material-UI** - UI frameworks

### DevOps
- **Docker** & **Docker Compose**
- **Git** - Version control
- **Ubuntu Server** deployment
- **Maven** wrapper

---

## 🚀 Getting Started

### Prerequisites
- Java JDK 17 or higher
- Node.js 16+ and npm
- Maven 3.6+
- MySQL 8.0+ (or use Docker)
- Docker & Docker Compose (optional)
- Git

### Installation

#### Option 1: Local Development

**Backend Setup:**
```bash
# Navigate to backend directory
cd JAVA-010112213601/BackEnd

# Install dependencies and build
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

**Frontend Setup:**
```bash
# Navigate to frontend directory
cd JAVA-010112213601/FrontEnd

# Install dependencies
npm install

# Start development server
npm start
```

#### Option 2: Docker Deployment

```bash
# Navigate to project directory
cd JAVA-010112213601

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

---

## 📖 Documentation

Each module and project contains its own documentation:

- **Elearning2**: [Gym Management System Documentation](./Elearning2/README.md)
- **JAVA-010112213601**: Multiple deployment and setup guides
  - [Quick Setup Guide](./JAVA-010112213601/QUICK_SETUP_SEPARATE_SERVERS.md)
  - [Deployment Guide](./JAVA-010112213601/README_SEPARATE_DEPLOYMENT.md)
  - [Ubuntu Deployment](./JAVA-010112213601/UBUNTU_DEPLOYMENT.md)
  - [Three Servers Architecture](./JAVA-010112213601/THREE_SERVERS_ARCHITECTURE.md)

---

## 📝 Project Highlights

### Design Patterns Implemented
- ✅ Singleton Pattern
- ✅ Factory Pattern
- ✅ Repository Pattern
- ✅ MVC Architecture
- ✅ RESTful API Design

### Database Concepts
- ✅ CRUD Operations
- ✅ One-to-Many Relationships
- ✅ Many-to-Many Relationships
- ✅ Transaction Management
- ✅ Query Optimization

### Full-Stack Integration
- ✅ REST API Development
- ✅ Frontend-Backend Communication
- ✅ State Management
- ✅ Authentication & Authorization
- ✅ Error Handling

---

## 🤝 Contributing

This is an educational repository. If you'd like to:
- Report issues
- Suggest improvements
- Share similar projects

Feel free to open an issue or submit a pull request!

---

## 📜 License

This project is part of academic coursework. Please respect academic integrity policies when referencing this code.

---

**⭐ Star this repository if you find it helpful!**

**📚 Happy Learning!** 🚀

