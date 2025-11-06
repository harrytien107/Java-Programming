# 🔀 Deployment với Frontend và Backend trên 2 Ubuntu Servers riêng biệt

## 📌 Tổng quan

Tài liệu này hướng dẫn deploy project Java với kiến trúc tách biệt:
- **Ubuntu Server 1**: Backend (Spring Boot) + MySQL
- **Ubuntu Server 2**: Frontend (React)
- **Windows**: Truy cập Frontend → Frontend gọi Backend API

---

## 📚 Tài liệu

### ⚡ Hướng dẫn nhanh (5-10 phút)
👉 **[QUICK_SETUP_SEPARATE_SERVERS.md](QUICK_SETUP_SEPARATE_SERVERS.md)**
- Các bước tối thiểu để setup
- Lệnh copy-paste trực tiếp
- Checklist ngắn gọn

### 📖 Hướng dẫn chi tiết (20-30 phút)
👉 **[SEPARATE_SERVERS_GUIDE.md](SEPARATE_SERVERS_GUIDE.md)**
- Giải thích đầy đủ từng bước
- Xử lý sự cố chi tiết
- Kiến trúc hệ thống
- Scripts tự động

---

## 📁 Files cần dùng

### Cho Ubuntu Server 1 (Backend):
- **docker-compose.backend.yml** - Docker Compose chỉ chạy Backend + MySQL

