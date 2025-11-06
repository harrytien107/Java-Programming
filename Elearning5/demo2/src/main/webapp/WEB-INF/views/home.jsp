<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Management System</title>
    
    <!-- Bootstrap 5 CSS - Local -->
    <link href="<c:url value='/resources/css/bootstrap.min.css'/>" rel="stylesheet">
    <!-- Font Awesome - Local -->
    <link href="<c:url value='/resources/css/font-awesome.min.css'/>" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="<c:url value='/resources/css/style.css'/>" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <div class="main-container">
            <!-- Header Section -->
            <div class="header-section">
                <h1><i class="fas fa-graduation-cap"></i> Student Management System</h1>
            </div>
            
            <!-- Students Table -->
            <div class="table-container" id="students">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead>
                            <tr>
                                <th><i class="fas fa-check-circle"></i> Select</th>
                                <th><i class="fas fa-id-badge"></i> Student ID</th>
                                <th><i class="fas fa-user"></i> First Name</th>
                                <th><i class="fas fa-user"></i> Last Name</th>
                                <th><i class="fas fa-chart-bar"></i> Marks (0-10)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty studentList}">
                                    <tr>
                                        <td colspan="5" class="text-center py-5">
                                            <i class="fas fa-users fa-3x text-muted mb-3"></i>
                                            <h5 class="text-muted">No students found</h5>
                                            <p class="text-muted">Add your first student to get started!</p>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="student" items="${studentList}">
                                        <tr class="student-row" data-student-id="${student.id}">
                                            <td>
                                                <input type="radio" name="selectedStudent" value="${student.id}" 
                                                       class="form-check-input"
                                                       onclick="selectStudent('${student.id}', '${student.firstName}', '${student.lastName}', '${student.marks}')">
                                            </td>
                                            <td><strong>#${student.id}</strong></td>
                                            <td>${student.firstName}</td>
                                            <td>${student.lastName}</td>
                                            <td>
                                                <span class="badge 
                                                    <c:choose>
                                                        <c:when test="${student.marks >= 9}">bg-success</c:when>
                                                        <c:when test="${student.marks >= 7}">bg-info</c:when>
                                                        <c:when test="${student.marks >= 5}">bg-warning</c:when>
                                                        <c:otherwise>bg-danger</c:otherwise>
                                                    </c:choose>">
                                                    ${student.marks}
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
            
            <!-- Divider -->
            <div class="row my-1">
                <div class="col-12">
                    <hr class="custom-divider">
                </div>
            </div>
            
            <!-- Student Management Form -->
            <div class="form-container mt-5 pt-4">
                <div class="row">
                    <div class="col-12">
                        <div class="card shadow-sm border-0">
                            <div class="card-header bg-white">
                                <div class="d-flex align-items-center">
                                    <i class="fas fa-user-edit fa-2x me-3" style="background: linear-gradient(135deg, #1976d2, #7b1fa2); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;"></i>
                                    <h4 class="mb-0">Student Management</h4>
                                </div>
                            </div>
                            <div class="card-body p-4">
                
                <form method="post" action="/manageStudent" id="studentForm">
                    <input type="hidden" id="originalID" name="originalID" value="">
                    
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label for="txtID" class="form-label">
                                <i class="fas fa-id-badge"></i> Student ID
                            </label>
                            <input type="number" class="form-control" id="txtID" name="txtID" 
                                   required min="1" placeholder="Enter Student ID">
                        </div>
                        <div class="col-md-3">
                            <label for="txtFirstName" class="form-label">
                                <i class="fas fa-user"></i> First Name
                            </label>
                            <input type="text" class="form-control" id="txtFirstName" name="txtFirstName" 
                                   required placeholder="Enter First Name">
                        </div>
                        <div class="col-md-3">
                            <label for="txtLastName" class="form-label">
                                <i class="fas fa-user"></i> Last Name
                            </label>
                            <input type="text" class="form-control" id="txtLastName" name="txtLastName" 
                                   required placeholder="Enter Last Name">
                        </div>
                        <div class="col-md-3">
                            <label for="txtMarks" class="form-label">
                                <i class="fas fa-chart-bar"></i> Marks (0-10)
                            </label>
                            <input type="number" class="form-control" id="txtMarks" name="txtMarks" 
                                   min="0" max="10" step="0.1" required placeholder="Enter Marks">
                        </div>
                    </div>
                    
                    <div class="d-flex flex-wrap gap-3 mt-4">
                        <button type="button" class="btn btn-primary btn-enhanced" onclick="submitForm('Add')">
                            <i class="fas fa-plus"></i> Add Student
                        </button>
                        <button type="button" class="btn btn-warning btn-enhanced" onclick="submitForm('Update')">
                            <i class="fas fa-edit"></i> Update Student
                        </button>
                        <button type="button" class="btn btn-danger btn-enhanced" onclick="submitForm('Delete')">
                            <i class="fas fa-trash"></i> Delete Student
                        </button>
                        <button type="button" class="btn btn-secondary btn-enhanced" onclick="clearForm()">
                            <i class="fas fa-refresh"></i> Clear Form
                        </button>
                    </div>
                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS - Local -->
    <script src="<c:url value='/resources/js/bootstrap.bundle.min.js'/>"></script>
    <!-- Custom JS -->
    <script src="<c:url value='/resources/js/app.js'/>"></script>
    
    <script>
        function selectStudent(id, firstName, lastName, marks) {
            // Clear previous selections
            document.querySelectorAll('.student-row').forEach(row => {
                row.classList.remove('table-active');
            });
            
            // Highlight selected row
            const selectedRow = document.querySelector(`[data-student-id="${id}"]`);
            if (selectedRow) {
                selectedRow.classList.add('table-active');
            }
            
            // Fill form
            document.getElementById('txtID').value = id;
            document.getElementById('originalID').value = id;
            document.getElementById('txtFirstName').value = firstName;
            document.getElementById('txtLastName').value = lastName;
            document.getElementById('txtMarks').value = marks;
        }
        
        function submitForm(action) {
            console.log('submitForm called with action:', action);
            
            // Create hidden input for action
            let actionInput = document.getElementById('actionInput');
            if (!actionInput) {
                actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'btnManageStudent';
                actionInput.id = 'actionInput';
                document.getElementById('studentForm').appendChild(actionInput);
            }
            actionInput.value = action;
            
            // Submit form
            document.getElementById('studentForm').submit();
        }
        
        function clearForm() {
            document.getElementById('txtID').value = '';
            document.getElementById('originalID').value = '';
            document.getElementById('txtFirstName').value = '';
            document.getElementById('txtLastName').value = '';
            document.getElementById('txtMarks').value = '';
            
            // Clear table selection
            document.querySelectorAll('.student-row').forEach(row => {
                row.classList.remove('table-active');
            });
            
            document.querySelectorAll('input[name="selectedStudent"]').forEach(radio => {
                radio.checked = false;
            });
        }
        
        // Smooth scrolling for navigation
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                const target = document.querySelector(this.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
        
        // Debug form submission
        document.getElementById('studentForm').addEventListener('submit', function(e) {
            console.log('Form submitted!');
            console.log('Form action:', this.action);
            console.log('Form method:', this.method);
            
            // Log form data
            const formData = new FormData(this);
            console.log('Form data:');
            for (let [key, value] of formData.entries()) {
                console.log('  ' + key + ':', value);
            }
        });
        
    </script>
</body>
</html>
