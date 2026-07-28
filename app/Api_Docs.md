# API Documentation - Church Finder Makassar (v1)

Base URL: `http://localhost:8000/api/v1`

**Konvensi Respons:**
Semua *response* yang dikembalikan oleh API berformat JSON dengan standar berikut:
```json
{
  "success": true,
  "message": "Pesan dari server (opsional)",
  "data": {}
}
```

---

## 1. Authentication (Autentikasi)

### 1.1 Register User
- **URL**: `/register`
- **Method**: `POST`
- **Auth Required**: False

**Request Body**:
```json
{
  "name": "John Doe",
  "email": "johndoe@example.com",
  "password": "password123"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Registrasi berhasil",
  "data": {
    "user": {
      "name": "John Doe",
      "email": "johndoe@example.com",
      "role": "user",
      "is_active": true,
      "updated_at": "2026-07-27T08:00:00.000000Z",
      "created_at": "2026-07-27T08:00:00.000000Z",
      "id": 1
    },
    "token": "1|O2DxgI56Yv7h90ZtQJz1K783..."
  }
}
```

---

### 1.2 Login
- **URL**: `/login`
- **Method**: `POST`
- **Auth Required**: False

**Request Body**:
```json
{
  "email": "johndoe@example.com",
  "password": "password123"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Login berhasil",
  "data": {
    "user": {
      "id": 1,
      "name": "John Doe",
      "email": "johndoe@example.com",
      "role": "user",
      "is_active": true,
      "last_login_at": "2026-07-27T08:05:00.000000Z"
    },
    "token": "2|9ZxtYI56O2DxgJz1K783vh90..."
  }
}
```

---

### 1.3 Logout
- **URL**: `/logout`
- **Method**: `POST`
- **Auth Required**: True (Bearer Token)

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Logout berhasil"
}
```

---

### 1.4 Get Current User Profile
- **URL**: `/me`
- **Method**: `GET`
- **Auth Required**: True (Bearer Token)

**Response (200 OK)**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "johndoe@example.com",
    "phone": null,
    "role": "user",
    "avatar_path": null,
    "is_active": true,
    "last_login_at": "2026-07-27T08:05:00.000000Z",
    "created_at": "2026-07-27T08:00:00.000000Z",
    "updated_at": "2026-07-27T08:05:00.000000Z"
  }
}
```

---

## 2. Master Data (Kategori & Artikel)

### 2.1 Get Categories
- **URL**: `/categories`
- **Method**: `GET`
- **Auth Required**: False

**Response (200 OK)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "Gereja Toraja",
      "slug": "gereja-toraja",
      "description": null,
      "icon_path": null,
      "sort_order": 1,
      "is_active": 1,
      "created_at": "2026-07-27T08:00:00.000000Z",
      "updated_at": "2026-07-27T08:00:00.000000Z",
      "deleted_at": null
    },
    {
      "id": 2,
      "name": "Gereja Pentakosta",
      "slug": "gereja-pentakosta",
      "description": null,
      "icon_path": null,
      "sort_order": 2,
      "is_active": 1,
      "created_at": "2026-07-27T08:00:00.000000Z",
      "updated_at": "2026-07-27T08:00:00.000000Z",
      "deleted_at": null
    }
  ]
}
```

---

### 2.2 Get Articles
Mendapatkan daftar artikel dengan sistem *pagination*.
- **URL**: `/articles`
- **Method**: `GET`
- **Auth Required**: False

**Response (200 OK)**:
```json
{
  "success": true,
  "data": {
    "current_page": 1,
    "data": [
      {
        "id": 1,
        "author_id": 1,
        "title": "Sejarah Gereja Makassar",
        "slug": "sejarah-gereja-makassar",
        "excerpt": "Sejarah singkat...",
        "content": "Isi lengkap artikel...",
        "thumbnail_path": "images/articles/1.jpg",
        "status": "published",
        "published_at": "2026-07-27T08:00:00.000000Z"
      }
    ],
    "first_page_url": "http://localhost:8000/api/v1/articles?page=1",
    "last_page": 1,
    "per_page": 10,
    "total": 1
  }
}
```

---

## 3. Churches (Gereja)

### 3.1 Get All Churches
- **URL**: `/churches`
- **Method**: `GET`
- **Auth Required**: False
- **Query Parameters**:
    - `search` (opsional): filter nama gereja
    - `category_id` (opsional): filter kategori

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Data berhasil diambil",
  "data": {
    "current_page": 1,
    "data": [
      {
        "id": 1,
        "church_category_id": 1,
        "name": "Gereja Toraja Jemaat Makassar",
        "slug": "gt-makassar",
        "address": "Jl. Bawakaraeng No. 1, Makassar",
        "city": "Makassar",
        "province": "Sulawesi Selatan",
        "latitude": "-5.1345000",
        "longitude": "119.4182000",
        "verification_status": "verified",
        "category": {
          "id": 1,
          "name": "Gereja Toraja",
          "slug": "gereja-toraja"
        },
        "images": []
      }
    ],
    "per_page": 15,
    "total": 1
  }
}
```

