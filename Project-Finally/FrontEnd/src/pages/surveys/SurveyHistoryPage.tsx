import React, { useState, useEffect } from 'react';
import {
  Container,
  Typography,
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Alert,
  CircularProgress,
  Chip
} from '@mui/material';
import {
  ArrowBack as ArrowBackIcon,
  History as HistoryIcon
} from '@mui/icons-material';
import { Link } from 'react-router-dom';
import { SurveyMark, Survey } from '../../types/survey';
import { SurveyService } from '../../services/SurveyService';
import { AuthService } from '../../services/AuthService';
import ClientLayout from '../../components/layout/ClientLayout';

const SurveyHistoryPage: React.FC = () => {
  const surveyService = new SurveyService();
  const authService = new AuthService();

  const [markHistory, setMarkHistory] = useState<SurveyMark[]>([]);
  const [surveys, setSurveys] = useState<Survey[]>([]);
  const [selectedSurvey, setSelectedSurvey] = useState<string>('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Load all surveys for filter
  const loadSurveys = async () => {
    try {
      const [code, data] = await surveyService.findAllSurveys({
        page: 1,
        limit: 100
      });
      if (code === 200) {
        setSurveys(data.content);
      }
    } catch (error) {
      console.error('Error loading surveys:', error);
    }
  };

  // Load mark history
  const loadMarkHistory = async (surveyId?: number) => {
    try {
      setLoading(true);
      setError(null);
      
      const authenDTO = await authService.readInfoFromLocal();
      if (authenDTO.userName) {
        const [code, data, message] = await surveyService.getMarkHistory(
          authenDTO.userName, 
          surveyId
        );
        
        if (code === 200) {
          setMarkHistory(data || []);
        } else {
          setError(message || 'Không thể tải lịch sử điểm');
        }
      } else {
        setError('Không tìm thấy thông tin người dùng');
      }
    } catch (error) {
      console.error('Error loading mark history:', error);
      setError('Có lỗi xảy ra khi tải lịch sử điểm');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadSurveys();
    loadMarkHistory();
  }, []);

  const handleSurveyFilterChange = (event: any) => {
    const surveyId = event.target.value;
    setSelectedSurvey(surveyId);
    loadMarkHistory(surveyId ? parseInt(surveyId) : undefined);
  };

  const getSurveyName = (surveyName: string | null) => {
    return surveyName || 'Không xác định';
  };

  if (loading && markHistory.length === 0) {
    return (
      <ClientLayout>
        <Container>
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '400px' }}>
            <CircularProgress />
          </Box>
        </Container>
      </ClientLayout>
    );
  }

  return (
    <ClientLayout>
      <Container maxWidth="lg" sx={{ py: 4 }}>
        {/* Header */}
        <Box sx={{ mb: 4 }}>
          <Button
            component={Link}
            to="/surveys"
            startIcon={<ArrowBackIcon />}
            sx={{ mb: 2 }}
          >
            Quay lại danh sách khảo sát
          </Button>
          <Typography variant="h4" gutterBottom sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <HistoryIcon />
            Điểm số & Lịch sử khảo sát
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Xem lại tất cả điểm số và lịch sử các bài khảo sát của bạn
          </Typography>
        </Box>

        {/* Filter */}
        <Paper sx={{ p: 3, mb: 3 }}>
          <FormControl fullWidth sx={{ maxWidth: 300 }}>
            <InputLabel>Lọc theo khảo sát</InputLabel>
            <Select
              value={selectedSurvey}
              onChange={handleSurveyFilterChange}
              label="Lọc theo khảo sát"
            >
              <MenuItem value="">Tất cả khảo sát</MenuItem>
              {surveys.map(survey => (
                <MenuItem key={survey.id} value={survey.id?.toString()}>
                  {survey.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Paper>

        {/* Error */}
        {error && (
          <Alert severity="error" sx={{ mb: 3 }}>
            {error}
          </Alert>
        )}

        {/* History Table */}
        <Paper sx={{ overflow: 'hidden' }}>
          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>STT</TableCell>
                  <TableCell>Tên khảo sát</TableCell>
                  <TableCell>Điểm số</TableCell>
                  <TableCell>Ngày làm</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {markHistory.length > 0 ? (
                  markHistory.map((mark, index) => (
                    <TableRow key={mark.id} hover>
                      <TableCell>{index + 1}</TableCell>
                      <TableCell>
                        <Typography variant="body1" fontWeight="medium">
                          {getSurveyName(mark.surveyName)}
                        </Typography>
                      </TableCell>
                      <TableCell>
                        <Chip
                          label={`${mark.mark}%`}
                          color={mark.mark >= 70 ? 'success' : mark.mark >= 50 ? 'warning' : 'error'}
                          variant="filled"
                        />
                      </TableCell>
                      <TableCell>
                        {new Date(mark.createDate).toLocaleDateString('vi-VN', {
                          year: 'numeric',
                          month: '2-digit',
                          day: '2-digit',
                          hour: '2-digit',
                          minute: '2-digit'
                        })}
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={4} align="center">
                      <Typography variant="body1" color="text.secondary" sx={{ py: 4 }}>
                        {selectedSurvey ? 'Chưa có lịch sử điểm cho khảo sát này' : 'Chưa có lịch sử điểm nào'}
                      </Typography>
                    </TableCell>
                  </TableRow>
                )}
              </TableBody>
            </Table>
          </TableContainer>
        </Paper>

        {/* Summary */}
        {markHistory.length > 0 && (
          <Paper sx={{ p: 3, mt: 3 }}>
            <Typography variant="h6" gutterBottom>
              Thống kê
            </Typography>
            <Box sx={{ display: 'flex', gap: 4, flexWrap: 'wrap' }}>
              <Box>
                <Typography variant="body2" color="text.secondary">
                  Tổng số lần làm
                </Typography>
                <Typography variant="h5" color="primary">
                  {markHistory.length}
                </Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">
                  Điểm cao nhất
                </Typography>
                <Typography variant="h5" color="success.main">
                  {Math.max(...markHistory.map(m => m.mark))}%
                </Typography>
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">
                  Điểm trung bình
                </Typography>
                <Typography variant="h5" color="info.main">
                  {(markHistory.reduce((sum, m) => sum + m.mark, 0) / markHistory.length).toFixed(1)}%
                </Typography>
              </Box>
            </Box>
          </Paper>
        )}
      </Container>
    </ClientLayout>
  );
};

export default SurveyHistoryPage;
