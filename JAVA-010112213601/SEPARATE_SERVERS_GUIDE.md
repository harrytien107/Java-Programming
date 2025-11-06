# 🔀 Hướng dẫn tách Frontend và Backend ra 2 Ubuntu Server

## 🎯 MỤC TIÊU

**Hiện tại:**
```
Ubuntu Server 1 (192.168.1.14)
├── MySQL
├── Backend
└── Frontend
```

**Mục tiêu:**
```
Ubuntu Server 1 (192.168.1.14)          Ubuntu Server 2 (192.168.1.X)
├── MySQL                                ├── Frontend
└── Backend                              
```

**Kết quả:** 
- Windows có thể truy cập Frontend trên Server 2
- Frontend trên Server 2 gọi API Backend trên Server 1
- Backend trên Server 1 kết nối MySQL cục bộ

---

## 📋 YÊU CẦU

### Ubuntu Server 1 (Backend + MySQL):
- IP hiện tại: `192.168.1.14`
- Services: MySQL (port 3306), Backend (port 8080)
- VMware Network: **Bridged mode**

### Ubuntu Server 2 (Frontend):
- IP: Chưa biết (ví dụ: `192.168.1.15`)
- Services: Frontend (port 3000)
- VMware Network: **Bridged mode**
- Cần cài: Docker, Docker Compose

### Windows:
- Kết nối mạng LAN với cả 2 Ubuntu Servers

---

## 🚀 BƯỚC 1: CHUẨN BỊ UBUNTU SERVER 2 (Frontend)

### 1.1. Cài Ubuntu Server 2 trên VMware

1. Tạo VM mới trong VMware
2. Cài Ubuntu Server (20.04 hoặc 22.04)
3. **Quan trọng:** Chọn **Bridged Network**
4. Boot và login

### 1.2. Lấy IP của Ubuntu Server 2

```bash
hostname -I
# Ví dụ output: 192.168.1.15
# Ghi nhớ IP này!
```

### 1.3. Cài Docker và Docker Compose trên Server 2

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Cài Docker
sudo apt install -y docker.io

# Start và enable Docker
sudo systemctl start docker
sudo systemctl enable docker

# Add user vào docker group
sudo usermod -aG docker $USER
newgrp docker

# Kiểm tra Docker
docker --version

# Kiểm tra Docker Compose (V2 plugin)
docker compose version
```

---

## 🚀 BƯỚC 2: CẤU HÌNH UBUNTU SERVER 1 (Backend)

### 2.1. Tạo docker-compose riêng cho Backend

Trên **Ubuntu Server 1**, tạo file mới:

```bash
cd ~/JAVA-010112213601
nano docker-compose.backend.yml
```

Nội dung:

```yaml
version: "3.8"
services:
  mysql:
    image: mysql:8.0.40-debian
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: 123
      MYSQL_DATABASE: doanyte
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-p123"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - backend_network

  backend:
    build: ./BackEnd
    container_name: backend
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/doanyte?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 123
      DOCKER_ENV: "true"
    ports:
      - "8080:8080"
    volumes:
      - backend_static:/app/static
    restart: unless-stopped
    networks:
      - backend_network

volumes:
  mysql_data:
  backend_static:

networks:
  backend_network:
    driver: bridge
```

### 2.2. Dừng containers hiện tại và chạy lại Backend

```bash
# Dừng tất cả containers cũ
docker compose down

# Chạy chỉ Backend và MySQL
docker compose -f docker-compose.backend.yml up -d --build

# Kiểm tra
docker ps
# Phải thấy: mysql và backend đang chạy
```

### 2.3. Cấu hình CORS Backend cho phép Frontend từ Server 2

**QUAN TRỌNG:** Backend phải cho phép CORS từ IP của Server 2.

Kiểm tra file `BackEnd/src/main/java/com/project/codebasespringjpa/configuration/security/Security.java`:

Đảm bảo có:
```java
private static final String[] ALLOWED_ORIGINS = {
    "http://localhost:3000",
    "http://127.0.0.1:3000",
    "http://192.168.1.14:3000",  // Server 1 (nếu cần)
    "http://192.168.1.15:3000"   // Server 2 - THÊM DÒNG NÀY
};
```

**Hoặc** dùng pattern (đã có sẵn):
```java
private static final String[] ALLOWED_ORIGIN_PATTERNS = {
    "http://192.168.*.*:3000",  // Cho phép tất cả IP 192.168.x.x
    "http://10.*.*.*:3000",
    "http://172.16.*.*:3000"
};
```

Nếu đã có pattern `192.168.*.*:3000` thì **KHÔNG CẦN SỬA GÌ**!

### 2.4. Mở Firewall trên Server 1

```bash
# Mở port 8080 cho Backend API
sudo ufw allow 8080/tcp

# Kiểm tra
sudo ufw status
```

### 2.5. Test Backend từ Server 1

```bash
curl -X POST http://localhost:8080/auth/login \ -H "Content-Type: application/json" \ -d '{"username":"admin","password":"1234"}'