### Cho Ubuntu Server 2 (Frontend):
- **docker-compose.frontend.yml** - Docker Compose chỉ chạy Frontend
- **FrontEnd/** - Code React app

---

## ⚡ Quick Start

### Server 1 (Backend - IP: 192.168.1.14):
```bash
cd ~/JAVA-010112213601
docker compose down
docker compose -f docker-compose.backend.yml up -d --build
sudo ufw allow 8080/tcp
```

### Server 2 (Frontend - IP: 192.168.1.15):
```bash
cd ~/JAVA-010112213601
# Đảm bảo docker-compose.frontend.yml có Backend IP đúng
docker compose -f docker-compose.frontend.yml up -d --build
sudo ufw allow 3000/tcp
```

### Windows:
```
http://192.168.1.15:3000
Login: admin / 1234
```

---

## 🎯 Kiến trúc

```
┌─────────────────────────────────────────────┐
│            Windows Machine                  │
│   Browser → http://192.168.1.15:3000       │
└────────────────┬────────────────────────────┘
                 │
                 │ VMware Bridged Network
                 │
         ┌───────┴───────┐
         │               │
         ▼               ▼
┌─────────────┐   ┌─────────────────┐
│  Server 2   │   │    Server 1     │
│ Frontend    │   │ Backend + MySQL │
│ :3000       │   │ :8080, :3306    │
│             │   │                 │
│  React      │──►│  Spring Boot    │
│  Nginx      │   │  MySQL          │
└─────────────┘   └─────────────────┘
  192.168.1.15      192.168.1.14
```

---

## ✅ Lợi ích

1. **Tách biệt services** - Frontend và Backend độc lập
2. **Dễ scale** - Có thể thêm nhiều Frontend servers
3. **Deployment riêng** - Update Frontend không ảnh hưởng Backend
4. **Security tốt hơn** - Firewall riêng cho từng service
5. **Giống production** - Kiến trúc thực tế microservices

---

## 🔧 Configuration

### Backend CORS (đã có sẵn)
File: `BackEnd/src/main/java/com/project/codebasespringjpa/configuration/security/Security.java`

```java
private static final String[] ALLOWED_ORIGIN_PATTERNS = {
    "http://192.168.*.*:3000",  // Cho phép mọi IP 192.168.x.x
    "http://10.*.*.*:3000",
    "http://172.16.*.*:3000"
};
```

→ **Không cần sửa gì** nếu Frontend có IP 192.168.x.x

### Frontend API URL
File: `docker-compose.frontend.yml`

```yaml
REACT_APP_API_URL: http://192.168.1.14:8080
```

→ **Phải đúng IP của Backend Server**

---

## 🆘 Troubleshooting

### Frontend không gọi được Backend
```bash
# Test connectivity từ Server 2
ping 192.168.1.14
curl http://192.168.1.14:8080/auth/login

# Mở firewall Server 1
sudo ufw allow 8080/tcp
```

### CORS Error
```bash
# Rebuild Backend trên Server 1
docker compose -f docker-compose.backend.yml up -d --build
```

### Frontend build với sai API URL
```bash
# Rebuild Frontend trên Server 2
docker compose -f docker-compose.frontend.yml down
docker rmi java-010112213601-frontend
docker compose -f docker-compose.frontend.yml up -d --build
```

---

## 📊 IP Addresses

| Component | IP | Port | URL |
|-----------|---------|------|-----|
| Backend API | 192.168.1.14 | 8080 | http://192.168.1.14:8080 |
| Frontend | 192.168.1.15 | 3000 | http://192.168.1.15:3000 |
| MySQL | 192.168.1.14 | 3306 | Internal only |

---

## 🔐 Credentials

- **Admin**: username `admin`, password `1234`
- **MySQL**: root/123, database `doanyte`

---

## 📝 Checklist

### Prerequisites:
- [ ] VMware với 2 VMs Ubuntu Server
- [ ] Cả 2 VMs đều Bridged Network
- [ ] Docker cài trên cả 2 servers
- [ ] Code đã có trên cả 2 servers

### Server 1 Setup:
- [ ] `docker-compose.backend.yml` có sẵn
- [ ] Chạy Backend + MySQL
- [ ] Test login API thành công
- [ ] Firewall mở port 8080

### Server 2 Setup:
- [ ] `docker-compose.frontend.yml` có đúng Backend IP
- [ ] Chạy Frontend
- [ ] Firewall mở port 3000
- [ ] Test từ Server 2 tới Backend thành công

### Final Test:
- [ ] Windows ping được cả 2 servers
- [ ] Truy cập Frontend từ Windows
- [ ] Đăng nhập thành công
- [ ] DevTools thấy API calls tới Backend

---

## 💡 Tips

### Backup trước khi tách
```bash
# Trên Server 1 (trước khi tách)
docker exec mysql mysqldump -uroot -p123 doanyte > backup.sql
```

### Restore sau khi tách
```bash
# Trên Server 1 (sau khi setup Backend)
docker exec -i mysql mysql -uroot -p123 doanyte < backup.sql
```

### Test connectivity giữa 2 servers
```bash
# Từ Server 2
ping 192.168.1.14
telnet 192.168.1.14 8080
curl http://192.168.1.14:8080/auth/login
```

---

## 🚀 Nâng cao

### 1. Multiple Frontend Servers
Có thể tạo thêm Server 3, Server 4... chạy Frontend để load balancing:
```
Server 2: Frontend (192.168.1.15)
Server 3: Frontend (192.168.1.16)
Server 4: Frontend (192.168.1.17)
   ↓
All connect to Backend (192.168.1.14)
```

### 2. Nginx Reverse Proxy
Setup Nginx trên một server riêng:
```
Windows → Nginx Proxy (192.168.1.20)
            ├─→ Frontend (192.168.1.15)
            └─→ Backend (192.168.1.14)
```

### 3. Docker Swarm
Sử dụng Docker Swarm để quản lý cluster:
```bash
# Server 1 (Manager)
docker swarm init

# Server 2 (Worker)
docker swarm join --token ...
```

---

## 🔗 Links

- **Quick Setup**: [QUICK_SETUP_SEPARATE_SERVERS.md](QUICK_SETUP_SEPARATE_SERVERS.md)
- **Detailed Guide**: [SEPARATE_SERVERS_GUIDE.md](SEPARATE_SERVERS_GUIDE.md)
- **Backend Compose**: [docker-compose.backend.yml](docker-compose.backend.yml)
- **Frontend Compose**: [docker-compose.frontend.yml](docker-compose.frontend.yml)

---

## 📞 Support

Nếu gặp vấn đề:
1. Kiểm tra logs: `docker logs [container_name]`
2. Kiểm tra network: `ping`, `curl`, `telnet`
3. Kiểm tra firewall: `sudo ufw status`
4. Đọc [SEPARATE_SERVERS_GUIDE.md](SEPARATE_SERVERS_GUIDE.md) phần Troubleshooting

---

**Happy Deployment! 🎉**