---

### 3.2 Get Nearby Churches (Haversine)
- **URL**: `/churches/nearby`
- **Method**: `GET`
- **Auth Required**: False
- **Query Parameters**:
    - `latitude` (wajib): e.g., `-5.134`
    - `longitude` (wajib): e.g., `119.418`
    - `search` (opsional)
    - `category_id` (opsional)

**Response (200 OK)**: (Sama seperti `/churches` namun ditambah property `distance` (jarak dalam KM))
```json
{
  "success": true,
  "message": "Data berhasil diambil",
  "data": {
    "current_page": 1,
    "data": [
      {
        "id": 1,
        "church_category_id": 1,
        "name": "Gereja Toraja Jemaat Makassar",
        "latitude": "-5.1345000",
        "longitude": "119.4182000",
        "distance": 0.05971489433365824, 
        "category": { ... },
        "images": []
      }
    ],
    "total": 1
  }
}
```

---

### 3.3 Get Church Detail
- **URL**: `/churches/{slug}`
- **Method**: `GET`
- **Auth Required**: False

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "Detail gereja berhasil diambil",
  "data": {
    "id": 1,
    "church_category_id": 1,
    "name": "Gereja Toraja Jemaat Makassar",
    "slug": "gt-makassar",
    "address": "Jl. Bawakaraeng No. 1, Makassar",
    "city": "Makassar",
    "province": "Sulawesi Selatan",
    "postal_code": null,
    "latitude": "-5.1345000",
    "longitude": "119.4182000",
    "description": null,
    "worship_guide": null,
    "phone": null,
    "email": null,
    "website_url": null,
    "capacity": null,
    "main_image_path": null,
    "verification_status": "verified",
    "category": {
      "id": 1,
      "name": "Gereja Toraja"
    },
    "schedules": [
      {
        "id": 1,
        "church_id": 1,
        "title": "Ibadah Minggu Pagi",
        "day_of_week": 7,
        "start_time": "09:00:00",
        "end_time": "11:00:00",
        "preacher_name": "Pdt. A.B.C"
      }
    ],
    "facilities": [
      {
        "id": 1,
        "name": "Area Parkir Luas",
        "slug": "area-parkir"
      }
    ],
    "images": [],
    "activities": [],
    "announcements": []
  }
}
```

---

## 4. Personalization (Fitur Pengguna Terdaftar)

*(Header Wajib: `Authorization: Bearer {token}`)*

### 4.1 Get Favorites
- **URL**: `/favorites`
- **Method**: `GET`

**Response (200 OK)**:
```json
{
  "success": true,
  "data": {
    "current_page": 1,
    "data": [
      {
        "id": 1,
        "user_id": 1,
        "church_id": 1,
        "created_at": "2026-07-27T08:15:00.000000Z",
        "church": {
          "id": 1,
          "name": "Gereja Toraja Jemaat Makassar",
          "category": {
            "id": 1,
            "name": "Gereja Toraja"
          },
          "images": []
        }
      }
    ],
    "total": 1
  }
}
```

---

### 4.2 Toggle Favorite
Digunakan untuk men-favoritkan atau menghapus dari favorit (switch).
- **URL**: `/favorites/{church_id}`
- **Method**: `POST`

**Response (Jika berhasil ditambahkan)**:
```json
{
  "success": true,
  "message": "Ditambahkan ke favorit"
}
```

**Response (Jika berhasil dihapus dari favorit)**:
```json
{
  "success": true,
  "message": "Dihapus dari favorit"
}
```

---

### 4.3 Get Reminders
- **URL**: `/reminders`
- **Method**: `GET`

**Response (200 OK)**:
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "user_id": 1,
      "worship_schedule_id": 1,
      "reminder_minutes": 30,
      "is_active": 1,
      "schedule": {
        "id": 1,
        "title": "Ibadah Minggu Pagi",
        "start_time": "09:00:00",
        "church": {
          "id": 1,
          "name": "Gereja Toraja Jemaat Makassar"
        }
      }
    }
  ]
}
```

---

### 4.4 Toggle Reminder
Membuat atau menghapus notifikasi pengingat pada suatu jadwal ibadah tertentu.
- **URL**: `/reminders/{worship_schedule_id}`
- **Method**: `POST`

**Request Body** (Opsional, saat membuat baru):
```json
{
  "reminder_minutes": 30
}
```

**Response (Jika berhasil dibuat)**:
```json
{
  "success": true,
  "message": "Pengingat dibuat"
}
```

**Response (Jika berhasil dihapus karena sudah ada)**:
```json
{
  "success": true,
  "message": "Pengingat dihapus"
}
```
