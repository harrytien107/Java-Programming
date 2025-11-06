# Student Management System

A comprehensive web application for managing students and books using Spring MVC, Spring Data JPA, and modern web technologies.

## 🚀 Features

- **Student Management**: Add, edit, delete, and view student information
- **Book Management**: Manage library books with title, author, and ISBN
- **Admin Dashboard**: View detailed statistics and performance analytics
- **Modern UI**: Responsive design with Bootstrap and custom CSS
- **Data Persistence**: JPA/Hibernate with H2 database
- **RESTful APIs**: Clean REST endpoints for all operations

## 📋 Requirements

- Java 17 or higher
- Maven 3.6+
- Modern web browser

## 🛠️ Technologies Used

### Backend
- Spring Boot 3.5.0
- Spring MVC
- Spring Data JPA
- Spring ORM
- Spring JDBC
- Hibernate Core
- H2 Database

### Frontend
- JSP (JavaServer Pages)
- Bootstrap 5.1.3
- Font Awesome 6.0.0
- Chart.js (for analytics)
- Custom CSS with modern gradients and animations

### Build Tools
- Maven
- Spring Boot Maven Plugin

## 📦 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd demo
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

Or run the main class:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.war
```

### 4. Access the Application
- **Main Application**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (empty)

## 🏗️ Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── _079205025447/SuDucTien/KM2301B/
│   │   │       ├── controller/          # Spring MVC Controllers
│   │   │       │   ├── HomeController.java
│   │   │       │   └── BookController.java
│   │   │       ├── service/            # Business Logic Layer
│   │   │       │   ├── IStudentService.java
│   │   │       │   ├── StudentService.java
│   │   │       │   ├── IBookService.java
│   │   │       │   └── BookService.java
│   │   │       ├── repository/         # Data Access Layer
│   │   │       │   ├── IStudentRepository.java
│   │   │       │   ├── StudentRepository.java
│   │   │       │   ├── IBookRepository.java
│   │   │       │   └── BookRepository.java
│   │   │       ├── dao/               # Data Access Objects
│   │   │       │   ├── StudentDAO.java
│   │   │       │   └── BookDAO.java
│   │   │       ├── pojo/              # Entity Classes
│   │   │       │   ├── Student.java
│   │   │       │   └── Book.java
│   │   │       └── DemoApplication.java
│   │   ├── resources/
│   │   │   ├── META-INF/
│   │   │   │   └── persistence.xml    # JPA Configuration
│   │   │   └── application.properties # Spring Boot Configuration
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── views/             # JSP Views
│   │       │   │   ├── home.jsp
│   │       │   │   ├── admin.jsp
│   │       │   │   └── books.jsp
│   │       │   └── web.xml            # Web Configuration
│   │       ├── resources/
│   │       │   └── css/
│   │       │       └── style.css      # Custom Styles
│   │       └── index.jsp              # Welcome Page
│   └── test/
└── pom.xml                           # Maven Configuration
```

## 🎯 Usage

### Student Management
1. Navigate to the home page
2. View the list of students in the table
3. Select a student by clicking on a row or radio button
4. Use the form below to Add, Update, or Delete students
5. Fill in First Name, Last Name, and Marks (0-100)

### Book Management
1. Click on "Manage Books" in the navigation
2. View the list of books
3. Add new books with Title, Author, and ISBN
4. Edit or delete existing books

### Admin Dashboard
1. Click on "Admin Dashboard" to view statistics
2. See total students, average scores, highest and lowest scores
3. View interactive charts showing grade distribution
4. Review detailed student performance table

## 🔧 Configuration

### Database Configuration
The application uses H2 in-memory database by default. To use a different database:

1. Update `application.properties`:
```properties
# For MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/student_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

2. Update `persistence.xml` with corresponding database settings.

3. Add the appropriate database driver dependency to `pom.xml`.

### Application Properties
Key configuration options in `application.properties`:
- `server.port`: Change application port (default: 8080)
- `spring.jpa.hibernate.ddl-auto`: Database schema generation strategy
- `spring.jpa.show-sql`: Enable/disable SQL logging

## 🎨 UI Features

- **Modern Design**: Gradient backgrounds and smooth animations
- **Responsive Layout**: Works on desktop, tablet, and mobile devices
- **Interactive Tables**: Click-to-select rows with hover effects
- **Real-time Statistics**: Live updating charts and metrics
- **Form Validation**: Client-side and server-side validation
- **Loading Indicators**: Smooth transitions and feedback

## 📊 API Endpoints

### Student Management
- `GET /` - Home page with student list
- `POST /manageStudent` - Add/Update/Delete student
- `GET /admin` - Admin dashboard with statistics

### Book Management
- `GET /books` - Book management page
- `POST /books/manage` - Add/Update/Delete book

## 🧪 Testing

### Manual Testing
1. Start the application
2. Test student CRUD operations
3. Test book CRUD operations
4. Verify statistics accuracy
5. Test responsive design on different screen sizes

### Database Testing
1. Access H2 console at http://localhost:8080/h2-console
2. Verify table structure and data
3. Run custom queries to validate business logic

## 🚀 Deployment

### Local Deployment
```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.war
```

### Docker Deployment (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/demo-0.0.1-SNAPSHOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.war"]
```

## 🛡️ Security Considerations

- Input validation on all forms
- SQL injection prevention through JPA
- XSS protection in JSP templates
- CSRF protection (can be enabled)

## 🔄 Future Enhancements

- [ ] User authentication and authorization
- [ ] File upload for student photos
- [ ] Email notifications
- [ ] Export functionality (PDF, Excel)
- [ ] Advanced search and filtering
- [ ] REST API documentation with Swagger
- [ ] Unit and integration tests
- [ ] Caching with Redis
- [ ] Microservices architecture

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Su Duc Tien** - Initial development

## 📞 Support

For support and questions:
- Create an issue in the repository
- Contact the development team

---

**Built with ❤️ using Spring Boot and modern web technologies** 