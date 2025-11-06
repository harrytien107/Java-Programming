# 🚀 Hướng dẫn Deploy Project Java trên Ubuntu Server VMware

## 📌 Mục tiêu
- Chạy Backend (Spring Boot) và Frontend (React) bằng Docker trên Ubuntu Server VMware
- Truy cập web từ máy Windows thông qua IP của Ubuntu Server

---

## ✅ Yêu cầu hệ thống

### Trên Ubuntu Server VMware:
- Ubuntu Server 20.04+ (hoặc tương tự)
- Docker và Docker Compose đã được cài đặt
- Cổng 3000 (Frontend), 8080 (Backend), 3306 (MySQL) không bị chặn
- Network mode: **Bridged** hoặc **NAT** với port forwarding

### Trên máy Windows:
- Kết nối mạng LAN với Ubuntu Server (hoặc qua VMware NAT)

---

## 📝 Các bước thực hiện

### **Bước 1: Kiểm tra IP của Ubuntu Server**

Trên Ubuntu Server, chạy lệnh:

```bash
ip addr show
# Hoặc
hostname -I
```

**Ví dụ output:**
```
192.168.1.100  # <- Đây là IP bạn cần
```

Ghi nhớ IP này (ví dụ: `192.168.1.100`).

---

### **Bước 2: Cấu hình VMware Network**

#### Option A: Bridged Mode (Khuyến nghị)
1. Mở VMware → Chọn VM Ubuntu → Settings → Network Adapter
2. Chọn **Bridged** mode
3. Restart VM Ubuntu
4. IP của Ubuntu sẽ nằm trong cùng dải mạng với Windows (ví dụ: 192.168.1.x)

#### Option B: NAT Mode (với Port Forwarding)
1. VMware → Edit → Virtual Network Editor
2. Chọn VMnet8 (NAT) → NAT Settings
3. Thêm port forwarding:
   - Port 3000 (Frontend): Host 3000 → VM 3000
   - Port 8080 (Backend): Host 8080 → VM 8080
   - Port 3306 (MySQL): Host 3306 → VM 3306
4. Truy cập qua `localhost` trên Windows

---

### **Bước 3: Cài đặt Docker và Docker Compose trên Ubuntu**

Nếu chưa cài đặt Docker, chạy các lệnh sau:

```bash
# Cập nhật hệ thống
sudo apt update && sudo apt upgrade -y

# Cài đặt Docker
sudo apt install -y docker.io

# Khởi động Docker và cho phép tự động chạy khi boot
sudo systemctl start docker
sudo systemctl enable docker

# Thêm user hiện tại vào group docker (để không cần sudo)
sudo usermod -aG docker $USER

# Đăng xuất và đăng nhập lại để áp dụng group mới
# Hoặc chạy: newgrp docker

# Kiểm tra Docker
docker --version

# Cài đặt Docker Compose (nếu chưa có)
sudo apt install -y docker-compose

# Kiểm tra Docker Compose
docker-compose --version
```

---

### **Bước 4: Chuyển code lên Ubuntu Server**

#### Option A: Sử dụng Git (Khuyến nghị)
```bash
# Clone repository về Ubuntu
cd ~
git clone YOUR_REPOSITORY_URL
cd JAVA-010112213601
```

#### Option B: Sử dụng WinSCP hoặc FileZilla
1. Kết nối từ Windows tới Ubuntu qua SFTP
2. Upload toàn bộ folder project lên `/home/your_username/JAVA-010112213601`

#### Option C: Sử dụng SCP từ Windows PowerShell
```powershell
# Từ Windows PowerShell
scp -r D:\CODE\Java\JAVA-010112213601 username@192.168.1.100:/home/username/
```

---

### **Bước 5: Cấu hình docker-compose.yml**

**⚠️ QUAN TRỌNG:** Mở file `docker-compose.yml` và thay `YOUR_UBUNTU_IP` bằng IP thực tế của Ubuntu Server.

```bash
cd ~/JAVA-010112213601
nano docker-compose.yml
```

