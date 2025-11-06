# 🏗️ Kiến trúc 3 Servers: MySQL + Backend + Frontend

## 📊 SO SÁNH KIẾN TRÚC

### 🔄 Kiến trúc hiện tại (2 servers):
```
Ubuntu1 (192.168.1.100)          Ubuntu2 (192.168.1.101)
├── Backend (Spring Boot)        ├── Frontend (React)
└── MySQL                        
```

### 🚀 Kiến trúc mới (3 servers):
```
Ubuntu1 (192.168.1.100)    Ubuntu2 (192.168.1.101)    Ubuntu3 (192.168.1.102)
├── MySQL                   ├── Backend                 ├── Frontend
                           (Spring Boot)               (React)
```

---

## 🔧 THAY ĐỔI CẦN THIẾT

### 1️⃣ **Frontend (Ubuntu3) - KHÔNG ĐỔI**

```yaml
# docker-compose.frontend.yml (Ubuntu3)
services:
  frontend:
    build:
      context: ./FrontEnd
      args:
        REACT_APP_API_URL: http://192.168.1.101:8080  # ← Trỏ tới Backend (Ubuntu2)
```

**Lý do:** Frontend vẫn chỉ cần biết Backend ở đâu, không cần biết MySQL.

### 2️⃣ **Backend (Ubuntu2) - THAY ĐỔI QUAN TRỌNG**

```yaml
# docker-compose.backend.yml (Ubuntu2) - KHÔNG CÓ MYSQL
services:
  backend:
    build: ./BackEnd
    container_name: backend
    environment:
      # ← THAY ĐỔI: Trỏ tới MySQL server riêng
      SPRING_DATASOURCE_URL: jdbc:mysql://192.168.1.100:3306/doanyte?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 123
      DOCKER_ENV: "true"
    ports:
      - "8080:8080"
    volumes:
      - backend_static:/app/static
    restart: unless-stopped

volumes:
  backend_static:
```

**Thay đổi chính:**
- ❌ Xóa service `mysql`
- ❌ Xóa `depends_on: mysql`
- ✅ Đổi `SPRING_DATASOURCE_URL` từ `mysql:3306` → `192.168.1.100:3306`

### 3️⃣ **MySQL (Ubuntu1) - MỚI**

```yaml
# docker-compose.mysql.yml (Ubuntu1) - CHỈ CÓ MYSQL
services:
  mysql:
    image: mysql:8.0.40-debian
    container_name: mysql
    environment:
      MYSQL_ROOT_PASSWORD: 123
      MYSQL_DATABASE: doanyte
    ports:
      - "3306:3306"  # ← QUAN TRỌNG: Expose ra ngoài
    volumes:
      - mysql_data:/var/lib/mysql
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --bind-address=0.0.0.0
    restart: unless-stopped

volumes:
  mysql_data:
```

**Thay đổi chính:**
- ✅ Thêm `--bind-address=0.0.0.0` để cho phép kết nối từ bên ngoài
- ✅ Expose port `3306` ra ngoài

---

## 🔐 CẤU HÌNH MYSQL BẢO MẬT

### Tạo user riêng cho Backend (Khuyến nghị):

```bash
# Trên Ubuntu1 (MySQL server)
docker exec -it mysql mysql -uroot -p123

# Trong MySQL shell:
CREATE USER 'backend_user'@'192.168.1.101' IDENTIFIED BY 'backend_password';
GRANT ALL PRIVILEGES ON doanyte.* TO 'backend_user'@'192.168.1.101';
FLUSH PRIVILEGES;
EXIT;
```

**Cập nhật Backend config:**
```yaml
# Ubuntu2 - docker-compose.backend.yml
environment:
  SPRING_DATASOURCE_URL: jdbc:mysql://192.168.1.100:3306/doanyte
  SPRING_DATASOURCE_USERNAME: backend_user
  SPRING_DATASOURCE_PASSWORD: backend_password
```

---

## 🌐 FIREWALL VÀ NETWORK

### Ubuntu1 (MySQL):
```bash
# Mở port MySQL
sudo ufw allow from 192.168.1.101 to any port 3306
# Hoặc mở cho cả subnet
sudo ufw allow from 192.168.1.0/24 to any port 3306
```

### Ubuntu2 (Backend):
```bash
# Mở port Backend
sudo ufw allow 8080/tcp
```

### Ubuntu3 (Frontend):
```bash
# Mở port Frontend
sudo ufw allow 3000/tcp
```

---

## 📁 FILES CẦN TẠO

### 1. Ubuntu1 (MySQL) - `docker-compose.mysql.yml`:
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
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci --bind-address=0.0.0.0
    restart: unless-stopped

volumes:
  mysql_data:
```

### 2. Ubuntu2 (Backend) - `docker-compose.backend-only.yml`:
```yaml
version: "3.8"
services:
  backend:
    build: ./BackEnd
    container_name: backend
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://192.168.1.100:3306/doanyte?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=utf-8
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 123
      DOCKER_ENV: "true"
    ports:
      - "8080:8080"
    volumes:
      - backend_static:/app/static
    restart: unless-stopped

volumes:
  backend_static:
```

### 3. Ubuntu3 (Frontend) - `docker-compose.frontend.yml`:
```yaml
version: "3.8"
services:
  frontend:
    build:
      context: ./FrontEnd
      args:
        REACT_APP_API_URL: http://192.168.1.101:8080
    container_name: frontend
    ports:
      - "3000:80"
    restart: unless-stopped
