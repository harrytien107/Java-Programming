import { 
  Lesson, 
  Quiz, 
  QuizQuestion, 
  CourseEnrollment, 
  LessonProgress, 
  QuizAttempt, 
  CourseReview, 
  CourseNote, 
  CourseDiscussion,
  LessonMaterial
} from '../types/course';

// Mock Lessons for Course ID "1" - "Nhận thức về ma túy cho học sinh"
export const mockLessonsForCourse1: Lesson[] = [
  {
    id: 'lesson_1_1',
    courseId: '1',
    title: 'Bài 1: Giới thiệu về ma túy và tác hại',
    description: 'Tìm hiểu khái niệm cơ bản về ma túy, các loại ma túy phổ biến và tác hại của chúng đối với sức khỏe và xã hội.',
    type: 'video',
    duration: 15,
    order: 1,
    content: `
      <h2>Mục tiêu bài học</h2>
      <ul>
        <li>Hiểu được khái niệm ma túy và chất gây nghiện</li>
        <li>Nhận biết các loại ma túy phổ biến</li>
        <li>Nắm được tác hại của ma túy đối với cơ thể và tinh thần</li>
      </ul>
      
      <h2>Nội dung chính</h2>
      <p>Ma túy là các chất có khả năng gây nghiện, tác động tiêu cực đến hệ thần kinh trung ương...</p>
    `,
    videoUrl: 'https://www.youtube.com/embed/dQw4w9WgXcQ',
    materials: [
      {
        id: 'material_1_1_1',
        lessonId: 'lesson_1_1',
        title: 'Tài liệu: Các loại ma túy phổ biến',
        type: 'pdf',
        url: '/materials/drug-types.pdf',
        description: 'Tài liệu chi tiết về các loại ma túy thường gặp',
        downloadable: true
      },
      {
        id: 'material_1_1_2',
        lessonId: 'lesson_1_1',
        title: 'Infographic: Tác hại của ma túy',
        type: 'image',
        url: '/materials/drug-effects-infographic.jpg',
        description: 'Hình ảnh minh họa tác hại của ma túy',
        downloadable: true
      }
    ],
    isRequired: true,
    createdAt: new Date('2023-02-01'),
    updatedAt: new Date('2023-02-01'),
  },
  {
    id: 'lesson_1_2',
    courseId: '1',
    title: 'Bài 2: Nhận biết các loại ma túy phổ biến',
    description: 'Học cách nhận biết các dấu hiệu và đặc điểm của các loại ma túy thường gặp trong đời sống.',
    type: 'interactive',
    duration: 12,
    order: 2,
    content: `
      <h2>Các loại ma túy phổ biến</h2>
      <h3>1. Ma túy tự nhiên</h3>
      <p>Cần sa, thuốc phiện, cocaine...</p>
      
      <h3>2. Ma túy tổng hợp</h3>
      <p>Ecstasy, methamphetamine, ketamine...</p>
      
      <h2>Cách nhận biết</h2>
      <p>Quan sát hình dạng, màu sắc, mùi vị...</p>
    `,
    materials: [
      {
        id: 'material_1_2_1',
        lessonId: 'lesson_1_2',
        title: 'Bộ thẻ nhận biết ma túy',
        type: 'link',
        url: 'https://interactive-drug-cards.example.com',
        description: 'Công cụ tương tác để luyện tập nhận biết ma túy',
        downloadable: false
      }
    ],
    quiz: {
      id: 'quiz_1_2',
      lessonId: 'lesson_1_2',
      title: 'Kiểm tra nhận biết ma túy',
      description: 'Bài kiểm tra khả năng nhận biết các loại ma túy phổ biến',
      timeLimit: 10,
      passingScore: 70,
      maxAttempts: 3,
      questions: [
        {
          id: 'q_1_2_1',
          quizId: 'quiz_1_2',
          question: 'Cần sa thuộc loại ma túy nào?',
          type: 'multiple_choice',
          options: ['Ma túy tự nhiên', 'Ma túy tổng hợp', 'Ma túy bán tổng hợp', 'Không phải ma túy'],
          correctAnswer: 'Ma túy tự nhiên',
          explanation: 'Cần sa là ma túy tự nhiên được chiết xuất từ cây cần sa.',
          points: 10,
          order: 1
        },
        {
          id: 'q_1_2_2',
          quizId: 'quiz_1_2',
          question: 'Ecstasy có màu sắc đặc trưng nào?',
          type: 'multiple_choice',
          options: ['Trắng', 'Đỏ', 'Nhiều màu sắc khác nhau', 'Chỉ có màu xanh'],
          correctAnswer: 'Nhiều màu sắc khác nhau',
          explanation: 'Ecstasy thường có nhiều màu sắc và hình dạng khác nhau.',
          points: 10,
          order: 2
        }
      ],
      createdAt: new Date('2023-02-01'),
      updatedAt: new Date('2023-02-01'),
    },
    isRequired: true,
    createdAt: new Date('2023-02-01'),
    updatedAt: new Date('2023-02-01'),
  },
  {
    id: 'lesson_1_3',
    courseId: '1',
    title: 'Bài 3: Kỹ năng từ chối và phòng tránh',
    description: 'Phát triển kỹ năng từ chối ma túy một cách tự tin và hiệu quả trong các tình huống khác nhau.',
    type: 'video',
    duration: 18,
    order: 3,
    content: `
      <h2>Kỹ năng từ chối hiệu quả</h2>
      <h3>1. Nói "KHÔNG" một cách quyết đoán</h3>
      <p>Sử dụng ngôn ngữ cơ thể và giọng nói tự tin...</p>
      
      <h3>2. Đưa ra lý do hợp lý</h3>
      <p>Chuẩn bị sẵn các lý do để từ chối...</p>
      
      <h3>3. Tránh xa những tình huống nguy hiểm</h3>
      <p>Nhận biết và tránh các môi trường có nguy cơ...</p>
    `,
    videoUrl: 'https://www.youtube.com/embed/dQw4w9WgXcQ',
    materials: [
      {
        id: 'material_1_3_1',
        lessonId: 'lesson_1_3',
        title: 'Kịch bản luyện tập từ chối',
        type: 'doc',
        url: '/materials/refusal-scenarios.docx',
        description: 'Các tình huống mô phỏng để luyện tập kỹ năng từ chối',
        downloadable: true
      }
    ],
    isRequired: true,
    createdAt: new Date('2023-02-01'),
    updatedAt: new Date('2023-02-01'),
  },
  {
    id: 'lesson_1_4',
    courseId: '1',
    title: 'Bài 4: Hỗ trợ người thân và bạn bè',
    description: 'Học cách nhận biết dấu hiệu và hỗ trợ người thân, bạn bè có nguy cơ hoặc đang sử dụng ma túy.',
    type: 'text',
    duration: 10,
    order: 4,
    content: `
      <h2>Nhận biết dấu hiệu</h2>
      <h3>Dấu hiệu về hành vi</h3>
      <ul>
        <li>Thay đổi thói quen sinh hoạt</li>
        <li>Tránh né gia đình và bạn bè</li>
        <li>Thành tích học tập giảm sút</li>
      </ul>
      
      <h3>Dấu hiệu về thể chất</h3>
      <ul>
        <li>Mắt đỏ, đồng tử giãn hoặc co</li>
        <li>Sụt cân hoặc tăng cân bất thường</li>
        <li>Mùi lạ trên người</li>
      </ul>
      
      <h2>Cách hỗ trợ</h2>
      <p>Lắng nghe, không phán xét, khuyến khích tìm kiếm sự giúp đỡ chuyên nghiệp...</p>
    `,
    materials: [
      {
        id: 'material_1_4_1',
        lessonId: 'lesson_1_4',
        title: 'Danh sách hotline hỗ trợ',
        type: 'pdf',
        url: '/materials/support-hotlines.pdf',
        description: 'Các số điện thoại hỗ trợ khẩn cấp và tư vấn',
        downloadable: true
      }
    ],
    isRequired: true,
    createdAt: new Date('2023-02-01'),
    updatedAt: new Date('2023-02-01'),
  },
  {
    id: 'lesson_1_5',
    courseId: '1',
    title: 'Bài 5: Tổng kết và bài kiểm tra cuối khóa',
    description: 'Ôn tập kiến thức đã học và thực hiện bài kiểm tra cuối khóa để đánh giá mức độ hiểu biết.',
    type: 'quiz',
    duration: 15,
    order: 5,
    content: `
      <h2>Tổng kết khóa học</h2>
      <p>Qua khóa học này, bạn đã học được:</p>
      <ul>
        <li>Kiến thức cơ bản về ma túy và tác hại</li>
        <li>Cách nhận biết các loại ma túy phổ biến</li>
        <li>Kỹ năng từ chối ma túy hiệu quả</li>
        <li>Cách hỗ trợ người thân và bạn bè</li>
      </ul>
      
      <p>Hãy thực hiện bài kiểm tra cuối khóa để kiểm tra kiến thức của mình!</p>
    `,
    quiz: {
      id: 'quiz_1_5',
      lessonId: 'lesson_1_5',
      title: 'Bài kiểm tra cuối khóa',
      description: 'Bài kiểm tra tổng hợp kiến thức toàn khóa học',
      timeLimit: 20,
      passingScore: 80,
      maxAttempts: 2,
      questions: [
        {
          id: 'q_1_5_1',
          quizId: 'quiz_1_5',
          question: 'Ma túy tác động chủ yếu đến hệ thống nào của cơ thể?',
          type: 'multiple_choice',
          options: ['Hệ tiêu hóa', 'Hệ thần kinh trung ương', 'Hệ tuần hoàn', 'Hệ hô hấp'],
          correctAnswer: 'Hệ thần kinh trung ương',
          explanation: 'Ma túy chủ yếu tác động đến hệ thần kinh trung ương, gây ra các thay đổi về tâm lý và hành vi.',
          points: 20,
          order: 1
        },
        {
          id: 'q_1_5_2',
          quizId: 'quiz_1_5',
          question: 'Khi bị mời sử dụng ma túy, cách từ chối hiệu quả nhất là gì?',
          type: 'multiple_choice',
          options: [
            'Nói "để tôi suy nghĩ"',
            'Nói "KHÔNG" một cách quyết đoán và rời khỏi hiện trường',
            'Giả vờ đồng ý nhưng không sử dụng',
            'Im lặng và tránh né'
          ],
          correctAnswer: 'Nói "KHÔNG" một cách quyết đoán và rời khỏi hiện trường',
          explanation: 'Từ chối quyết đoán và rời khỏi tình huống nguy hiểm là cách hiệu quả nhất.',
          points: 20,
          order: 2
        },
        {
          id: 'q_1_5_3',
          quizId: 'quiz_1_5',
          question: 'Việc sử dụng ma túy có thể dẫn đến những hậu quả nào? (Chọn tất cả đáp án đúng)',
          type: 'multiple_choice',
          options: ['Nghiện', 'Suy giảm trí nhớ', 'Các bệnh về tâm thần', 'Tất cả các đáp án trên'],
          correctAnswer: 'Tất cả các đáp án trên',
          explanation: 'Ma túy có thể gây ra tất cả những hậu quả nghiêm trọng này và nhiều hậu quả khác.',
          points: 20,
          order: 3
        }
      ],
      createdAt: new Date('2023-02-01'),
      updatedAt: new Date('2023-02-01'),
    },
    isRequired: true,
    createdAt: new Date('2023-02-01'),
    updatedAt: new Date('2023-02-01'),
  }
];

