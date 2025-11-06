import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import './App.css';

// Theme
import theme from './utils/theme';

// Context Providers
import { AuthProvider, useAuth } from './contexts/AuthContext';

// Layouts
import ClientLayout from './components/layout/ClientLayout';
import AdminLayout from './components/layout/AdminLayout';

// Pages
import HomePage from './pages/home/HomePage';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import CoursesPage from './pages/courses/CoursesPage';
import CourseDetailPage from './pages/courses/CourseDetailPage';
import CourseLearningPage from './pages/courses/CourseLearningPage';
import QuizPage from './pages/courses/QuizPage';
import ConsultantsPage from './pages/consultants/ConsultantsPage';
import ConsultantDetailPage from './pages/consultants/ConsultantDetailPage';
import SurveysPage from './pages/surveys/SurveysPageClient';
import SurveyDetailPage from './pages/surveys/SurveyDetailPage';
import SurveyHistoryPage from './pages/surveys/SurveyHistoryPage';
import AllSurveyHistoryPage from './pages/surveys/AllSurveyHistoryPage';
import AppointmentsPage from './pages/appointments/AppointmentsPage';
import ProgramsPage from './pages/programs/ProgramsPage';
import ProgramDetailPage from './pages/programs/ProgramDetailPage';
import RegisteredProgramsPage from './pages/programs/RegisteredProgramsPage';

import DashboardPage from './pages/dashboard/DashboardPage';
import ProfilePage from './pages/profile/ProfilePage';

// Admin Pages
import UsersPage from './pages/admin/UsersPage';
import AdminCoursesPage from './pages/admin/CoursesPage';
import AdminSurveysPage from './pages/admin/SurveysPage';
import AdminAppointmentsPage from './pages/admin/AppointmentsPage';
import AdminProgramsPage from './pages/admin/ProgramsPage';
import ProgramRegistrationsPage from './pages/admin/ProgramRegistrationsPage';
import AdminConsultantsPage from './pages/admin/ConsultantsPage';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


// Protected route component for admin routes
const AdminRoute = ({ children }: { children: React.ReactNode }) => {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" />;
  }

  // if (user?.role !== 'admin' && user?.role !== 'manager') {
  //   return <Navigate to="/" />;
  // }

  return <>{children}</>;
};

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <ToastContainer position="top-right" autoClose={3000} />
          <Routes>
            {/* Auth Routes */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />

            {/* Client Routes with ClientLayout */}
            <Route path="/" element={
              <ClientLayout>
                <HomePage />
              </ClientLayout>
            } />

            <Route path="/courses" element={
              <ClientLayout>
                <CoursesPage />
              </ClientLayout>
            } />

            <Route path="/courses/:id" element={
              <ClientLayout>
                <CourseDetailPage />
              </ClientLayout>
            } />

            <Route path="/courses/:id/learn" element={
              <CourseLearningPage />
            } />

            <Route path="/courses/:courseId/lessons/:lessonId/quiz" element={
              <QuizPage />
            } />

            <Route path="/consultants" element={
              <ClientLayout>
                <ConsultantsPage />
              </ClientLayout>
            } />

            <Route path="/consultants/:id" element={
              <ClientLayout>
                <ConsultantDetailPage />
              </ClientLayout>
            } />

            <Route path="/surveys" element={<SurveysPage />} />
            <Route path="/surveys/:id" element={<SurveyDetailPage />} />
            <Route path="/survey-history" element={<SurveyHistoryPage />} />
            <Route path="/all-survey-history" element={<AllSurveyHistoryPage />} />

            <Route path="/appointments" element={
              <ClientLayout>
                <AppointmentsPage />
              </ClientLayout>
            } />

            <Route path="/programs" element={
              <ClientLayout>
                <ProgramsPage />
              </ClientLayout>
            } />

            <Route path="/programs/:id" element={
              <ClientLayout>
                <ProgramDetailPage />
              </ClientLayout>
            } />

            <Route path="/my-registered-programs" element={<RegisteredProgramsPage />} />

            <Route path="/profile" element={
              <ClientLayout>
                <ProfilePage />
              </ClientLayout>
            } />

            {/* Admin Routes with AdminLayout */}
            <Route path="/admin/dashboard" element={
              <AdminRoute>
                <AdminLayout>
                  <DashboardPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/users" element={
              <AdminRoute>
                <AdminLayout>
                  <UsersPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/courses" element={
              <AdminRoute>
                <AdminLayout>
                  <AdminCoursesPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/surveys" element={
              <AdminRoute>
                <AdminLayout>
                  <AdminSurveysPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/appointments" element={
              <AdminRoute>
                <AdminLayout>
                  <AdminAppointmentsPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/programs" element={
              <AdminRoute>
                <AdminLayout>
                  <AdminProgramsPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/programs/:id/registrations" element={
              <AdminRoute>
                <AdminLayout>
                  <ProgramRegistrationsPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/consultants" element={
              <AdminRoute>
                <AdminLayout>
                  <AdminConsultantsPage />
                </AdminLayout>
              </AdminRoute>
            } />

            <Route path="/admin/profile" element={
              <AdminRoute>
                <AdminLayout>
                  <ProfilePage />
                </AdminLayout>
              </AdminRoute>
            } />

            {/* Redirect old dashboard to admin dashboard */}
            <Route path="/dashboard" element={<Navigate to="/admin/dashboard" replace />} />

            {/* Catch all route - redirect to home */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
