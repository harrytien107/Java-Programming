# Hibernate Many-to-Many Demo Project

This is a demonstration project showing how to implement a **Many-to-Many** relationship using **Hibernate** and **JPA** with **MySQL** database.

## Project Structure

```
├── src/main/java/com/fe/
│   ├── pojo/               # Entity classes
│   │   ├── Student.java    # Student entity
│   │   └── Book.java       # Book entity  
│   ├── dao/                # Data Access Layer
│   │   └── StudentDAO.java # Student DAO implementation
│   ├── repository/         # Repository Layer
│   │   ├── IStudentRepository.java
│   │   └── StudentRepository.java
│   ├── service/           # Service Layer
│   │   ├── IStudentService.java
│   │   └── StudentService.java
│   └── main/              # Main application
│       └── StudentMain.java
├── src/main/resources/
│   ├── hibernate.cfg.xml   # Hibernate configuration
│   └── META-INF/
│       └── persistence.xml # JPA configuration
├── database-setup.sql      # Database setup script
└── pom.xml                # Maven dependencies
```

## Many-to-Many Relationship

- **Student** ↔ **Book**: A student can enroll in multiple books, and a book can have multiple students
- Join table: `STUDENT_BOOKS` with foreign keys `student_id` and `book_id`

## Prerequisites

1. **Java 17** or higher
2. **Maven 3.6+**
3. **MySQL 8.0** (running in Docker as specified)
4. **Docker** (for MySQL container)

## Database Setup

### 1. Start MySQL Docker Container

```bash
# Pull and run MySQL 8.0.42 container
docker run --name mysql-8.0.42 -e MYSQL_ROOT_PASSWORD=1066 -p 3306:3306 -d mysql:8.0.42

# Verify container is running
docker ps
```

### 2. Create Database

```bash
# Connect to MySQL container
docker exec -it mysql-8.0.42 mysql -u root -p1066

# Run the database setup script
SOURCE /path/to/database-setup.sql;
# OR manually:
CREATE DATABASE IF NOT EXISTS manytomany_hibernate_db;
USE manytomany_hibernate_db;
```

Alternatively, copy the `database-setup.sql` file to the container and run it:

```bash
docker cp database-setup.sql mysql-8.0.42:/tmp/
docker exec -it mysql-8.0.42 mysql -u root -p1066 < /tmp/database-setup.sql
```

## Project Configuration

The project is configured to connect to:

- **Host**: localhost:3306
- **Database**: manytomany_hibernate_db  
- **Username**: root
- **Password**: 1066

Configuration files:
- `src/main/resources/hibernate.cfg.xml`
- `src/main/resources/META-INF/persistence.xml`

## Running the Application

### 1. Compile and Package

```bash
mvn clean compile
```

### 2. Run the Application

```bash
mvn exec:java
```

Or run directly:

```bash
java -cp target/classes:target/dependency/* com.fe.main.StudentMain
```

## Application Features

The console application provides the following menu options:

1. **Add Student** - Create a new student
2. **Delete Student** - Remove a student by ID
3. **Update Student** - Modify student details
4. **Get a Student** - Find student by ID (shows enrolled books)
5. **Get All Students** - List all students
6. **Demo Many-to-Many Relationship** - Creates sample data demonstrating the relationship
7. **Exit** - Close the application

### Demo Feature (Option 6)

The demo creates:
- 2 Students: John Doe, Jane Smith
- 3 Books: Java Programming, Spring Framework, Hibernate Guide
- Relationships:
  - John enrolls in Java Programming and Spring Framework
  - Jane enrolls in Java Programming and Hibernate Guide
  - Shows that one book (Java Programming) has multiple students

## Database Tables Created

Hibernate automatically creates these tables:

```sql
STUDENTS (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    marks INT
);

BOOKS (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(30),
    author VARCHAR(255),
    isbn VARCHAR(255)
);

STUDENT_BOOKS (
    student_id BIGINT,
    book_id BIGINT,
    PRIMARY KEY (student_id, book_id),
    FOREIGN KEY (student_id) REFERENCES STUDENTS(id),
    FOREIGN KEY (book_id) REFERENCES BOOKS(id)
);
```

## Troubleshooting

### Common Issues

1. **Connection refused**: Ensure MySQL container is running
2. **Database not found**: Run the database setup script
3. **Authentication failed**: Check password (1066) and username (root)
4. **Port conflicts**: Ensure port 3306 is available

### Verify Database Connection

```bash
# Test connection to MySQL
docker exec -it mysql-8.0.42 mysql -u root -p1066 -e "SHOW DATABASES;"
```

### Check Application Logs

The application shows SQL queries in the console due to:
```xml
<property name="hibernate.show_sql">true</property>
<property name="hibernate.format_sql">true</property>
```

## Key Hibernate Annotations Used

- `@Entity` - Marks classes as JPA entities
- `@Table` - Specifies database table name
- `@Id` - Primary key field
- `@GeneratedValue` - Auto-generated primary keys
- `@Column` - Column mapping and constraints
- `@ManyToMany` - Many-to-many relationship
- `@JoinTable` - Configures join table
- `@JoinColumn` - Foreign key columns

## Architecture Layers

1. **Entity Layer** (`pojo`): JPA entities with relationships
2. **DAO Layer** (`dao`): Direct Hibernate session management
3. **Repository Layer** (`repository`): Abstraction over DAO
4. **Service Layer** (`service`): Business logic
5. **Presentation Layer** (`main`): Console interface

This demonstrates a clean separation of concerns in a Hibernate application. 