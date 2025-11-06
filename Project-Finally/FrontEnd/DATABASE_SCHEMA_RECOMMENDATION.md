# Đề xuất cấu trúc Database cho Hệ thống Phòng ngừa Ma túy

## Tổng quan
Dựa trên mockdata đã tạo, đây là đề xuất cấu trúc database để hỗ trợ đầy đủ các tính năng của hệ thống học trực tuyến.

## 1. Bảng Users (Người dùng)
```sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role ENUM('USER', 'SPECIALIST', 'ADMIN', 'MANAGER') NOT NULL,
    phone_number VARCHAR(20),
    address TEXT,
    date_of_birth DATE,
    gender ENUM('male', 'female', 'other'),
    profile_picture VARCHAR(500),
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 2. Bảng Courses (Khóa học)
```sql
CREATE TABLE courses (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    duration INT NOT NULL, -- in minutes
    thumbnail VARCHAR(500),
    content LONGTEXT,
    difficulty ENUM('beginner', 'intermediate', 'advanced') DEFAULT 'beginner',
    instructor VARCHAR(255),
    certificate BOOLEAN DEFAULT FALSE,
    prerequisites TEXT,
    total_lessons INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 3. Bảng Course_Audience_Types (Đối tượng khóa học)
```sql
CREATE TABLE course_audience_types (
    id VARCHAR(36) PRIMARY KEY,
    course_id VARCHAR(36) NOT NULL,
    audience_type ENUM('student', 'parent', 'teacher', 'general') NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
```

## 4. Bảng Course_Tags (Thẻ khóa học)
```sql
CREATE TABLE course_tags (
    id VARCHAR(36) PRIMARY KEY,
    course_id VARCHAR(36) NOT NULL,
    tag VARCHAR(100) NOT NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
```

## 5. Bảng Lessons (Bài học)
```sql
CREATE TABLE lessons (
    id VARCHAR(36) PRIMARY KEY,
    course_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    type ENUM('video', 'text', 'quiz', 'interactive', 'assignment') NOT NULL,
    duration INT NOT NULL, -- in minutes
    order_index INT NOT NULL,
    content LONGTEXT,
    video_url VARCHAR(500),
    is_required BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);
```

## 6. Bảng Lesson_Materials (Tài liệu bài học)
```sql
CREATE TABLE lesson_materials (
    id VARCHAR(36) PRIMARY KEY,
    lesson_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    type ENUM('pdf', 'doc', 'image', 'link', 'video') NOT NULL,
    url VARCHAR(500) NOT NULL,
    description TEXT,
    downloadable BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
```

## 7. Bảng Quizzes (Bài kiểm tra)
```sql
CREATE TABLE quizzes (
    id VARCHAR(36) PRIMARY KEY,
    lesson_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    time_limit INT, -- in minutes
    passing_score INT NOT NULL, -- percentage
    max_attempts INT DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
```

## 8. Bảng Quiz_Questions (Câu hỏi kiểm tra)
```sql
CREATE TABLE quiz_questions (
    id VARCHAR(36) PRIMARY KEY,
    quiz_id VARCHAR(36) NOT NULL,
    question TEXT NOT NULL,
    type ENUM('multiple_choice', 'true_false', 'short_answer', 'essay') NOT NULL,
    correct_answer TEXT NOT NULL,
    explanation TEXT,
    points INT DEFAULT 1,
    order_index INT NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE
);
```

## 9. Bảng Quiz_Question_Options (Lựa chọn câu hỏi)
```sql
CREATE TABLE quiz_question_options (
    id VARCHAR(36) PRIMARY KEY,
    question_id VARCHAR(36) NOT NULL,
    option_text VARCHAR(500) NOT NULL,
    order_index INT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES quiz_questions(id) ON DELETE CASCADE
);
```

## 10. Bảng Course_Enrollments (Đăng ký khóa học)
```sql
CREATE TABLE course_enrollments (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    course_id VARCHAR(36) NOT NULL,
    status ENUM('enrolled', 'in_progress', 'completed', 'dropped') DEFAULT 'enrolled',
    progress DECIMAL(5,2) DEFAULT 0.00, -- percentage
    completed BOOLEAN DEFAULT FALSE,
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    certificate_issued BOOLEAN DEFAULT FALSE,
    certificate_url VARCHAR(500),
    final_score DECIMAL(5,2),
    time_spent INT DEFAULT 0, -- in minutes
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (user_id, course_id)
);
```

## 11. Bảng Lesson_Progress (Tiến độ bài học)
```sql
CREATE TABLE lesson_progress (
    id VARCHAR(36) PRIMARY KEY,
    enrollment_id VARCHAR(36) NOT NULL,
    lesson_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    progress DECIMAL(5,2) DEFAULT 0.00, -- percentage
    time_spent INT DEFAULT 0, -- in minutes
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    last_accessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollment_id) REFERENCES course_enrollments(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_lesson_progress (enrollment_id, lesson_id)
);
```

## 12. Bảng Quiz_Attempts (Lần làm bài kiểm tra)
```sql
CREATE TABLE quiz_attempts (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    quiz_id VARCHAR(36) NOT NULL,
    enrollment_id VARCHAR(36) NOT NULL,
    attempt_number INT NOT NULL,
    score DECIMAL(5,2) NOT NULL, -- percentage
    started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    time_spent INT DEFAULT 0, -- in minutes
    passed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
    FOREIGN KEY (enrollment_id) REFERENCES course_enrollments(id) ON DELETE CASCADE
);
```

## 13. Bảng Quiz_Answers (Câu trả lời)
```sql
CREATE TABLE quiz_answers (
    id VARCHAR(36) PRIMARY KEY,
    attempt_id VARCHAR(36) NOT NULL,
    question_id VARCHAR(36) NOT NULL,
    answer TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    points DECIMAL(5,2) DEFAULT 0,
    FOREIGN KEY (attempt_id) REFERENCES quiz_attempts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES quiz_questions(id) ON DELETE CASCADE
);
```

## 14. Bảng Course_Reviews (Đánh giá khóa học)
```sql
CREATE TABLE course_reviews (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    course_id VARCHAR(36) NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_review (user_id, course_id)
);
```

## 15. Bảng Course_Notes (Ghi chú khóa học)
```sql
CREATE TABLE course_notes (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    course_id VARCHAR(36) NOT NULL,
    lesson_id VARCHAR(36),
    content TEXT NOT NULL,
    timestamp INT, -- for video notes (seconds)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);
```

## 16. Bảng Course_Discussions (Thảo luận khóa học)
```sql
CREATE TABLE course_discussions (
    id VARCHAR(36) PRIMARY KEY,
    course_id VARCHAR(36) NOT NULL,
    lesson_id VARCHAR(36),
    user_id VARCHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## 17. Bảng Discussion_Replies (Phản hồi thảo luận)
```sql
CREATE TABLE discussion_replies (
    id VARCHAR(36) PRIMARY KEY,
    discussion_id VARCHAR(36) NOT NULL,
    user_id VARCHAR(36) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (discussion_id) REFERENCES course_discussions(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## Indexes đề xuất
```sql
-- Performance indexes
CREATE INDEX idx_course_enrollments_user_id ON course_enrollments(user_id);
CREATE INDEX idx_course_enrollments_course_id ON course_enrollments(course_id);
CREATE INDEX idx_lesson_progress_enrollment_id ON lesson_progress(enrollment_id);
CREATE INDEX idx_lesson_progress_user_id ON lesson_progress(user_id);
CREATE INDEX idx_quiz_attempts_user_id ON quiz_attempts(user_id);
CREATE INDEX idx_quiz_attempts_quiz_id ON quiz_attempts(quiz_id);
CREATE INDEX idx_lessons_course_id_order ON lessons(course_id, order_index);
CREATE INDEX idx_quiz_questions_quiz_id_order ON quiz_questions(quiz_id, order_index);
CREATE INDEX idx_course_discussions_course_id ON course_discussions(course_id);
CREATE INDEX idx_course_discussions_lesson_id ON course_discussions(lesson_id);
```

## Lưu ý triển khai
1. **UUID**: Sử dụng UUID cho primary keys để tránh conflict khi scale
2. **Soft Delete**: Có thể thêm cột `deleted_at` cho các bảng quan trọng
3. **Audit Trail**: Có thể thêm bảng audit để track changes
4. **File Storage**: URLs trong database chỉ là references, files thực tế nên lưu trên cloud storage
5. **Caching**: Implement Redis cache cho course content và user progress
6. **Backup**: Thiết lập backup strategy cho data quan trọng