Tìm dòng:
```yaml
REACT_APP_API_URL: http://YOUR_UBUNTU_IP:8080
```

Thay thế bằng:
```yaml
REACT_APP_API_URL: http://192.168.1.100:8080
```
*(Thay 192.168.1.100 bằng IP thực tế của bạn)*

Lưu file: `Ctrl + O` → `Enter` → `Ctrl + X`

---

### **Bước 6: Chạy Docker Compose**

```bash
# Di chuyển vào thư mục project
cd ~/JAVA-010112213601

# Build và chạy tất cả các services
docker-compose up -d --build

# Kiểm tra trạng thái containers
docker-compose ps

# Xem logs nếu có lỗi
docker-compose logs -f
```

**Giải thích các lệnh:**
- `docker-compose up -d`: Chạy containers ở chế độ background (detached mode)
- `--build`: Build lại images từ Dockerfile
- `docker-compose ps`: Liệt kê các containers đang chạy
- `docker-compose logs -f`: Xem logs real-time (nhấn Ctrl+C để thoát)

---

### **Bước 7: Kiểm tra services đang chạy**

```bash
# Kiểm tra tất cả containers
docker ps

# Kết quả mong đợi (3 containers):
# - mysql (port 3306)
# - backend (port 8080)
# - frontend (port 3000)
```

---

### **Bước 8: Mở Firewall trên Ubuntu (nếu cần)**

```bash
# Kiểm tra firewall status
sudo ufw status

# Nếu firewall đang bật, cho phép các ports
sudo ufw allow 3000/tcp   # Frontend
sudo ufw allow 8080/tcp   # Backend
sudo ufw allow 3306/tcp   # MySQL (không bắt buộc nếu chỉ dùng internal)

# Reload firewall
sudo ufw reload
```

---

### **Bước 9: Truy cập từ máy Windows**

Mở trình duyệt trên Windows và truy cập:

#### Frontend (React):
```
http://192.168.1.100:3000
```

#### Backend API (Swagger UI):
```
http://192.168.1.100:8080/swagger-ui/index.html
```

#### Backend API Health Check:
```
http://192.168.1.100:8080/api/actuator/health
```
*(Nếu có Spring Actuator)*

**Thay `192.168.1.100` bằng IP thực tế của Ubuntu Server của bạn.**

---

## 🔧 Các lệnh Docker hữu ích

### Quản lý containers:
```bash
# Xem logs của một service cụ thể
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# Dừng tất cả containers
docker-compose down

# Dừng và xóa volumes (xóa database)
docker-compose down -v

# Restart một service cụ thể
docker-compose restart backend

# Rebuild một service cụ thể
docker-compose up -d --build backend
```

### Kiểm tra thông tin:
```bash
# Vào bên trong container
docker exec -it backend bash
docker exec -it frontend sh
docker exec -it mysql bash

# Kiểm tra MySQL
docker exec -it mysql mysql -uroot -p123 -e "SHOW DATABASES;"
```

### Dọn dẹp hệ thống:
```bash
# Xóa tất cả containers đã dừng
docker container prune -f

# Xóa tất cả images không sử dụng
docker image prune -a -f

# Xóa tất cả volumes không sử dụng
docker volume prune -f

# Xóa tất cả mọi thứ (CẢNH BÁO: Sẽ mất data!)
docker system prune -a --volumes -f
```

---

## ⚠️ Xử lý sự cố

### ❌ Vấn đề 1: Không kết nối được từ Windows

**Kiểm tra:**
```bash
# Trên Ubuntu, kiểm tra ports đang listening
sudo netstat -tulpn | grep -E '3000|8080'

# Ping từ Windows tới Ubuntu
ping 192.168.1.100

# Kiểm tra firewall
sudo ufw status
```

**Giải pháp:**
- Đảm bảo VMware Network ở chế độ Bridged
- Tắt firewall tạm thời để test: `sudo ufw disable`
- Kiểm tra Windows Firewall

---

### ❌ Vấn đề 2: Backend không kết nối được MySQL

**Kiểm tra:**
```bash
# Xem logs của backend
docker-compose logs backend

# Kiểm tra MySQL có chạy không
docker-compose ps mysql
```