# Phải thấy: {"code":200,"data":{...}}
```

---

## 🚀 BƯỚC 3: UPLOAD VÀ CẤU HÌNH FRONTEND TRÊN SERVER 2

### 3.1. Upload code Frontend lên Server 2

**Option A: Dùng Git**
```bash
# Trên Server 2
cd ~
git clone YOUR_REPO_URL
cd JAVA-010112213601
```

**Option B: Dùng SCP từ Windows**
```powershell
# Trên Windows PowerShell
scp -r D:\CODE\Java\JAVA-010112213601\FrontEnd user@192.168.1.15:/home/user/
```

**Option C: Dùng SCP từ Server 1 sang Server 2**
```bash
# Trên Server 1
cd ~/JAVA-010112213601
scp -r FrontEnd/ user@192.168.1.15:/home/user/JAVA-010112213601/
```

### 3.2. Tạo docker-compose cho Frontend trên Server 2

Trên **Ubuntu Server 2**, tạo file:

```bash
cd ~/JAVA-010112213601
nano docker-compose.frontend.yml
```

Nội dung:

```yaml
version: "3.8"
services:
  frontend:
    build:
      context: ./FrontEnd
      args:
        # IP của Backend trên Server 1
        REACT_APP_API_URL: http://192.168.1.14:8080
    container_name: frontend
    ports:
      - "3000:80"
    restart: unless-stopped
```

### 3.3. Build và chạy Frontend trên Server 2

```bash
cd ~/JAVA-010112213601

# Build và chạy Frontend
docker compose -f docker-compose.frontend.yml up -d --build

# Xem logs
docker logs -f frontend

# Kiểm tra container
docker ps
```

### 3.4. Mở Firewall trên Server 2

```bash
# Mở port 3000 cho Frontend
sudo ufw allow 3000/tcp

# Kiểm tra
sudo ufw status
```

---

## 🚀 BƯỚC 4: KIỂM TRA KẾT NỐI

### 4.1. Test từ Server 2 tới Backend trên Server 1

```bash
# Trên Server 2
curl -X POST http://192.168.1.101:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"1234"}'

# Phải thấy: {"code":200,"data":{...}}
```

Nếu bị lỗi "Connection refused":
- Kiểm tra Backend trên Server 1: `docker ps`
- Kiểm tra firewall Server 1: `sudo ufw status`
- Ping từ Server 2 tới Server 1: `ping 192.168.1.14`

### 4.2. Test Frontend từ Server 2

```bash
# Trên Server 2
curl http://localhost:3000

# Phải thấy HTML content
```

### 4.3. Test từ Windows

**Ping cả 2 servers:**
```powershell
# Trên Windows PowerShell
ping 192.168.1.14   # Backend Server
ping 192.168.1.15   # Frontend Server
```

**Test kết nối:**
```powershell
# Test Backend
Invoke-RestMethod -Method Post -Uri "http://192.168.1.100:8080/auth/login" `
  -ContentType "application/json" `
  -Body '{"username":"admin","password":"1234"}'

# Test Frontend
Invoke-WebRequest -Uri "http://192.168.1.15:3000" | Select-Object StatusCode
```

---

## 🌐 BƯỚC 5: TRUY CẬP TỪ WINDOWS

### 5.1. Mở Browser trên Windows

Truy cập Frontend trên Server 2:
```
http://192.168.1.15:3000
```

### 5.2. Đăng nhập

- Username: `admin`
- Password: `1234`

### 5.3. Kiểm tra Network trong DevTools (F12)

1. Mở DevTools (F12)
2. Tab **Network**
3. Filter: **Fetch/XHR**
4. Thử đăng nhập
5. Xem request tới Backend:
   - URL: `http://192.168.1.14:8080/auth/login`
   - Method: POST
   - Status: 200 OK

**Thành công!** ✅

---

## 📊 KIẾN TRÚC SAU KHI TÁCH

```
┌─────────────────────────────────────────────────────────────┐
│                    Windows Machine                          │
│                                                              │
│  Browser → http://192.168.1.15:3000 (Frontend Server 2)    │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       │ VMware Bridged Network
                       │
       ┌───────────────┴────────────────┐
       │                                │
       ▼                                ▼
┌──────────────────┐          ┌──────────────────┐
│ Ubuntu Server 2  │          │ Ubuntu Server 1  │
│ 192.168.1.15     │          │ 192.168.1.14     │
│                  │          │                  │
│  ┌────────────┐  │          │  ┌────────────┐ │
│  │  Frontend  │  │   API    │  │  Backend   │ │
│  │   React    │──┼─────────►│  │ Spring Boot│ │
│  │   Nginx    │  │  :8080   │  │   :8080    │ │
│  │   :3000    │  │          │  └─────┬──────┘ │
│  └────────────┘  │          │        │        │
│                  │          │        │ JDBC   │
└──────────────────┘          │  ┌─────▼──────┐ │
                               │  │   MySQL    │ │
                               │  │   :3306    │ │
                               │  └────────────┘ │
                               └──────────────────┘
```

---

## 🔍 XỬ LÝ SỰ CỐ

### ❌ Frontend không gọi được Backend

