# 🛠️ Lệnh xóa Docker và Cấu hình Static IP Ubuntu

## 📋 MỤC LỤC
1. [Xóa Docker Compose và dọn dẹp](#1-xóa-docker-compose-và-dọn-dẹp)
2. [Cấu hình Static IP trên Ubuntu](#2-cấu-hình-static-ip-trên-ubuntu)

---

## 1️⃣ XÓA DOCKER COMPOSE VÀ DỌN DẸP

### 🔴 Dừng và xóa containers

```bash
# Dừng tất cả containers của một docker-compose file
docker compose down

# Hoặc với file cụ thể
docker compose -f docker-compose.backend.yml down
docker compose -f docker-compose.frontend.yml down
docker compose -f docker-compose.yml down
# Dừng và XÓA VOLUMES (⚠️ Cẩn thận: sẽ mất dữ liệu database!)
docker compose down -v

# Dừng một container cụ thể
docker stop frontend
docker stop backend
docker stop mysql
```

### 🗑️ Xóa containers

```bash
# Xóa một container cụ thể
docker rm frontend
docker rm backend
docker rm mysql

# Xóa một container đang chạy (force)
docker rm -f frontend

# Xóa TẤT CẢ containers đã dừng
docker container prune -f

# Xóa TẤT CẢ containers (kể cả đang chạy) ⚠️
docker rm -f $(docker ps -aq)
```

### 🖼️ Xóa images

```bash
# Xóa một image cụ thể
docker rmi java-010112213601-frontend
docker rmi java-010112213601-backend

# Xóa image bắt buộc (ngay cả khi container đang dùng)
docker rmi -f java-010112213601-frontend

# Xóa TẤT CẢ images không được sử dụng
docker image prune -a -f

# Xóa TẤT CẢ images ⚠️
docker rmi -f $(docker images -aq)
```

### 💾 Xóa volumes

```bash
# Liệt kê volumes
docker volume ls

# Xóa một volume cụ thể
docker volume rm mysql_data
docker volume rm backend_static

# Xóa TẤT CẢ volumes không được sử dụng
docker volume prune -f

# Xóa TẤT CẢ volumes ⚠️ (Mất dữ liệu!)
docker volume rm $(docker volume ls -q)
```

### 🌐 Xóa networks

```bash
# Liệt kê networks
docker network ls

# Xóa một network cụ thể
docker network rm backend_network

# Xóa TẤT CẢ networks không được sử dụng
docker network prune -f
```

### 🧹 DỌN DẸP TOÀN BỘ (Nuclear Option)

```bash
# ⚠️ CẢNH BÁO: Lệnh này xóa TẤT CẢ MỌI THỨ!
# - Tất cả containers (đang chạy và đã dừng)
# - Tất cả images
# - Tất cả volumes (MẤT DỮ LIỆU!)
# - Tất cả networks
# - Build cache

docker system prune -a --volumes -f

# Xác nhận trước khi chạy:
echo "This will delete EVERYTHING. Press Ctrl+C to cancel, Enter to continue."
read
docker system prune -a --volumes -f
```

### 📊 Kiểm tra dung lượng

```bash
# Xem dung lượng Docker đang dùng
docker system df

# Xem chi tiết
docker system df -v
```

---

## 2️⃣ CẤU HÌNH STATIC IP TRÊN UBUNTU

### 🔍 Bước 1: Kiểm tra thông tin hiện tại

```bash
# Xem tên network interface
ip addr show
# Hoặc
ip link show

# Thường sẽ thấy:
# - ens33, ens34 (VMware)
# - eth0, eth1 (VirtualBox)
# - enp0s3, enp0s8 (Một số hệ thống)

# Xem IP hiện tại
hostname -I

# Xem gateway hiện tại
ip route show
# Hoặc
route -n

# Xem DNS hiện tại
cat /etc/resolv.conf
```

**Ghi nhớ:**
- Interface name (ví dụ: `ens33`)
- Current IP (ví dụ: `192.168.1.14`)
- Gateway (ví dụ: `192.168.1.1`)
- DNS servers (ví dụ: `8.8.8.8`)

### 📝 Bước 2: Backup cấu hình cũ

```bash
# Backup file cấu hình
sudo cp /etc/netplan/*.yaml /etc/netplan/backup-$(date +%Y%m%d).yaml

# Liệt kê files netplan
ls -la /etc/netplan/
```

### ✏️ Bước 3: Cấu hình Static IP

#### Option A: Ubuntu 20.04+ (Netplan)

```bash
# Mở file cấu hình
sudo nano /etc/netplan/00-installer-config.yaml
```

**Nội dung mẫu (DHCP - mặc định):**
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens33:
      dhcp4: true
```

**Thay bằng Static IP:**
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens33:                          # Thay bằng interface của bạn
      dhcp4: no                     # Tắt DHCP
      addresses:
        - 192.168.1.14/24           # IP tĩnh của bạn
      routes:
        - to: default
          via: 192.168.1.1          # Gateway (thường là IP router)
      nameservers:
        addresses:
          - 8.8.8.8                 # Google DNS
          - 8.8.4.4                 # Google DNS backup
          - 192.168.1.1             # Router DNS (tùy chọn)
```

**Ví dụ cụ thể cho Server 1 (Backend):**
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens33:
      dhcp4: no
      addresses:
        - 192.168.1.14/24
      routes:
        - to: default
          via: 192.168.1.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 8.8.4.4
```

**Ví dụ cho Server 2 (Frontend):**
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens33:
      dhcp4: no
      addresses:
        - 192.168.1.15/24
      routes:
        - to: default
          via: 192.168.1.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 8.8.4.4
```

**Lưu file:** `Ctrl + O` → `Enter` → `Ctrl + X`

#### Option B: Ubuntu 18.04 hoặc cũ hơn (ifupdown)

```bash
sudo nano /etc/network/interfaces
```

Nội dung:
```bash
auto ens33
iface ens33 inet static
    address 192.168.1.14
    netmask 255.255.255.0
    gateway 192.168.1.1
    dns-nameservers 8.8.8.8 8.8.4.4
```

### ✅ Bước 4: Áp dụng cấu hình

```bash
# Kiểm tra cú pháp trước (Netplan)
sudo netplan try

# Nếu OK, áp dụng vĩnh viễn
sudo netplan apply

# Hoặc (ifupdown)
sudo systemctl restart networking
sudo ifdown ens33 && sudo ifup ens33
```

### 🔍 Bước 5: Kiểm tra

```bash
# Xem IP mới
ip addr show ens33
hostname -I

# Xem routing
ip route show

# Xem DNS
cat /etc/resolv.conf

# Test connectivity
ping 8.8.8.8                # Ping Google DNS
ping google.com             # Ping với DNS resolution
ping 192.168.1.1            # Ping gateway

# Test từ máy khác
# Trên Windows:
ping 192.168.1.14
```

---

## 🎯 QUI TRÌNH HOÀN CHỈNH CHO PROJECT CỦA BẠN

### Server 1 (Backend - IP tĩnh: 192.168.1.14)

```bash
# 1. Backup cấu hình
sudo cp /etc/netplan/*.yaml /etc/netplan/backup.yaml

# 2. Cấu hình Static IP
sudo nano /etc/netplan/00-installer-config.yaml
```

Nội dung:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens33:                    # Thay bằng interface của bạn
      dhcp4: no
      addresses:
        - 192.168.1.14/24
      routes:
        - to: default
          via: 192.168.1.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 8.8.4.4
```

```bash
# 3. Áp dụng
sudo netplan apply

# 4. Kiểm tra
hostname -I
ping 8.8.8.8
```

### Server 2 (Frontend - IP tĩnh: 192.168.1.15)

```bash
# 1. Backup cấu hình
sudo cp /etc/netplan/*.yaml /etc/netplan/backup.yaml

# 2. Cấu hình Static IP
sudo nano /etc/netplan/00-installer-config.yaml
```

Nội dung:
```yaml
network:
  version: 2
  renderer: networkd
  ethernets:
    ens33:                    # Thay bằng interface của bạn
      dhcp4: no
      addresses:
        - 192.168.1.15/24
      routes:
        - to: default
          via: 192.168.1.1
      nameservers:
        addresses:
          - 8.8.8.8
          - 8.8.4.4
```

```bash
# 3. Áp dụng
sudo netplan apply

# 4. Kiểm tra
hostname -I
ping 192.168.1.14      # Ping Server 1
ping 8.8.8.8
```

---

## 🔍 XỬ LÝ SỰ CỐ

### ❌ Lỗi: "Cannot apply netplan configuration"

```bash
# Kiểm tra cú pháp YAML (phải đúng indentation)
sudo netplan --debug apply

# Xem chi tiết lỗi
sudo netplan try
```

**Lưu ý YAML:**
- Dùng **spaces** (2 spaces), KHÔNG dùng tabs
- Indentation phải chính xác
- Dùng `:` và có space sau

### ❌ Mất kết nối sau khi apply

```bash
# Khôi phục cấu hình cũ
sudo cp /etc/netplan/backup.yaml /etc/netplan/00-installer-config.yaml
sudo netplan apply

# Hoặc reboot
sudo reboot
```

### ❌ Không tìm thấy interface name

```bash
# Liệt kê tất cả interfaces
ip link show

# Hoặc
ls /sys/class/net/

# Thường thấy:
# - lo (loopback - bỏ qua)
# - ens33, ens34 (VMware)
# - eth0, eth1 (VirtualBox)
```

### ❌ Không có internet sau khi set Static IP

```bash
# Kiểm tra gateway
ip route show
# Phải thấy: default via 192.168.1.1

# Kiểm tra DNS
cat /etc/resolv.conf
# Phải có: nameserver 8.8.8.8

# Test gateway
ping 192.168.1.1

# Test DNS
nslookup google.com
```

**Giải pháp:** Kiểm tra lại gateway và DNS trong file netplan

---
## WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED! 
## IT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!
Đây là lỗi SSH bình thường khi bạn đổi IP! SSH đang cảnh báo vì trước đây IP 192.168.1.100 có thể đã được dùng bởi máy khác với key khác.
ssh-keygen -R 192.168.1.100
ssh harytien@192.168.1.100
## 📝 CHEAT SHEET

### Docker Compose Commands
```bash
# Dừng và xóa
docker compose down                          # Xóa containers, giữ volumes
docker compose down -v                       # Xóa containers VÀ volumes

# Xóa images
docker rmi java-010112213601-frontend       # Xóa 1 image
docker image prune -a -f                     # Xóa tất cả unused images

# Dọn dẹp toàn bộ
docker system prune -a --volumes -f          # Xóa TẤT CẢ
```

### Static IP Commands
```bash
# Xem thông tin
ip addr show                                 # Xem IP
ip route show                                # Xem gateway
cat /etc/resolv.conf                        # Xem DNS

# Cấu hình
sudo nano /etc/netplan/00-installer-config.yaml
sudo netplan apply                           # Áp dụng

# Kiểm tra
hostname -I                                  # IP hiện tại
ping 8.8.8.8                                # Test internet
```

---

## 💡 TIPS

### 1. Chọn IP tĩnh không xung đột
```bash
# Xem các IP đang dùng trong mạng
nmap -sn 192.168.1.0/24

# Hoặc đơn giản hơn, ping thử
ping 192.168.1.14
ping 192.168.1.15

# Nếu "Host unreachable" → IP đó chưa dùng, OK!
```

### 2. Reserve IP trên Router (Khuyến nghị)
Thay vì set static IP trên Ubuntu, bạn có thể:
1. Login vào Router admin panel
2. Tìm DHCP Reservation / Static DHCP
3. Bind MAC address của Ubuntu với IP cố định
4. Ubuntu vẫn dùng DHCP nhưng luôn nhận IP cố định

**Ưu điểm:** Dễ quản lý, không sợ conflict

### 3. Backup trước khi thay đổi
```bash
# Backup netplan
sudo cp /etc/netplan/*.yaml ~/netplan-backup.yaml

# Backup toàn bộ network config
sudo tar -czf ~/network-backup.tar.gz /etc/netplan/ /etc/network/
```

---

## 🎯 SEQUENCE ĐỀ XUẤT

### Khi setup Server mới:

1. **Cài Ubuntu** → Để DHCP tạm thời
2. **Cài Docker** và các tools cần thiết
3. **Upload code** và test
4. **Khi mọi thứ OK** → Set Static IP
5. **Update docker-compose.yml** với IP mới (nếu cần)
6. **Rebuild containers** nếu thay đổi IP

### Khi dọn dẹp Docker:

1. **Backup database** (nếu cần giữ data)
2. **Stop containers**: `docker compose down`
3. **Xóa images**: `docker rmi ...`
4. **Xóa volumes** (nếu muốn xóa data): `docker volume prune -f`
5. **Rebuild clean**: `docker compose up -d --build`

---

**Happy Configuring! 🚀**