```

---

## 🚀 DEPLOYMENT SEQUENCE

### Bước 1: Setup MySQL (Ubuntu1)
```bash
cd ~/JAVA-010112213601
docker compose -f docker-compose.mysql.yml up -d
docker logs -f mysql
```

### Bước 2: Setup Backend (Ubuntu2)
```bash
cd ~/JAVA-010112213601
docker compose -f docker-compose.backend-only.yml up -d --build
docker logs -f backend
```

### Bước 3: Setup Frontend (Ubuntu3)
```bash
cd ~/JAVA-010112213601
docker compose -f docker-compose.frontend.yml up -d --build
docker logs -f frontend
```

---

## 🔍 TESTING

### Test từng layer:

#### 1. Test MySQL (từ Ubuntu2):
```bash
# Từ Ubuntu2
mysql -h 192.168.1.100 -uroot -p123 -e "SHOW DATABASES;"
```

#### 2. Test Backend (từ Ubuntu3):
```bash
# Từ Ubuntu3
curl -X POST http://192.168.1.101:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"1234"}'
```

#### 3. Test Frontend (từ Windows):
```
http://192.168.1.102:3000
```

---

## 📊 KIẾN TRÚC HOÀN CHỈNH

```
┌─────────────────────────────────────────────────────────┐
│                Windows (192.168.1.x)                   │
│                                                          │
│  Browser → http://192.168.1.102:3000                   │
└────────────────────┬────────────────────────────────────┘
                     │
                     │ Network
                     │
        ┌────────────▼─────────────┐
        │                          │
        ▼                          ▼
┌─────────────┐  API calls  ┌─────────────────┐  JDBC  ┌─────────────┐
│  Ubuntu3    │────────────►│    Ubuntu2      │───────►│  Ubuntu1    │
│ Frontend    │             │ Backend         │        │ MySQL       │
│ :3000       │             │ :8080           │        │ :3306       │
│             │             │                 │        │             │
│ React       │             │ Spring Boot     │        │ Database    │
│ Nginx       │             │ API Server      │        │ Storage     │
└─────────────┘             └─────────────────┘        └─────────────┘
192.168.1.102               192.168.1.101              192.168.1.100
```

---

## ✅ LỢI ÍCH KIẾN TRÚC 3 SERVERS

### 🚀 Performance:
- **MySQL**: Dedicated resources, không chia sẻ CPU/RAM
- **Backend**: Tập trung xử lý business logic
- **Frontend**: Chỉ serve static files

### 🔒 Security:
- **Database isolation**: MySQL không expose trực tiếp ra internet
- **Network segmentation**: Có thể firewall riêng từng layer
- **Credential separation**: Mỗi service có user/password riêng

### 📈 Scalability:
- **Horizontal scaling**: Có thể thêm nhiều Backend servers
- **Database scaling**: MySQL có thể setup Master-Slave replication
- **Frontend scaling**: Có thể thêm nhiều Frontend servers + Load Balancer

### 🛠️ Maintenance:
- **Independent updates**: Update Frontend không ảnh hưởng Backend/Database
- **Backup strategy**: Database backup riêng biệt
- **Monitoring**: Monitor từng service riêng biệt

---

## ⚠️ NHƯỢC ĐIỂM

### 🌐 Network Latency:
- Thêm network hops: Frontend → Backend → MySQL
- Cần network ổn định giữa các servers

### 🔧 Complexity:
- Phức tạp hơn trong setup và troubleshooting
- Cần quản lý nhiều servers hơn

### 💰 Cost:
- Cần 3 VMs thay vì 2
- Tốn thêm resources

---

## 🎯 KHI NÀO NÊN DÙNG 3 SERVERS?

### ✅ Nên dùng khi:
- **Production environment** với traffic cao
- **Team lớn** với nhiều developers
- **Cần high availability** và scalability
- **Security requirements** cao
- **Database workload** nặng

### ❌ Không cần khi:
- **Development/Testing** environment
- **Ứng dụng nhỏ** với ít users
- **Limited resources** (ít RAM/CPU)
- **Simple applications** không cần scale

---

## 📝 MIGRATION PLAN

### Từ 2 servers → 3 servers:

#### Bước 1: Backup data
```bash
# Trên Ubuntu1 (hiện tại)
docker exec mysql mysqldump -uroot -p123 doanyte > backup.sql
```

#### Bước 2: Setup MySQL server mới (Ubuntu1)
```bash
# Stop containers cũ
docker compose -f docker-compose.backend.yml down

# Chạy chỉ MySQL
docker compose -f docker-compose.mysql.yml up -d

# Restore data
docker exec -i mysql mysql -uroot -p123 doanyte < backup.sql
```

#### Bước 3: Move Backend sang Ubuntu2
```bash
# Upload code lên Ubuntu2
# Chạy Backend với MySQL connection mới
docker compose -f docker-compose.backend-only.yml up -d --build
```

#### Bước 4: Move Frontend sang Ubuntu3
```bash
# Upload code lên Ubuntu3
# Update API URL trỏ tới Ubuntu2
docker compose -f docker-compose.frontend.yml up -d --build
```

---

## 🔗 IP ADDRESSES SUMMARY

| Server | IP | Services | Ports | Connects To |
|--------|---------|----------|-------|-------------|
| **Ubuntu1** | 192.168.1.100 | MySQL | 3306 | - |
| **Ubuntu2** | 192.168.1.101 | Backend | 8080 | Ubuntu1:3306 |
| **Ubuntu3** | 192.168.1.102 | Frontend | 3000 | Ubuntu2:8080 |
| **Windows** | 192.168.1.x | Browser | - | Ubuntu3:3000 |

---

**Kiến trúc 3 servers là chuẩn production! Bạn có muốn implement không?** 🚀