**Kiểm tra:**
```bash
# Trên Server 2
curl http://192.168.1.14:8080/auth/login

# Nếu lỗi "Connection refused"
ping 192.168.1.14
telnet 192.168.1.14 8080
```

**Giải pháp:**
1. Kiểm tra Backend đang chạy trên Server 1: `docker ps`
2. Mở firewall trên Server 1: `sudo ufw allow 8080/tcp`
3. Kiểm tra VMware Network: cả 2 servers phải ở Bridged mode

### ❌ CORS Error

**Triệu chứng:**
```
Access to fetch at 'http://192.168.1.14:8080/auth/login' from origin 
'http://192.168.1.15:3000' has been blocked by CORS policy
```

**Giải pháp:**
1. Thêm IP Server 2 vào Backend CORS config
2. Rebuild Backend trên Server 1:
   ```bash
   # Trên Server 1
   docker compose -f docker-compose.backend.yml down
   docker compose -f docker-compose.backend.yml up -d --build
   ```

### ❌ Frontend build với sai API URL

**Triệu chứng:** Frontend gọi API về `localhost` thay vì `192.168.1.14`

**Giải pháp:**
```bash
# Trên Server 2
docker compose -f docker-compose.frontend.yml down
docker rmi java-010112213601-frontend
docker compose -f docker-compose.frontend.yml up -d --build
```

### ❌ Windows không truy cập được Server 2

**Kiểm tra:**
```powershell
# Trên Windows
ping 192.168.1.15
```

**Giải pháp:**
1. Kiểm tra VMware Network của Server 2: phải là Bridged
2. Mở firewall trên Server 2: `sudo ufw allow 3000/tcp`
3. Kiểm tra cả 2 servers cùng subnet: 192.168.1.x

---

## 🛠️ SCRIPTS TỰ ĐỘNG

### Script cho Server 1 (Backend)

Tạo file `start-backend.sh` trên Server 1:

```bash
#!/bin/bash
cd ~/JAVA-010112213601
docker compose -f docker-compose.backend.yml up -d --build
docker ps
echo "Backend started on 192.168.1.14:8080"
```

### Script cho Server 2 (Frontend)

Tạo file `start-frontend.sh` trên Server 2:

```bash
#!/bin/bash
cd ~/JAVA-010112213601
docker compose -f docker-compose.frontend.yml up -d --build
docker ps
UBUNTU_IP=$(hostname -I | awk '{print $1}')
echo "Frontend started on $UBUNTU_IP:3000"
```

---

## 📝 CHECKLIST HOÀN CHỈNH

### Server 1 (Backend):
- [ ] Docker và Docker Compose đã cài
- [ ] `docker-compose.backend.yml` đã tạo
- [ ] Backend CORS cho phép IP Server 2
- [ ] MySQL và Backend containers đang chạy
- [ ] Firewall mở port 8080
- [ ] Test login API thành công

### Server 2 (Frontend):
- [ ] Docker và Docker Compose đã cài
- [ ] Code Frontend đã upload
- [ ] `docker-compose.frontend.yml` đã tạo với đúng Backend IP
- [ ] Frontend container đang chạy
- [ ] Firewall mở port 3000
- [ ] Test từ Server 2 tới Backend thành công

### Windows:
- [ ] Ping được cả 2 servers
- [ ] Truy cập được `http://192.168.1.15:3000`
- [ ] Đăng nhập thành công
- [ ] Frontend gọi Backend API thành công

---

## 💡 LỢI ÍCH CỦA KIẾN TRÚC NÀY

1. **Tách biệt services** - Dễ scale và maintain
2. **Độc lập deployment** - Update Frontend không ảnh hưởng Backend
3. **Tăng security** - Có thể firewall riêng cho từng server
4. **Chuẩn bị cho production** - Giống kiến trúc thực tế
5. **Load balancing** - Có thể thêm nhiều Frontend servers

---

## 🚀 NÂNG CAO (Tùy chọn)

### 1. Sử dụng Nginx Reverse Proxy

Thêm Nginx trên Server 1 để:
- Frontend gọi API qua domain thay vì IP
- Thêm SSL/HTTPS
- Load balancing

### 2. Sử dụng Docker Network giữa 2 servers

Dùng Docker Swarm hoặc Overlay network để containers giao tiếp trực tiếp.

### 3. Setup DNS

Thay vì dùng IP, dùng domain name:
- `backend.local` → 192.168.1.14
- `frontend.local` → 192.168.1.15

---

## 🆘 CẦN GIÚP ĐỠ?

Nếu gặp vấn đề:

1. **Xem logs:**
   ```bash
   # Server 1
   docker logs backend
   
   # Server 2
   docker logs frontend
   ```

2. **Test connectivity:**
   ```bash
   # Từ Server 2
   ping 192.168.1.14
   curl http://192.168.1.14:8080/auth/login
   ```

3. **Kiểm tra firewall:**
   ```bash
   sudo ufw status
   sudo netstat -tulpn | grep -E '3000|8080'
   ```

---

**Chúc bạn thành công với kiến trúc mới! 🎉**