// Mock Course Enrollments
export const mockCourseEnrollments: CourseEnrollment[] = [
  {
    id: 'enrollment_1',
    userId: '2', // Member user
    courseId: '1',
    status: 'in_progress',
    progress: 60, // 3/5 lessons completed
    completed: false,
    startedAt: new Date('2023-06-01'),
    lastAccessedAt: new Date('2023-06-15'),
    timeSpent: 45, // 45 minutes spent so far
    finalScore: undefined
  },
  {
    id: 'enrollment_2',
    userId: '3', // Staff user
    courseId: '1',
    status: 'completed',
    progress: 100,
    completed: true,
    startedAt: new Date('2023-05-15'),
    completedAt: new Date('2023-05-20'),
    lastAccessedAt: new Date('2023-05-20'),
    certificateIssued: true,
    certificateUrl: '/certificates/cert_enrollment_2.pdf',
    finalScore: 85,
    timeSpent: 70
  }
];

// Mock Lesson Progress
export const mockLessonProgress: LessonProgress[] = [
  // For enrollment_1 (user 2, in progress)
  {
    id: 'progress_1_1',
    enrollmentId: 'enrollment_1',
    lessonId: 'lesson_1_1',
    userId: '2',
    completed: true,
    progress: 100,
    timeSpent: 15,
    startedAt: new Date('2023-06-01T09:00:00'),
    completedAt: new Date('2023-06-01T09:15:00'),
    lastAccessedAt: new Date('2023-06-01T09:15:00')
  },
  {
    id: 'progress_1_2',
    enrollmentId: 'enrollment_1',
    lessonId: 'lesson_1_2',
    userId: '2',
    completed: true,
    progress: 100,
    timeSpent: 12,
    startedAt: new Date('2023-06-02T14:00:00'),
    completedAt: new Date('2023-06-02T14:12:00'),
    lastAccessedAt: new Date('2023-06-02T14:12:00')
  },
  {
    id: 'progress_1_3',
    enrollmentId: 'enrollment_1',
    lessonId: 'lesson_1_3',
    userId: '2',
    completed: true,
    progress: 100,
    timeSpent: 18,
    startedAt: new Date('2023-06-05T10:00:00'),
    completedAt: new Date('2023-06-05T10:18:00'),
    lastAccessedAt: new Date('2023-06-05T10:18:00')
  },
  {
    id: 'progress_1_4',
    enrollmentId: 'enrollment_1',
    lessonId: 'lesson_1_4',
    userId: '2',
    completed: false,
    progress: 50,
    timeSpent: 5,
    startedAt: new Date('2023-06-15T16:00:00'),
    lastAccessedAt: new Date('2023-06-15T16:05:00')
  },
  // For enrollment_2 (user 3, completed)
  {
    id: 'progress_2_1',
    enrollmentId: 'enrollment_2',
    lessonId: 'lesson_1_1',
    userId: '3',
    completed: true,
    progress: 100,
    timeSpent: 15,
    startedAt: new Date('2023-05-15T09:00:00'),
    completedAt: new Date('2023-05-15T09:15:00'),
    lastAccessedAt: new Date('2023-05-15T09:15:00')
  },
  {
    id: 'progress_2_2',
    enrollmentId: 'enrollment_2',
    lessonId: 'lesson_1_2',
    userId: '3',
    completed: true,
    progress: 100,
    timeSpent: 12,
    startedAt: new Date('2023-05-16T14:00:00'),
    completedAt: new Date('2023-05-16T14:12:00'),
    lastAccessedAt: new Date('2023-05-16T14:12:00')
  },
  {
    id: 'progress_2_3',
    enrollmentId: 'enrollment_2',
    lessonId: 'lesson_1_3',
    userId: '3',
    completed: true,
    progress: 100,
    timeSpent: 18,
    startedAt: new Date('2023-05-17T10:00:00'),
    completedAt: new Date('2023-05-17T10:18:00'),
    lastAccessedAt: new Date('2023-05-17T10:18:00')
  },
  {
    id: 'progress_2_4',
    enrollmentId: 'enrollment_2',
    lessonId: 'lesson_1_4',
    userId: '3',
    completed: true,
    progress: 100,
    timeSpent: 10,
    startedAt: new Date('2023-05-18T16:00:00'),
    completedAt: new Date('2023-05-18T16:10:00'),
    lastAccessedAt: new Date('2023-05-18T16:10:00')
  },
  {
    id: 'progress_2_5',
    enrollmentId: 'enrollment_2',
    lessonId: 'lesson_1_5',
    userId: '3',
    completed: true,
    progress: 100,
    timeSpent: 15,
    startedAt: new Date('2023-05-20T11:00:00'),
    completedAt: new Date('2023-05-20T11:15:00'),
    lastAccessedAt: new Date('2023-05-20T11:15:00')
  }
];

