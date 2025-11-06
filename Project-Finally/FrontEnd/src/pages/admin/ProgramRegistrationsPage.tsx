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
  Avatar,
  Chip,
  Button,
  TextField,
  InputAdornment,
  CircularProgress
} from '@mui/material';
import {
  Search as SearchIcon,
  ArrowBack as ArrowBackIcon,
  Email as EmailIcon,
  Phone as PhoneIcon
} from '@mui/icons-material';
import { useParams, Link } from 'react-router-dom';
import { ProgramService } from '../../services/ProgramService';
import { toast } from 'react-toastify';

interface RegisteredUser {
  id: number;
  username: string;
  fullname: string;
  email: string;
  avatar: string;
  position: string;
  phone: string;
  majors: string[];
  role: string;
  createDate: string;
}

const ProgramRegistrationsPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [users, setUsers] = useState<RegisteredUser[]>([]);
  const [filteredUsers, setFilteredUsers] = useState<RegisteredUser[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [programTitle, setProgramTitle] = useState('');

  const programService = new ProgramService();

  useEffect(() => {
    const loadRegisteredUsers = async () => {
      if (!id) return;

      try {
        setLoading(true);
        
        // Load program details first
        const [programCode, programData] = await programService.findById(parseInt(id));
        if (programCode === 200 && programData) {
          setProgramTitle(programData.title);
        }

        // Load registered users
        const [code, data, message] = await programService.getRegisteredUsers(parseInt(id));
        if (code === 200) {
          setUsers(data || []);
          setFilteredUsers(data || []);
        } else {
          toast.error(message || 'Lỗi khi tải danh sách người đăng ký');
        }
      } catch (error) {
        console.error('Error loading registered users:', error);
        toast.error('Lỗi kết nối khi tải danh sách người đăng ký');
      } finally {
        setLoading(false);
      }
    };

    loadRegisteredUsers();
  }, [id]);

  useEffect(() => {
    if (searchTerm) {
      const filtered = users.filter(user =>
        user.fullname.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.username.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.phone.includes(searchTerm)
      );
      setFilteredUsers(filtered);
    } else {
      setFilteredUsers(users);
    }
  }, [searchTerm, users]);

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const getAvatarUrl = (avatar: string) => {
    if (!avatar || avatar === 'default_no_image.png') {
      return `http://localhost:8080/default_no_image.png`;
    }
    if (avatar.startsWith('http')) {
      return avatar;
    }
    return `http://localhost:8080/${avatar}`;
  };

  const getRoleDisplay = (role: string) => {
    switch (role) {
      case 'ADMIN':
        return 'Quản trị viên';
      case 'SPECIALIST':
        return 'Chuyên viên';
      case 'USER':
        return 'Người dùng';
      default:
        return role;
    }
  };

  const getRoleColor = (role: string) => {
    switch (role) {
      case 'ADMIN':
        return 'error';
      case 'SPECIALIST':
        return 'warning';
      case 'USER':
        return 'primary';
      default:
        return 'default';
    }
  };

  if (loading) {
    return (
      <Container>
        <Box sx={{ py: 4, textAlign: 'center' }}>
          <CircularProgress />
          <Typography sx={{ mt: 2 }}>Đang tải danh sách người đăng ký...</Typography>
        </Box>
      </Container>
    );
  }

  return (
    <Container>
      <Box sx={{ mb: 4 }}>
        <Button
          component={Link}
          to="/admin/programs"
          startIcon={<ArrowBackIcon />}
          sx={{ mb: 2 }}
        >
          Quay lại danh sách chương trình
        </Button>
        
        <Typography variant="h4" component="h1" gutterBottom>
          Danh sách người đăng ký
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Chương trình: {programTitle}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Tổng số người đăng ký: {users.length}
        </Typography>
      </Box>

      {/* Search */}
      <Paper sx={{ p: 2, mb: 3 }}>
        <TextField
          label="Tìm kiếm người đăng ký"
          variant="outlined"
          size="small"
          value={searchTerm}
          onChange={handleSearchChange}
          sx={{ width: '100%', maxWidth: '400px' }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
        />
      </Paper>

      {/* Users Table */}
      <Paper sx={{ width: '100%', overflow: 'hidden' }}>
        <TableContainer>
          <Table stickyHeader>
            <TableHead>
              <TableRow>
                <TableCell>STT</TableCell>
                <TableCell>Người dùng</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Số điện thoại</TableCell>
                <TableCell>Chức vụ</TableCell>
                <TableCell>Vai trò</TableCell>
                <TableCell>Ngày đăng ký</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {filteredUsers.length > 0 ? (
                filteredUsers.map((user, index) => (
                  <TableRow hover key={user.id}>
                    <TableCell>{index + 1}</TableCell>
                    <TableCell>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                        <Avatar
                          src={getAvatarUrl(user.avatar)}
                          alt={user.fullname}
                          sx={{ width: 40, height: 40 }}
                        />
                        <Box>
                          <Typography variant="body1" fontWeight="medium">
                            {user.fullname}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            @{user.username}
                          </Typography>
                        </Box>
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <EmailIcon fontSize="small" color="action" />
                        {user.email}
                      </Box>
                    </TableCell>
                    <TableCell>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <PhoneIcon fontSize="small" color="action" />
                        {user.phone}
                      </Box>
                    </TableCell>
                    <TableCell>{user.position}</TableCell>
                    <TableCell>
                      <Chip
                        label={getRoleDisplay(user.role)}
                        color={getRoleColor(user.role) as any}
                        size="small"
                      />
                    </TableCell>
                    <TableCell>
                      {new Date(user.createDate).toLocaleDateString('vi-VN')}
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={7} align="center">
                    <Typography variant="body2" color="text.secondary">
                      {searchTerm ? 'Không tìm thấy người dùng nào phù hợp.' : 'Chưa có người nào đăng ký chương trình này.'}
                    </Typography>
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Paper>
    </Container>
  );
};

export default ProgramRegistrationsPage;
