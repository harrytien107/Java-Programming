import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  Radio,
  RadioGroup,
  FormControlLabel,
  FormControl,
  FormLabel,
  LinearProgress,
  Paper,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Chip
} from '@mui/material';
import {
  Timer as TimerIcon,
  CheckCircle as CheckCircleIcon,
  Cancel as CancelIcon,
  Refresh as RefreshIcon
} from '@mui/icons-material';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import { mockLessonsForCourse1, mockQuizAttempts, getQuizAttemptsByUser } from '../../utils/courseDetailMockData';
import { Quiz, QuizQuestion, QuizAttempt, QuizAnswer } from '../../types/course';

const QuizPage: React.FC = () => {
  const { courseId, lessonId } = useParams<{ courseId: string; lessonId: string }>();
  const { user } = useAuth();
  const navigate = useNavigate();

  // State management
  const [quiz, setQuiz] = useState<Quiz | null>(null);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [answers, setAnswers] = useState<{ [questionId: string]: string }>({});
  const [timeLeft, setTimeLeft] = useState<number>(0);
  const [quizStarted, setQuizStarted] = useState(false);
  const [quizCompleted, setQuizCompleted] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const [score, setScore] = useState(0);
  const [previousAttempts, setPreviousAttempts] = useState<QuizAttempt[]>([]);
  const [confirmDialogOpen, setConfirmDialogOpen] = useState(false);

  useEffect(() => {
    // Find the lesson and its quiz
    const lesson = mockLessonsForCourse1.find(l => l.id === lessonId);
    if (lesson?.quiz) {
      setQuiz(lesson.quiz);
      setTimeLeft((lesson.quiz.timeLimit || 30) * 60); // Convert to seconds
      
      // Load previous attempts
      if (user) {
        const attempts = getQuizAttemptsByUser(user.id, lesson.quiz.id);
        setPreviousAttempts(attempts);
      }
    }
  }, [lessonId, user]);

  useEffect(() => {
    let timer: NodeJS.Timeout;
    if (quizStarted && timeLeft > 0 && !quizCompleted) {
      timer = setTimeout(() => {
        setTimeLeft(timeLeft - 1);
      }, 1000);
    } else if (timeLeft === 0 && quizStarted && !quizCompleted) {
      handleSubmitQuiz();
    }
    return () => clearTimeout(timer);
  }, [timeLeft, quizStarted, quizCompleted]);

  const formatTime = (seconds: number) => {
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;
    return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`;
  };

  const handleStartQuiz = () => {
    setQuizStarted(true);
    setCurrentQuestionIndex(0);
    setAnswers({});
    setQuizCompleted(false);
    setShowResults(false);
  };

  const handleAnswerChange = (questionId: string, answer: string) => {
    setAnswers(prev => ({
      ...prev,
      [questionId]: answer
    }));
  };

  const handleNextQuestion = () => {
    if (quiz && currentQuestionIndex < quiz.questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    }
  };

  const handlePreviousQuestion = () => {
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex(currentQuestionIndex - 1);
    }
  };

  const calculateScore = (): { score: number; quizAnswers: QuizAnswer[] } => {
    if (!quiz) return { score: 0, quizAnswers: [] };

    let totalPoints = 0;
    let earnedPoints = 0;
    const quizAnswers: QuizAnswer[] = [];

    quiz.questions.forEach(question => {
      totalPoints += question.points;
      const userAnswer = answers[question.id];
      const isCorrect = userAnswer === question.correctAnswer;
      const points = isCorrect ? question.points : 0;
      
      earnedPoints += points;
      quizAnswers.push({
        questionId: question.id,
        answer: userAnswer || '',
        isCorrect,
        points
      });
    });

    const percentage = Math.round((earnedPoints / totalPoints) * 100);
    return { score: percentage, quizAnswers };
  };

  const handleSubmitQuiz = () => {
    if (!quiz || !user) return;

    const { score: calculatedScore, quizAnswers } = calculateScore();
    setScore(calculatedScore);
    setQuizCompleted(true);
    setShowResults(true);

    // Create new attempt (in real app, this would be sent to API)
    const newAttempt: QuizAttempt = {
      id: `attempt_${Date.now()}`,
      userId: user.id,
      quizId: quiz.id,
      enrollmentId: 'enrollment_1', // This would come from context
      attemptNumber: previousAttempts.length + 1,
      score: calculatedScore,
      answers: quizAnswers,
      startedAt: new Date(Date.now() - ((quiz.timeLimit || 30) * 60 * 1000 - timeLeft * 1000)),
      completedAt: new Date(),
      timeSpent: Math.round(((quiz.timeLimit || 30) * 60 - timeLeft) / 60),
      passed: calculatedScore >= quiz.passingScore
    };

    setPreviousAttempts(prev => [...prev, newAttempt]);
  };

  const handleRetakeQuiz = () => {
    if (!quiz || previousAttempts.length >= quiz.maxAttempts) return;
    
    setQuizStarted(false);
    setQuizCompleted(false);
    setShowResults(false);
    setTimeLeft((quiz.timeLimit || 30) * 60);
    setAnswers({});
    setCurrentQuestionIndex(0);
  };

  const canRetake = quiz && previousAttempts.length < quiz.maxAttempts;
  const hasPassedQuiz = previousAttempts.some(attempt => attempt.passed);

  if (!quiz) {
    return (
      <Container>
        <Alert severity="error">Không tìm thấy bài kiểm tra.</Alert>
      </Container>
    );
  }

  if (!quizStarted) {
    return (
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Card>
          <CardContent>
            <Typography variant="h4" gutterBottom align="center">
              {quiz.title}
            </Typography>
            <Typography variant="body1" paragraph align="center">
              {quiz.description}
            </Typography>

            <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 2, my: 4 }}>
              <Paper sx={{ p: 2, textAlign: 'center' }}>
                <Typography variant="h6" color="primary">
                  {quiz.questions.length}
                </Typography>
                <Typography variant="body2">Câu hỏi</Typography>
              </Paper>
              <Paper sx={{ p: 2, textAlign: 'center' }}>
                <Typography variant="h6" color="primary">
                  {quiz.timeLimit || 'Không giới hạn'}
                </Typography>
                <Typography variant="body2">Phút</Typography>
              </Paper>
              <Paper sx={{ p: 2, textAlign: 'center' }}>
                <Typography variant="h6" color="primary">
                  {quiz.passingScore}%
                </Typography>
                <Typography variant="body2">Điểm đạt</Typography>
              </Paper>
              <Paper sx={{ p: 2, textAlign: 'center' }}>
                <Typography variant="h6" color="primary">
                  {quiz.maxAttempts}
                </Typography>
                <Typography variant="body2">Lần làm tối đa</Typography>
              </Paper>
            </Box>

            {previousAttempts.length > 0 && (
              <Box sx={{ mb: 3 }}>
                <Typography variant="h6" gutterBottom>Lịch sử làm bài</Typography>
                {previousAttempts.map((attempt, index) => (
                  <Paper key={attempt.id} sx={{ p: 2, mb: 1, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                      <Typography variant="body1">
                        Lần {attempt.attemptNumber}: {attempt.score}%
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {attempt.completedAt?.toLocaleString('vi-VN')}
                      </Typography>
                    </Box>
                    <Chip
                      icon={attempt.passed ? <CheckCircleIcon /> : <CancelIcon />}
                      label={attempt.passed ? 'Đạt' : 'Không đạt'}
                      color={attempt.passed ? 'success' : 'error'}
                      variant="outlined"
                    />
                  </Paper>
                ))}
              </Box>
            )}

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2 }}>
              <Button
                variant="outlined"
                onClick={() => navigate(`/courses/${courseId}/learn`)}
              >
                Quay lại bài học
              </Button>
              <Button
                variant="contained"
                onClick={handleStartQuiz}
                disabled={!canRetake && !hasPassedQuiz}
                size="large"
              >
                {previousAttempts.length === 0 ? 'Bắt đầu làm bài' : 'Làm lại'}
              </Button>
            </Box>

            {!canRetake && !hasPassedQuiz && (
              <Alert severity="warning" sx={{ mt: 2 }}>
                Bạn đã hết số lần làm bài cho phép.
              </Alert>
            )}
          </CardContent>
        </Card>
      </Container>
    );
  }

  if (showResults) {
    const passed = score >= quiz.passingScore;
    return (
      <Container maxWidth="md" sx={{ py: 4 }}>
        <Card>
          <CardContent>
            <Typography variant="h4" gutterBottom align="center">
              Kết quả bài kiểm tra
            </Typography>
            
            <Box sx={{ textAlign: 'center', my: 4 }}>
              <Typography variant="h2" color={passed ? 'success.main' : 'error.main'} gutterBottom>
                {score}%
              </Typography>
              <Chip
                icon={passed ? <CheckCircleIcon /> : <CancelIcon />}
                label={passed ? 'ĐẠT' : 'KHÔNG ĐẠT'}
                color={passed ? 'success' : 'error'}
                sx={{ fontSize: '1.2rem', padding: '8px 16px' }}
              />
            </Box>

            <Typography variant="body1" align="center" paragraph>
              {passed 
                ? 'Chúc mừng! Bạn đã hoàn thành bài kiểm tra thành công.'
                : `Bạn cần đạt tối thiểu ${quiz.passingScore}% để vượt qua bài kiểm tra.`
              }
            </Typography>

            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 4 }}>
              <Button
                variant="outlined"
                onClick={() => navigate(`/courses/${courseId}/learn`)}
              >
                Quay lại bài học
              </Button>
              {canRetake && !passed && (
                <Button
                  variant="contained"
                  onClick={handleRetakeQuiz}
                  startIcon={<RefreshIcon />}
                >
                  Làm lại ({previousAttempts.length}/{quiz.maxAttempts})
                </Button>
              )}
            </Box>
          </CardContent>
        </Card>
      </Container>
    );
  }

  const currentQuestion = quiz.questions[currentQuestionIndex];
  const progress = ((currentQuestionIndex + 1) / quiz.questions.length) * 100;

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      {/* Header */}
      <Paper sx={{ p: 2, mb: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h6">
          Câu {currentQuestionIndex + 1} / {quiz.questions.length}
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <TimerIcon color="primary" />
          <Typography variant="h6" color="primary">
            {formatTime(timeLeft)}
          </Typography>
        </Box>
      </Paper>

      {/* Progress */}
      <LinearProgress variant="determinate" value={progress} sx={{ mb: 3, height: 8, borderRadius: 4 }} />

      {/* Question */}
      <Card>
        <CardContent>
          <FormControl component="fieldset" fullWidth>
            <FormLabel component="legend">
              <Typography variant="h6" gutterBottom>
                {currentQuestion.question}
              </Typography>
            </FormLabel>
            
            {currentQuestion.type === 'multiple_choice' && currentQuestion.options && (
              <RadioGroup
                value={answers[currentQuestion.id] || ''}
                onChange={(e) => handleAnswerChange(currentQuestion.id, e.target.value)}
              >
                {currentQuestion.options.map((option, index) => (
                  <FormControlLabel
                    key={index}
                    value={option}
                    control={<Radio />}
                    label={option}
                    sx={{ mb: 1 }}
                  />
                ))}
              </RadioGroup>
            )}
          </FormControl>

          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 4 }}>
            <Button
              variant="outlined"
              onClick={handlePreviousQuestion}
              disabled={currentQuestionIndex === 0}
            >
              Câu trước
            </Button>

            {currentQuestionIndex === quiz.questions.length - 1 ? (
              <Button
                variant="contained"
                onClick={() => setConfirmDialogOpen(true)}
                color="success"
              >
                Nộp bài
              </Button>
            ) : (
              <Button
                variant="contained"
                onClick={handleNextQuestion}
              >
                Câu tiếp theo
              </Button>
            )}
          </Box>
        </CardContent>
      </Card>

      {/* Confirm Submit Dialog */}
      <Dialog open={confirmDialogOpen} onClose={() => setConfirmDialogOpen(false)}>
        <DialogTitle>Xác nhận nộp bài</DialogTitle>
        <DialogContent>
          <Typography>
            Bạn có chắc chắn muốn nộp bài? Bạn sẽ không thể thay đổi câu trả lời sau khi nộp.
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setConfirmDialogOpen(false)}>Hủy</Button>
          <Button onClick={handleSubmitQuiz} variant="contained" color="success">
            Nộp bài
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default QuizPage;