// Mock Quiz Attempts
export const mockQuizAttempts: QuizAttempt[] = [
  {
    id: 'attempt_1',
    userId: '2',
    quizId: 'quiz_1_2',
    enrollmentId: 'enrollment_1',
    attemptNumber: 1,
    score: 75,
    answers: [
      {
        questionId: 'q_1_2_1',
        answer: 'Ma túy tự nhiên',
        isCorrect: true,
        points: 10
      },
      {
        questionId: 'q_1_2_2',
        answer: 'Chỉ có màu xanh',
        isCorrect: false,
        points: 0
      }
    ],
    startedAt: new Date('2023-06-02T14:12:00'),
    completedAt: new Date('2023-06-02T14:17:00'),
    timeSpent: 5,
    passed: true
  },
  {
    id: 'attempt_2',
    userId: '3',
    quizId: 'quiz_1_2',
    enrollmentId: 'enrollment_2',
    attemptNumber: 1,
    score: 100,
    answers: [
      {
        questionId: 'q_1_2_1',
        answer: 'Ma túy tự nhiên',
        isCorrect: true,
        points: 10
      },
      {
        questionId: 'q_1_2_2',
        answer: 'Nhiều màu sắc khác nhau',
        isCorrect: true,
        points: 10
      }
    ],
    startedAt: new Date('2023-05-16T14:12:00'),
    completedAt: new Date('2023-05-16T14:16:00'),
    timeSpent: 4,
    passed: true
  },
  {
    id: 'attempt_3',
    userId: '3',
    quizId: 'quiz_1_5',
    enrollmentId: 'enrollment_2',
    attemptNumber: 1,
    score: 85,
    answers: [
      {
        questionId: 'q_1_5_1',
        answer: 'Hệ thần kinh trung ương',
        isCorrect: true,
        points: 20
      },
      {
        questionId: 'q_1_5_2',
        answer: 'Nói "KHÔNG" một cách quyết đoán và rời khỏi hiện trường',
        isCorrect: true,
        points: 20
      },
      {
        questionId: 'q_1_5_3',
        answer: 'Nghiện',
        isCorrect: false,
        points: 0
      }
    ],
    startedAt: new Date('2023-05-20T11:15:00'),
    completedAt: new Date('2023-05-20T11:25:00'),
    timeSpent: 10,
    passed: true
  }
];

