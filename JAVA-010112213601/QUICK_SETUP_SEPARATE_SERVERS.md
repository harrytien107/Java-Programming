# ⚡ HƯỚNG DẪN NHANH - Tách Frontend và Backend ra 2 Servers

## 📋 TÓM TẮT

- **Server 1 (192.168.1.14)**: MySQL + Backend
- **Server 2 (192.168.1.X)**: Frontend
- **Windows**: Truy cập Frontend trên Server 2 → Frontend gọi API Backend trên Server 1

---

## 🚀 BƯỚC 1: UBUNTU SERVER 1 (Backend)

```bash
# 1. Vào thư mục project
cd ~/JAVA-010112213601

# 2. Dừng containers cũ
docker compose down

# 3. Chạy chỉ Backend và MySQL
docker compose -f docker-compose.backend.yml up -d --build

# 4. Kiểm tra
docker ps
# Phải thấy: mysql và backend

# 5. Test API
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"1234"}'

# 6. Mở firewall
sudo ufw allow 8080/tcp
```

**✅ Server 1 xong!**

---

## 🚀 BƯỚC 2: UBUNTU SERVER 2 (Frontend)

### 2.1. Cài Docker (nếu chưa có)

```bash
sudo apt update && sudo apt install -y docker.io
sudo systemctl start docker && sudo systemctl enable docker
sudo usermod -aG docker $USER
newgrp docker
```

### 2.2. Lấy IP Server 2

```bash
hostname -I
# Ví dụ: 192.168.1.15
# Ghi nhớ IP này!
```

### 2.3. Upload code lên Server 2

**Option A: Clone từ Git**
```bash
cd ~
git clone YOUR_REPO_URL
cd JAVA-010112213601
```

**Option B: Copy từ Server 1**
```bash
# Trên Server 1
cd ~/JAVA-010112213601
scp -r FrontEnd/ docker-compose.frontend.yml user@192.168.1.15:/home/user/JAVA-010112213601/
```

### 2.4. Kiểm tra và sửa docker-compose.frontend.yml

```bash
cd ~/JAVA-010112213601
cat docker-compose.frontend.yml | grep REACT_APP_API_URL

# Phải thấy: REACT_APP_API_URL: http://192.168.1.14:8080
# Nếu sai, sửa lại:
nano docker-compose.frontend.yml
```

### 2.5. Chạy Frontend

```bash
docker compose -f docker-compose.frontend.yml up -d --build

# Đợi 2-3 phút để build

# Xem logs
docker logs -f frontend

# Kiểm tra
docker ps
```

### 2.6. Mở firewall

```bash
sudo ufw allow 3000/tcp
```

**✅ Server 2 xong!**

---

## 🌐 BƯỚC 3: KIỂM TRA TỪ WINDOWS

### 3.1. Ping servers

```powershell
ping 192.168.1.14   # Backend Server
ping 192.168.1.15   # Frontend Server
```

### 3.2. Truy cập Frontend

Mở browser: `http://192.168.1.15:3000`

### 3.3. Đăng nhập

- Username: `admin`
- Password: `1234`

### 3.4. Kiểm tra DevTools (F12)

- Tab **Network** → Filter **Fetch/XHR**
- Thử đăng nhập
- Xem request tới: `http://192.168.1.14:8080/auth/login`
- Status: `200 OK`

**✅ HOÀN THÀNH!** 🎉

---

## 📊 KIẾN TRÚC

```
Windows (192.168.1.x)
    │
    │ Browser → 192.168.1.15:3000
    │
    ▼
Server 2 (Frontend)        Server 1 (Backend + MySQL)
192.168.1.15              192.168.1.14
    │                          │
    │  API calls :8080         │
    └─────────────────────────►│
                                └─► MySQL :3306
```

---

## 🔍 XỬ LÝ SỰ CỐ NHANH

### ❌ Frontend không gọi được Backend

```bash
# Trên Server 2
ping 192.168.1.14
curl http://192.168.1.14:8080/auth/login

# Nếu lỗi → Kiểm tra firewall Server 1
# Trên Server 1:
sudo ufw allow 8080/tcp
docker ps
```

### ❌ Windows không vào được Frontend

```bash
# Trên Server 2
sudo ufw allow 3000/tcp
docker ps | grep frontend
docker logs frontend
```

### ❌ CORS Error

```bash
# Trên Server 1 - Rebuild Backend
docker compose -f docker-compose.backend.yml down
docker compose -f docker-compose.backend.yml up -d --build
```

---

## 📝 FILES QUAN TRỌNG

1. **docker-compose.backend.yml** - Cho Server 1 (Backend + MySQL)
2. **docker-compose.frontend.yml** - Cho Server 2 (Frontend)
3. **SEPARATE_SERVERS_GUIDE.md** - Hướng dẫn chi tiết đầy đủ

---

## 💡 CHECKLIST

### Server 1 (Backend):
- [ ] `docker-compose.backend.yml` đã có
- [ ] Chạy: `docker compose -f docker-compose.backend.yml up -d`
- [ ] MySQL và Backend containers chạy
- [ ] Test login API thành công
- [ ] Firewall mở port 8080

### Server 2 (Frontend):
- [ ] Docker đã cài
- [ ] Code Frontend đã upload
- [ ] `docker-compose.frontend.yml` có đúng Backend IP (192.168.1.14)
- [ ] Chạy: `docker compose -f docker-compose.frontend.yml up -d`
- [ ] Frontend container chạy
- [ ] Firewall mở port 3000

### Windows:
- [ ] Ping được cả 2 servers
- [ ] Truy cập `http://192.168.1.15:3000`
- [ ] Đăng nhập thành công

---

## 🎯 IP ADDRESSES SUMMARY

| Server | IP | Services | Ports |
|--------|---------|----------|-------|
| **Server 1** | 192.168.1.14 | Backend, MySQL | 8080, 3306 |
| **Server 2** | 192.168.1.15 | Frontend | 3000 |
| **Windows** | 192.168.1.x | Browser | - |

---

## 🆘 LỆNH HỮU ÍCH

```bash
# Xem logs
docker logs -f backend   # Server 1
docker logs -f frontend  # Server 2

# Restart
docker compose -f docker-compose.backend.yml restart   # Server 1
docker compose -f docker-compose.frontend.yml restart  # Server 2

# Stop
docker compose -f docker-compose.backend.yml down      # Server 1
docker compose -f docker-compose.frontend.yml down     # Server 2

# Rebuild
docker compose -f docker-compose.backend.yml up -d --build   # Server 1
docker compose -f docker-compose.frontend.yml up -d --build  # Server 2
```

---

**Xem hướng dẫn chi tiết: [SEPARATE_SERVERS_GUIDE.md](SEPARATE_SERVERS_GUIDE.md)**


