# 🚀 Hướng Dẫn Deploy Project Lên Ubuntu Server VMware

## 📋 Mục Lục
1. [Giới Thiệu](#giới-thiệu)
2. [Yêu Cầu Hệ Thống](#yêu-cầu-hệ-thống)
3. [Cấu Hình VMware Network](#cấu-hình-vmware-network)
4. [Phương Pháp 1: Deploy Tự Động (Khuyến Nghị)](#phương-pháp-1-deploy-tự-động-khuyến-nghị)
5. [Phương Pháp 2: Deploy Thủ Công](#phương-pháp-2-deploy-thủ-công)
6. [Cấu Hình Firewall](#cấu-hình-firewall)
7. [Kiểm Tra Và Troubleshooting](#kiểm-tra-và-troubleshooting)
8. [Các Lệnh Hữu Ích](#các-lệnh-hữu-ích)

---

## Giới Thiệu

Tài liệu này hướng dẫn chi tiết cách deploy ứng dụng **Drug Use Prevention Support System** (Backend Java Spring Boot + Frontend React) lên Ubuntu Server chạy trên VMware, và truy cập từ máy Windows host.

### Kiến Trúc Deploy

```
┌─────────────────────────────────────────────────────┐
│           Windows Host Machine                      │
│                                                     │
│  Browser → http://192.168.x.x (Ubuntu Server IP)  │
└─────────────────────────────────────────────────────┘
                       ↓
┌─────────────────────────────────────────────────────┐
│         VMware Ubuntu Server (192.168.x.x)         │
│                                                     │
│  ┌──────────────────────────────────────────────┐  │
│  │           Docker Network                     │  │
│  │                                              │  │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────┐  │  │
│  │  │ Frontend │  │ Backend  │  │  MySQL   │  │  │
│  │  │(Port 80) │  │(Port 8080)│ │(Port 3306)│ │  │
│  │  └──────────┘  └──────────┘  └──────────┘  │  │
│  └──────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────┘
```

---

## Yêu Cầu Hệ Thống

### Ubuntu Server
- **OS**: Ubuntu Server 20.04 LTS hoặc mới hơn
- **RAM**: Tối thiểu 4GB (khuyến nghị 8GB)
- **Disk**: Tối thiểu 20GB trống
- **CPU**: 2 cores trở lên

### Phần Mềm
- Docker Engine 20.10+
- Docker Compose 2.0+
- Git (để clone project)

### Windows Host
- Cùng mạng với Ubuntu Server VMware
- Browser: Chrome, Firefox, hoặc Edge

---

## Cấu Hình VMware Network

### Bước 1: Cấu Hình Network Adapter

Có 3 cách cấu hình network cho VMware, chọn 1 trong 3:

#### Option A: Bridged Network (Khuyến Nghị)
✅ **Ưu điểm**: Ubuntu Server có IP trong cùng dải mạng với Windows, dễ truy cập nhất

1. Mở **VMware Workstation/Player**
2. Chọn Ubuntu VM → **Edit virtual machine settings**
3. Chọn tab **Network Adapter**
4. Chọn **Bridged: Connected directly to the physical network**
5. Check **Replicate physical network connection state**
6. Click **OK** và khởi động lại VM

#### Option B: NAT Network
✅ **Ưu điểm**: Đơn giản, tự động cấp IP

1. Chọn Ubuntu VM → **Edit virtual machine settings**
2. Chọn tab **Network Adapter**
3. Chọn **NAT: Used to share the host's IP address**
4. Click **OK**

**Lưu ý với NAT**: Cần cấu hình port forwarding trong VMware:
- Mở **Edit** → **Virtual Network Editor**
- Chọn **VMnet8 (NAT)**
- Click **NAT Settings**
- Thêm port forwarding:
  ```
  Host Port 80   → VM IP Port 80   (Frontend)
  Host Port 8080 → VM IP Port 8080 (Backend)
  ```

#### Option C: Host-Only Network
✅ **Ưu điểm**: Mạng riêng giữa Host và VM, bảo mật hơn

1. Chọn Ubuntu VM → **Edit virtual machine settings**
2. Chọn tab **Network Adapter**
3. Chọn **Host-only: A private network shared with the host**
4. Click **OK**

---

### Bước 2: Kiểm Tra IP Ubuntu Server

Sau khi cấu hình network, khởi động Ubuntu Server và kiểm tra IP:

```bash
# Cách 1: Sử dụng hostname
hostname -I

# Cách 2: Sử dụng ip command
ip addr show

# Cách 3: Sử dụng ifconfig
ifconfig
```

Ghi chú lại địa chỉ IP (ví dụ: `192.168.1.100`)

---

### Bước 3: Test Kết Nối Từ Windows

Mở **Command Prompt** hoặc **PowerShell** trên Windows:

```powershell
# Ping Ubuntu Server
ping 192.168.1.100

# Nếu ping thành công, bạn đã cấu hình đúng!
```

**Nếu ping không thành công**:
- Kiểm tra lại cấu hình network adapter VMware
- Tắt firewall tạm thời trên Ubuntu: `sudo ufw disable`
- Restart network service: `sudo systemctl restart networking`

---

## Phương Pháp 1: Deploy Tự Động (Khuyến Nghị)

### Bước 1: Chuẩn Bị Project

```bash
# SSH vào Ubuntu Server hoặc dùng console VMware
# Clone project về (hoặc copy từ Windows bằng scp/winscp)
cd ~
git clone <repository-url> JAVA-010112213601
cd JAVA-010112213601

# Hoặc nếu đã có source code trên Windows, dùng scp:
# Từ Windows PowerShell:
# scp -r D:\CODE\Java\JAVA-010112213601 username@192.168.1.100:~/
```

### Bước 2: Cấp Quyền Thực Thi Cho Script

```bash
chmod +x deploy.sh
```

### Bước 3: Chạy Script Deploy

```bash
./deploy.sh
```

### Bước 4: Theo Hướng Dẫn Script

Script sẽ tự động:
1. ✅ Kiểm tra và cài đặt Docker (nếu chưa có)
2. ✅ Kiểm tra và cài đặt Docker Compose
3. ✅ Tự động phát hiện IP Ubuntu Server
4. ✅ Tạo file cấu hình `.env.production`
5. ✅ Hỏi bạn chọn chế độ deploy:
   - **Option 1**: Quick Start (nhanh, dùng images có sẵn)
   - **Option 2**: Full Build (build lại, khuyến nghị lần đầu)
   - **Option 3**: Clean & Build (xóa data cũ và build lại)
6. ✅ Khởi động tất cả containers
7. ✅ Hiển thị thông tin truy cập

### Bước 5: Truy Cập Ứng Dụng

Sau khi deploy thành công, mở browser trên **Windows**:

- **Frontend**: `http://192.168.1.100`
- **Backend API**: `http://192.168.1.100:8080`
- **API Docs**: `http://192.168.1.100:8080/swagger-ui/index.html`

---

## Phương Pháp 2: Deploy Thủ Công

### Bước 1: Cài Đặt Docker

```bash
# Cập nhật package index
sudo apt-get update

# Cài đặt các gói cần thiết
sudo apt-get install -y \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

# Thêm Docker GPG key
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | \
    sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Thêm Docker repository
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Cài đặt Docker Engine
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Thêm user vào group docker
sudo usermod -aG docker $USER

# Logout và login lại để áp dụng
```

### Bước 2: Kiểm Tra Docker

```bash
# Kiểm tra Docker version
docker --version

# Kiểm tra Docker Compose
docker compose version
```

### Bước 3: Clone Project

```bash
cd ~
git clone <repository-url> JAVA-010112213601
cd JAVA-010112213601
```

### Bước 4: Tạo File Cấu Hình

Lấy IP của Ubuntu Server:

```bash
SERVER_IP=$(hostname -I | awk '{print $1}')
echo $SERVER_IP
```

Tạo file `.env.production`:

```bash
cat > .env.production << EOF
REACT_APP_API_URL=http://${SERVER_IP}:8080
SERVER_IP=${SERVER_IP}
EOF
```

Kiểm tra file:

```bash
cat .env.production
```

### Bước 5: Build và Khởi Động Containers

```bash
# Build và start tất cả services
docker compose -f docker-compose.production.yml --env-file .env.production up -d --build
docker compose -f docker-compose.yml up --build
```

### Bước 6: Kiểm Tra Trạng Thái

```bash
# Xem trạng thái containers
docker compose -f docker-compose.production.yml ps

# Xem logs
docker compose -f docker-compose.production.yml logs -f
```

---

## Cấu Hình Firewall

### Ubuntu Firewall (UFW)

```bash
# Kiểm tra trạng thái firewall
sudo ufw status

# Nếu firewall đang bật, cho phép các port cần thiết:
sudo ufw allow 80/tcp       # Frontend
sudo ufw allow 8080/tcp     # Backend
sudo ufw allow 3306/tcp     # MySQL (nếu cần truy cập từ bên ngoài)
sudo ufw allow 22/tcp       # SSH

# Reload firewall
sudo ufw reload

# Kiểm tra lại
sudo ufw status verbose
```

### Windows Firewall

Thông thường không cần cấu hình gì trên Windows firewall vì chúng ta đang truy cập RA NGOÀI (outbound), không phải vào trong (inbound).

---

## Kiểm Tra Và Troubleshooting

### 1. Kiểm Tra Containers Đang Chạy

```bash
docker ps
```

Kết quả mong đợi: 3 containers đang chạy (mysql-prod, backend-prod, frontend-prod)

### 2. Kiểm Tra Logs

```bash
# Xem tất cả logs
docker compose -f docker-compose.production.yml logs

# Xem logs của từng service
docker compose -f docker-compose.production.yml logs frontend
docker compose -f docker-compose.production.yml logs backend
docker compose -f docker-compose.production.yml logs mysql

# Xem logs realtime
docker compose -f docker-compose.production.yml logs -f
```

### 3. Kiểm Tra Network

```bash
# Kiểm tra Docker networks
docker network ls

# Kiểm tra chi tiết network
docker network inspect java-010112213601_app-network
```

### 4. Kiểm Tra Port Đang Lắng Nghe

```bash
# Kiểm tra port 80 và 8080
sudo netstat -tlnp | grep -E ':(80|8080)'

# Hoặc dùng ss
sudo ss -tlnp | grep -E ':(80|8080)'
```

### 5. Test API Từ Ubuntu Server

```bash
# Test backend
curl http://localhost:8080/swagger-ui/index.html

# Test frontend
curl http://localhost
```

### 6. Test Từ Windows

Mở PowerShell trên Windows:

```powershell
# Test connectivity
Test-NetConnection -ComputerName 192.168.1.100 -Port 80
Test-NetConnection -ComputerName 192.168.1.100 -Port 8080

# Test HTTP
Invoke-WebRequest -Uri http://192.168.1.100 -UseBasicParsing
```

---

## Các Lỗi Thường Gặp

### Lỗi 0: No Space Left On Device ⭐ QUAN TRỌNG

**Triệu chứng**: 
```
write /tmp/.tmp-compose-build-metadataFile-xxxx.json: no space left on device
```

**Nguyên nhân**: Ổ đĩa Ubuntu Server đã đầy, không đủ không gian để build Docker images.

**Giải pháp nhanh**:

```bash
# 1. Kiểm tra dung lượng
df -h /
docker system df

# 2. Dọn dẹp Docker (cách nhanh nhất)
./cleanup-docker.sh
# → Chọn option 1 (An toàn, giữ database)

# Hoặc dùng lệnh thủ công:
docker system prune -a -f
docker builder prune -a -f

# 3. Deploy lại
./deploy.sh
```

**Giải pháp chi tiết**: Xem file **[FIX_DISK_SPACE.md](FIX_DISK_SPACE.md)**

**Yêu cầu tối thiểu**: Cần ít nhất **10GB trống** để build thành công.

---

### Lỗi 1: Không Thể Truy Cập Từ Windows

**Triệu chứng**: Ping được Ubuntu nhưng không mở được web

**Giải pháp**:
```bash
# 1. Kiểm tra containers có chạy không
docker ps

# 2. Kiểm tra firewall
sudo ufw status
sudo ufw allow 80/tcp
sudo ufw allow 8080/tcp

# 3. Kiểm tra port binding
sudo netstat -tlnp | grep -E ':(80|8080)'

# 4. Restart containers
docker compose -f docker-compose.production.yml restart
```

### Lỗi 2: Backend Không Kết Nối Được MySQL

**Triệu chứng**: Backend logs hiển thị lỗi kết nối database

**Giải pháp**:
```bash
# 1. Kiểm tra MySQL container
docker logs mysql-prod

# 2. Kiểm tra MySQL đã healthy chưa
docker compose -f docker-compose.production.yml ps

# 3. Test kết nối MySQL
docker exec -it mysql-prod mysql -uroot -p123 -e "SHOW DATABASES;"

# 4. Restart backend để thử kết nối lại
docker compose -f docker-compose.production.yml restart backend
```

### Lỗi 3: Frontend Không Gọi Được Backend API

**Triệu chứng**: Frontend hiển thị nhưng không load được data

**Giải pháp**:
```bash
# 1. Kiểm tra biến môi trường
cat .env.production

# 2. Kiểm tra backend có chạy không
curl http://localhost:8080/swagger-ui/index.html

# 3. Build lại frontend với API URL đúng
docker compose -f docker-compose.production.yml up -d --build frontend
```

### Lỗi 4: Docker Build Lỗi Do Thiếu Memory

**Triệu chứng**: Build bị kill hoặc out of memory

**Giải pháp**:
```bash
# Tăng swap space
sudo fallocate -l 4G /swapfile
sudo chmod 600 /swapfile
sudo mkswap /swapfile
sudo swapon /swapfile

# Làm cho swap persistent
echo '/swapfile none swap sw 0 0' | sudo tee -a /etc/fstab

# Build lại
docker compose -f docker-compose.production.yml up -d --build
```

### Lỗi 5: Permission Denied Khi Chạy Docker

**Triệu chứng**: `permission denied while trying to connect to the Docker daemon socket`

**Giải pháp**:
```bash
# Thêm user vào group docker
sudo usermod -aG docker $USER

# Logout và login lại
exit
# (SSH lại vào server)

# Hoặc dùng newgrp (không cần logout)
newgrp docker
```

---

## Các Lệnh Hữu Ích

### Quản Lý Containers

```bash
# Xem logs
docker compose -f docker-compose.production.yml logs -f

# Dừng containers
docker compose -f docker-compose.production.yml stop

# Khởi động containers
docker compose -f docker-compose.production.yml start

# Khởi động lại containers
docker compose -f docker-compose.production.yml restart

# Dừng và xóa containers (giữ data)
docker compose -f docker-compose.production.yml down

# Dừng và xóa containers + volumes (mất data)
docker compose -f docker-compose.production.yml down -v

# Build lại và restart
docker compose -f docker-compose.production.yml up -d --build
```

### Quản Lý Từng Service

```bash
# Restart từng service
docker compose -f docker-compose.production.yml restart frontend
docker compose -f docker-compose.production.yml restart backend
docker compose -f docker-compose.production.yml restart mysql

# Xem logs từng service
docker compose -f docker-compose.production.yml logs -f frontend
docker compose -f docker-compose.production.yml logs -f backend
docker compose -f docker-compose.production.yml logs -f mysql

# Build lại từng service
docker compose -f docker-compose.production.yml up -d --build frontend
```

### Truy Cập Container

```bash
# Truy cập shell của frontend
docker exec -it frontend-prod sh

# Truy cập shell của backend
docker exec -it backend-prod bash

# Truy cập MySQL
docker exec -it mysql-prod mysql -uroot -p123

# Xem files trong container
docker exec frontend-prod ls -la /usr/share/nginx/html
docker exec backend-prod ls -la /app
```

### Backup và Restore Data

```bash
# Backup MySQL database
docker exec mysql-prod mysqldump -uroot -p123 doanyte > backup_$(date +%Y%m%d).sql

# Restore MySQL database
docker exec -i mysql-prod mysql -uroot -p123 doanyte < backup_20240101.sql

# Backup volumes
docker run --rm -v java-010112213601_mysql_data:/data -v $(pwd):/backup ubuntu \
    tar czf /backup/mysql_data_backup.tar.gz -C /data .

# Restore volumes
docker run --rm -v java-010112213601_mysql_data:/data -v $(pwd):/backup ubuntu \
    tar xzf /backup/mysql_data_backup.tar.gz -C /data
```

### Giám Sát Hệ Thống

```bash
# Xem resource usage của containers
docker stats

# Xem disk usage
docker system df

# Xem thông tin chi tiết container
docker inspect backend-prod

# Kiểm tra health check
docker inspect mysql-prod | grep -A 10 Health
```

### Cleanup

```bash
# Xóa các containers đã dừng
docker container prune

# Xóa các images không dùng
docker image prune

# Xóa tất cả (cẩn thận!)
docker system prune -a

# Xóa volumes không dùng
docker volume prune
```

---

## Update Ứng Dụng

Khi có code mới cần deploy:

```bash
# 1. Pull code mới
cd ~/JAVA-010112213601
git pull

# 2. Build lại và restart
docker compose -f docker-compose.production.yml up -d --build

# Hoặc chỉ build service cần thiết
docker compose -f docker-compose.production.yml up -d --build frontend
docker compose -f docker-compose.production.yml up -d --build backend
```

---

## Auto Start Khi Server Restart

Containers đã được cấu hình `restart: unless-stopped`, nghĩa là chúng sẽ tự động khởi động khi Ubuntu Server restart.

Để đảm bảo Docker service tự động start:

```bash
# Enable Docker service
sudo systemctl enable docker

# Kiểm tra
sudo systemctl is-enabled docker
```

---

## Monitoring và Logs

### Setup Log Rotation

```bash
# Tạo file cấu hình Docker daemon
sudo tee /etc/docker/daemon.json > /dev/null <<EOF
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  }
}
EOF

# Restart Docker
sudo systemctl restart docker

# Restart containers
cd ~/JAVA-010112213601
docker compose -f docker-compose.production.yml restart
```

---

## Bảo Mật

### 1. Đổi Password MySQL

```bash
# Sửa file docker-compose.production.yml
# Thay đổi MYSQL_ROOT_PASSWORD
# Sau đó rebuild

# Hoặc đổi password trong MySQL
docker exec -it mysql-prod mysql -uroot -p123
```

```sql
ALTER USER 'root'@'%' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;
```

### 2. Setup HTTPS (Khuyến Nghị cho Production)

Sử dụng **Nginx Reverse Proxy** với **Let's Encrypt SSL**:

```bash
# Cài đặt Certbot
sudo apt-get update
sudo apt-get install -y certbot

# Lấy SSL certificate (cần domain name)
sudo certbot certonly --standalone -d yourdomain.com
```

### 3. Giới Hạn Access MySQL

Mặc định MySQL chỉ nên được truy cập từ backend container, không expose ra ngoài:

```yaml
# Trong docker-compose.production.yml
# Bỏ hoặc comment dòng này:
# ports:
#   - "3306:3306"
```

---

## Liên Hệ và Hỗ Trợ

Nếu gặp vấn đề, kiểm tra:
1. Logs: `docker compose -f docker-compose.production.yml logs -f`
2. Container status: `docker ps -a`
3. Network: `docker network inspect java-010112213601_app-network`
4. Firewall: `sudo ufw status`

---

## Tổng Kết

Sau khi hoàn tất các bước trên, bạn sẽ có:

✅ Backend Java Spring Boot chạy trên port 8080
✅ Frontend React chạy trên port 80
✅ MySQL Database với data persistent
✅ Truy cập từ Windows qua địa chỉ IP Ubuntu Server
✅ Auto-restart khi server reboot
✅ Logs được quản lý tốt

**Chúc bạn deploy thành công! 🎉**

