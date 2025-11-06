Ah! Bạn hỏi tại sao Frontend cần `REACT_APP_API_URL: http://192.168.1.100:8080` đúng không?

## 💡 GIẢI THÍCH

### 🌐 Luồng hoạt động:

```
1. Windows Browser → http://192.168.1.101:3000 (Frontend)
2. Frontend (React) → Gọi API tới Backend
3. Backend ở đâu? → 192.168.1.100:8080
```

### 📱 Khi người dùng truy cập từ Windows:

1. **Browser Windows** mở `http://192.168.1.101:3000`
2. **Frontend React** được tải về browser Windows
3. **JavaScript trong browser** cần gọi API
4. **API URL** phải là địa chỉ mà **browser Windows có thể truy cập**

### ❌ Nếu dùng `localhost`:

```yaml
REACT_APP_API_URL: http://localhost:8080  # ← SAI!
```

**Điều gì xảy ra:**
- Browser Windows nhận được JavaScript
- JavaScript cố gắng gọi `http://localhost:8080`
- `localhost` trên Windows = máy Windows
- Không có Backend trên Windows → **LỖI!**

### ✅ Phải dùng IP thực:

```yaml
REACT_APP_API_URL: http://192.168.1.100:8080  # ← ĐÚNG!
```

**Điều gì xảy ra:**
- Browser Windows nhận được JavaScript  
- JavaScript gọi `http://192.168.1.100:8080`
- `192.168.1.100` = Ubuntu1 (Backend server)
- Backend trả về data → **THÀNH CÔNG!**

---

## 🎯 KIẾN TRÚC THỰC TẾ

```
┌─────────────────────────────────────────┐
│         Windows (192.168.1.x)          │
│                                         │
│  Browser: http://192.168.1.101:3000    │
│     ↓                                   │
│  JavaScript chạy trong browser          │
│     ↓                                   │
│  Gọi API: http://192.168.1.100:8080    │
└─────────────────┬───────────────────────┘
                  │
                  │ Network
                  │
         ┌────────▼─────────┐
         │                  │
         ▼                  ▼
┌─────────────┐    ┌─────────────────┐
│  Ubuntu2    │    │    Ubuntu1      │
│ Frontend    │    │ Backend + MySQL │
│ :3000       │    │ :8080, :3306    │
│             │    │                 │
│ Nginx       │    │ Spring Boot     │
│ (Static)    │    │ (API Server)    │
└─────────────┘    └─────────────────┘
 192.168.1.101      192.168.1.100
```

## 🔍 TẠI SAO KHÔNG PHẢI `192.168.1.101`?

Vì **Backend KHÔNG chạy trên Ubuntu2**!

- Ubuntu2 chỉ có: **Frontend (Nginx + React static files)**
- Ubuntu1 mới có: **Backend (Spring Boot API)**

## 📝 SO SÁNH

### Kiến trúc cũ (1 server):
```yaml
# Cả Frontend và Backend cùng server
REACT_APP_API_URL: http://192.168.1.14:8080  # OK
```

### Kiến trúc mới (2 servers):
```yaml
# Frontend ở Ubuntu2, Backend ở Ubuntu1
REACT_APP_API_URL: http://192.168.1.100:8080  # Phải trỏ tới Ubuntu1
```

## 🎯 CÁCH KIỂM TRA

### Test từ Windows:

```powershell
# Test Frontend (Ubuntu2)
curl http://192.168.1.101:3000

# Test Backend (Ubuntu1) 
curl http://192.168.1.100:8080/auth/login
```

### Xem trong DevTools (F12):

1. Mở `http://192.168.1.101:3000` trên Windows
2. F12 → Network tab
3. Thử đăng nhập
4. Xem request URL → Phải là `http://192.168.1.100:8080/auth/login`

---

## 💡 TÓM LẠI

- **Frontend URL**: `192.168.1.101:3000` (Ubuntu2)
- **Backend URL**: `192.168.1.100:8080` (Ubuntu1)
- **REACT_APP_API_URL**: Phải trỏ tới Backend = `192.168.1.100:8080`

**Lý do:** JavaScript chạy trong browser Windows, cần truy cập Backend qua IP thực, không phải localhost! 🎯