# 🪟 Hướng Dẫn Truy Cập Từ Windows

## Sau Khi Deploy Xong Trên Ubuntu Server

### 1. Lấy IP Ubuntu Server

Trong Ubuntu Server, chạy lệnh:
```bash
hostname -I
```

Hoặc xem output từ script `deploy.sh`, ví dụ:
```
Server IP: 192.168.1.100
```

---

## 2. Kiểm Tra Kết Nối

### Từ Windows, mở PowerShell hoặc Command Prompt:

```powershell
# Ping Ubuntu Server
ping 192.168.1.100

# Test kết nối port 80 (Frontend)
Test-NetConnection -ComputerName 192.168.1.100 -Port 80

# Test kết nối port 8080 (Backend)
Test-NetConnection -ComputerName 192.168.1.100 -Port 8080
```

**Kết quả mong đợi**: `TcpTestSucceeded : True`

---

## 3. Truy Cập Ứng Dụng

Mở trình duyệt (Chrome, Firefox, Edge) trên Windows:

### 🌐 Frontend (Trang Web Chính)
```
http://192.168.1.100
```

### ⚙️ Backend API
```
http://192.168.1.100:8080
```

### 📚 API Documentation (Swagger)
```
http://192.168.1.100:8080/swagger-ui/index.html
```

---

## 4. Thông Tin Đăng Nhập

### Database (MySQL)
- **Host**: `192.168.1.100`
- **Port**: `3306`
- **Username**: `root`
- **Password**: `123`
- **Database**: `doanyte`

### Ứng Dụng
Thông tin đăng nhập tài khoản được tạo tự động khi khởi động lần đầu (xem logs backend).

---

## ⚠️ Nếu Không Truy Cập Được

### Kiểm Tra 1: Network VMware

Đảm bảo VMware Ubuntu Server dùng **Bridged Network**:
1. Mở VMware
2. Chọn Ubuntu VM → **Edit virtual machine settings**
3. Tab **Network Adapter**
4. Chọn **Bridged: Connected directly to the physical network**

### Kiểm Tra 2: Firewall Ubuntu

Trong Ubuntu Server:
```bash
# Kiểm tra firewall
sudo ufw status

# Cho phép port 80 và 8080
sudo ufw allow 80/tcp
sudo ufw allow 8080/tcp
sudo ufw reload
```

### Kiểm Tra 3: Docker Containers

Trong Ubuntu Server:
```bash
# Xem trạng thái containers
docker ps

# Nên thấy 3 containers đang chạy:
# - mysql-prod
# - backend-prod
# - frontend-prod
```

### Kiểm Tra 4: Test Từ Ubuntu

Trong Ubuntu Server, test localhost:
```bash
# Test frontend
curl http://localhost

# Test backend
curl http://localhost:8080/swagger-ui/index.html
```

Nếu localhost hoạt động nhưng Windows không truy cập được → vấn đề ở network/firewall.

---

## 🔧 Các Lỗi Thường Gặp

### Lỗi: "This site can't be reached"

**Nguyên nhân**: Network không thông hoặc firewall chặn

**Giải pháp**:
1. Ping Ubuntu từ Windows
2. Kiểm tra VMware Network Adapter (Bridged)
3. Tắt firewall Ubuntu tạm thời: `sudo ufw disable`
4. Test lại

### Lỗi: "Connection refused"

**Nguyên nhân**: Containers chưa chạy hoặc chưa ready

**Giải pháp** trong Ubuntu:
```bash
# Xem logs
docker compose -f docker-compose.production.yml logs -f

# Restart containers
docker compose -f docker-compose.production.yml restart

# Kiểm tra lại
docker ps
```

### Lỗi: Frontend hiển thị nhưng không load data

**Nguyên nhân**: Frontend không kết nối được Backend

**Kiểm tra**:
1. Mở Developer Tools trong browser (F12)
2. Tab **Console** xem có lỗi không
3. Tab **Network** xem request có gọi đúng địa chỉ không

**Giải pháp** trong Ubuntu:
```bash
# Kiểm tra biến môi trường
cat .env.production

# Phải có:
REACT_APP_API_URL=http://192.168.1.100:8080

# Nếu sai, build lại frontend
docker compose -f docker-compose.production.yml up -d --build frontend
```

---

## 📱 Truy Cập Từ Thiết Bị Khác

Nếu bạn muốn truy cập từ điện thoại hoặc máy tính khác trong cùng mạng:

1. Đảm bảo thiết bị đó cùng mạng WiFi/LAN với Windows host
2. Truy cập: `http://192.168.1.100`

---

## 🌐 Cấu Hình Domain (Tùy Chọn)

Nếu bạn không muốn nhớ IP, có thể thêm vào file hosts của Windows:

1. Mở Notepad **as Administrator**
2. Mở file: `C:\Windows\System32\drivers\etc\hosts`
3. Thêm dòng:
   ```
   192.168.1.100    drugprevention.local
   ```
4. Save file
5. Truy cập: `http://drugprevention.local`

---

## 📊 Kiểm Tra Hiệu Suất

Mở Developer Tools (F12) → Tab **Network**:
- **Load time**: Thường < 2s cho lần đầu
- **API response time**: < 500ms

Nếu chậm:
- Kiểm tra network Ubuntu Server
- Kiểm tra CPU/RAM Ubuntu: `htop` hoặc `docker stats`

---

## 💡 Tips

1. **Bookmark các URL** để truy cập nhanh
2. **Cài đặt làm shortcut** trên desktop Windows
3. **Dùng Docker logs** để debug nếu có lỗi
4. **Backup data** định kỳ (MySQL database)

---

**Chúc bạn sử dụng thành công! 🎉**

Nếu gặp vấn đề không giải quyết được, xem file `UBUNTU_DEPLOYMENT.md` để biết thêm chi tiết!