// Mock Course Reviews
export const mockCourseReviews: CourseReview[] = [
  {
    id: 'review_1',
    userId: '3',
    courseId: '1',
    rating: 5,
    comment: 'Khóa học rất bổ ích và dễ hiểu. Nội dung được trình bày một cách khoa học và thực tế. Tôi đã học được nhiều kiến thức quan trọng về phòng chống ma túy.',
    createdAt: new Date('2023-05-21'),
    updatedAt: new Date('2023-05-21'),
    userName: 'Staff User',
    userAvatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
  },
  {
    id: 'review_2',
    userId: '4',
    courseId: '1',
    rating: 4,
    comment: 'Khóa học có nội dung tốt, video minh họa rõ ràng. Tuy nhiên, tôi mong muốn có thêm nhiều tình huống thực tế để luyện tập.',
    createdAt: new Date('2023-06-10'),
    updatedAt: new Date('2023-06-10'),
    userName: 'Consultant User',
    userAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
  },
  {
    id: 'review_3',
    userId: '5',
    courseId: '1',
    rating: 5,
    comment: 'Tuyệt vời! Khóa học này đã giúp tôi hiểu rõ hơn về tác hại của ma túy và cách bảo vệ bản thân. Rất khuyến khích mọi người tham gia.',
    createdAt: new Date('2023-06-05'),
    updatedAt: new Date('2023-06-05'),
    userName: 'Manager User',
    userAvatar: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
  }
];