**Giải pháp:**
- Đảm bảo MySQL container đã healthy trước khi backend start
- Healthcheck trong docker-compose.yml đã được cấu hình đúng

---

### ❌ Vấn đề 3: Frontend không gọi được API

**Nguyên nhân:** `REACT_APP_API_URL` không đúng hoặc CORS issue.

**Giải pháp:**
```bash
# 1. Kiểm tra environment trong container frontend
docker exec -it frontend env | grep REACT_APP_API_URL

# 2. Nếu sai, rebuild lại frontend với đúng API URL
nano docker-compose.yml  # Sửa REACT_APP_API_URL
docker-compose up -d --build frontend

# 3. Kiểm tra CORS trong Backend (Java Spring Boot)
# Đảm bảo @CrossOrigin hoặc WebMvcConfigurer cho phép origin từ frontend
```

---

### ❌ Vấn đề 4: Build frontend bị lỗi

**Kiểm tra:**
```bash
docker-compose logs frontend
```

**Giải pháp:**
- Kiểm tra `package.json` có đúng không
- Build thử local trước: `cd FrontEnd && npm install && npm run build`
- Tăng memory cho Docker nếu bị OOM

---

### ❌ Vấn đề 5: Container bị restart liên tục

**Kiểm tra:**
```bash
docker-compose ps
docker-compose logs -f [service_name]
```

**Giải pháp:**
- Xem logs để biết nguyên nhân
- Thường do application.properties sai hoặc MySQL chưa ready

---

## 📊 Kiến trúc hệ thống

```
┌─────────────────────────────────────────────────────────┐
│                    Máy Windows                          │
│  Browser: http://192.168.1.100:3000                    │
└─────────────────┬───────────────────────────────────────┘
                  │ Network (LAN/VMware Bridged)
                  │
┌─────────────────▼───────────────────────────────────────┐
│              Ubuntu Server VMware                       │
│                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │  Frontend    │  │   Backend    │  │    MySQL     │ │
│  │   (Nginx)    │  │ (Spring Boot)│  │   (8.0.40)   │ │
│  │  Port: 3000  │  │  Port: 8080  │  │  Port: 3306  │ │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘ │
│         │                  │                  │          │
│         │    API Calls     │   JDBC           │          │
│         └─────────────────►│─────────────────►│          │
│                            │                  │          │
│         Docker Network: bridge                │          │
└──────────────────────────────────────────────────────────┘
```

---

## 🎯 Checklist hoàn thành

- [ ] Docker và Docker Compose đã cài đặt trên Ubuntu
- [ ] VMware Network đã cấu hình (Bridged hoặc NAT)
- [ ] Đã lấy được IP của Ubuntu Server
- [ ] Đã sửa `docker-compose.yml` với đúng IP
- [ ] Đã upload code lên Ubuntu Server
- [ ] Chạy `docker-compose up -d --build` thành công
- [ ] `docker-compose ps` hiển thị 3 containers đang chạy
- [ ] Firewall đã được mở (nếu có)
- [ ] Truy cập được `http://IP:3000` từ Windows
- [ ] Truy cập được `http://IP:8080` từ Windows

---

## 📚 Tài liệu tham khảo

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker)
- [React Environment Variables](https://create-react-app.dev/docs/adding-custom-environment-variables/)

---

## 💡 Tips

1. **Sử dụng Bridged Mode** thay vì NAT để dễ dàng truy cập
2. **Backup database** trước khi chạy `docker-compose down -v`
3. **Xem logs thường xuyên** để phát hiện lỗi sớm
4. **Sử dụng `.env` file** cho các biến môi trường nhạy cảm
5. **Cân nhắc dùng Docker Swarm hoặc Kubernetes** cho production

---

## 🆘 Hỗ trợ

Nếu gặp vấn đề, hãy cung cấp:
1. Output của `docker-compose ps`
2. Output của `docker-compose logs`
3. IP của Ubuntu Server
4. Cấu hình VMware Network

---

**Chúc bạn deploy thành công! 🎉**