// Mock Course Notes
export const mockCourseNotes: CourseNote[] = [
  {
    id: 'note_1',
    userId: '2',
    courseId: '1',
    lessonId: 'lesson_1_1',
    content: 'Ma túy gây nghiện do tác động lên hệ thần kinh trung ương, làm thay đổi cấu trúc và chức năng của não bộ.',
    timestamp: 300, // 5 minutes into the video
    createdAt: new Date('2023-06-01T09:05:00'),
    updatedAt: new Date('2023-06-01T09:05:00')
  },
  {
    id: 'note_2',
    userId: '2',
    courseId: '1',
    lessonId: 'lesson_1_2',
    content: 'Cần nhớ: Cần sa có mùi đặc trưng, thường được cuốn thành điếu thuốc hoặc sử dụng qua bong.',
    createdAt: new Date('2023-06-02T14:08:00'),
    updatedAt: new Date('2023-06-02T14:08:00')
  },
  {
    id: 'note_3',
    userId: '2',
    courseId: '1',
    lessonId: 'lesson_1_3',
    content: 'Kỹ năng từ chối hiệu quả: Nói KHÔNG rõ ràng, đưa ra lý do, rời khỏi tình huống nguy hiểm.',
    timestamp: 600, // 10 minutes into the video
    createdAt: new Date('2023-06-05T10:10:00'),
    updatedAt: new Date('2023-06-05T10:10:00')
  }
];

// Mock Course Discussions
export const mockCourseDiscussions: CourseDiscussion[] = [
  {
    id: 'discussion_1',
    courseId: '1',
    lessonId: 'lesson_1_1',
    userId: '2',
    title: 'Câu hỏi về tác hại lâu dài của ma túy',
    content: 'Xin chào mọi người, tôi muốn hỏi về tác hại lâu dài của việc sử dụng ma túy đối với não bộ. Có phải tất cả các tổn thương đều không thể phục hồi được không?',
    replies: [
      {
        id: 'reply_1_1',
        discussionId: 'discussion_1',
        userId: '4',
        content: 'Câu hỏi rất hay! Tùy thuộc vào loại ma túy và mức độ sử dụng, một số tổn thương có thể phục hồi được nếu ngừng sử dụng sớm và có điều trị phù hợp. Tuy nhiên, một số tổn thương có thể là vĩnh viễn.',
        createdAt: new Date('2023-06-01T15:00:00'),
        updatedAt: new Date('2023-06-01T15:00:00'),
        userName: 'Consultant User',
        userAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
      },
      {
        id: 'reply_1_2',
        discussionId: 'discussion_1',
        userId: '3',
        content: 'Bổ sung thêm: Não bộ có khả năng tự phục hồi nhất định, đặc biệt ở người trẻ. Việc tập luyện, ăn uống lành mạnh và có lối sống tích cực sẽ hỗ trợ quá trình phục hồi.',
        createdAt: new Date('2023-06-01T16:30:00'),
        updatedAt: new Date('2023-06-01T16:30:00'),
        userName: 'Staff User',
        userAvatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
      }
    ],
    createdAt: new Date('2023-06-01T14:30:00'),
    updatedAt: new Date('2023-06-01T16:30:00'),
    userName: 'Member User',
    userAvatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
  },
  {
    id: 'discussion_2',
    courseId: '1',
    lessonId: 'lesson_1_3',
    userId: '5',
    title: 'Chia sẻ kinh nghiệm từ chối ma túy',
    content: 'Tôi muốn chia sẻ một tình huống thực tế mà tôi đã gặp. Khi bị bạn bè rủ rê, tôi đã nói: "Tôi đang tập thể thao và cần giữ sức khỏe tốt". Cách này khá hiệu quả và không làm tổn thương mối quan hệ.',
    replies: [
      {
        id: 'reply_2_1',
        discussionId: 'discussion_2',
        userId: '2',
        content: 'Cảm ơn bạn đã chia sẻ! Đây là một cách từ chối rất thông minh và tích cực. Việc đưa ra lý do liên quan đến sức khỏe thường được mọi người hiểu và tôn trọng.',
        createdAt: new Date('2023-06-06T10:00:00'),
        updatedAt: new Date('2023-06-06T10:00:00'),
        userName: 'Member User',
        userAvatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
      }
    ],
    createdAt: new Date('2023-06-05T20:00:00'),
    updatedAt: new Date('2023-06-06T10:00:00'),
    userName: 'Manager User',
    userAvatar: 'https://images.unsplash.com/photo-1494790108755-2616b612b786?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
  },
  {
    id: 'discussion_3',
    courseId: '1',
    userId: '3',
    title: 'Thảo luận chung về khóa học',
    content: 'Mọi người cảm thấy khóa học này như thế nào? Phần nào hữu ích nhất đối với các bạn?',
    replies: [
      {
        id: 'reply_3_1',
        discussionId: 'discussion_3',
        userId: '2',
        content: 'Tôi thấy phần kỹ năng từ chối rất thực tế. Trước đây tôi không biết cách từ chối mà không làm tổn thương mối quan hệ.',
        createdAt: new Date('2023-06-10T14:00:00'),
        updatedAt: new Date('2023-06-10T14:00:00'),
        userName: 'Member User',
        userAvatar: 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
      },
      {
        id: 'reply_3_2',
        discussionId: 'discussion_3',
        userId: '4',
        content: 'Đối với tôi, phần nhận biết dấu hiệu ở người thân rất quan trọng. Giúp tôi có thể hỗ trợ kịp thời nếu cần.',
        createdAt: new Date('2023-06-10T15:30:00'),
        updatedAt: new Date('2023-06-10T15:30:00'),
        userName: 'Consultant User',
        userAvatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
      }
    ],
    createdAt: new Date('2023-06-10T13:00:00'),
    updatedAt: new Date('2023-06-10T15:30:00'),
    userName: 'Staff User',
    userAvatar: 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-4.0.3&auto=format&fit=crop&w=150&q=80'
  }
];

// Helper functions to get data for specific user/course
export const getCourseEnrollmentByUser = (userId: string, courseId: string): CourseEnrollment | undefined => {
  return mockCourseEnrollments.find(e => e.userId === userId && e.courseId === courseId);
};

export const getLessonProgressByEnrollment = (enrollmentId: string): LessonProgress[] => {
  return mockLessonProgress.filter(p => p.enrollmentId === enrollmentId);
};

export const getQuizAttemptsByUser = (userId: string, quizId: string): QuizAttempt[] => {
  return mockQuizAttempts.filter(a => a.userId === userId && a.quizId === quizId);
};

export const getCourseNotesByUser = (userId: string, courseId: string): CourseNote[] => {
  return mockCourseNotes.filter(n => n.userId === userId && n.courseId === courseId);
};

export const getCourseDiscussions = (courseId: string, lessonId?: string): CourseDiscussion[] => {
  return mockCourseDiscussions.filter(d =>
    d.courseId === courseId && (lessonId ? d.lessonId === lessonId : true)
  );
};
