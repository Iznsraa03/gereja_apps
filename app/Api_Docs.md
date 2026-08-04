# Church Finder API Documentation (v1)

Base URL: `http://localhost:8000/api/v1`
Authentication: Bearer Token (Sanctum)

Semua *response* API mengikuti standar format JSON berikut (kecuali ada *error* dari server):
```json
{
  "success": true,
  "data": { ... } // Atau array [...]
}
```
Jika terjadi *error* validasi / logika:
```json
{
  "success": false,
  "message": "Pesan error"
}
```

---

## 1. Authentication

### Register
Mendaftarkan pengguna baru.
- **Endpoint**: `POST /register`
- **Body**:
  ```json
  {
      "name": "Budi Santoso",
      "email": "budi@example.com",
      "password": "password123",
      "password_confirmation": "password123"
  }
  ```
- **Response**:
  ```json
  {
      "success": true,
      "data": {
          "user": { "id": 1, "name": "Budi Santoso", "email": "budi@example.com" },
          "token": "1|LaravelSanctumToken..."
      }
  }
  ```

### Login
- **Endpoint**: `POST /login`
- **Body**:
  ```json
  {
      "email": "budi@example.com",
      "password": "password123"
  }
  ```
- **Response**: Mengembalikan objek `user` dan `token` seperti *Register*.

### Logout (Membutuhkan Auth)
- **Endpoint**: `POST /logout`
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
  ```json
  {
      "success": true,
      "message": "Logged out successfully"
  }
  ```

### Get Profile (Membutuhkan Auth)
- **Endpoint**: `GET /me`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Mengembalikan data `user` saat ini.

---

## 2. Master Data

### Get Categories
Mengambil semua data kategori denominasi gereja.
- **Endpoint**: `GET /categories`
- **Response**:
  ```json
  {
      "success": true,
      "data": [
          {
              "id": 1,
              "name": "Gereja Toraja",
              "slug": "gereja-toraja"
          },
          {
              "id": 2,
              "name": "Gereja Katolik",
              "slug": "gereja-katolik"
          }
      ]
  }
  ```

### Get Articles
Mengambil daftar artikel / renungan mingguan.
- **Endpoint**: `GET /articles`
- **Response**:
  ```json
  {
      "success": true,
      "data": [
          {
              "id": 1,
              "title": "Renungan Minggu Ini",
              "slug": "renungan-minggu-ini",
              "excerpt": "Cuplikan isi renungan..."
          }
      ]
  }
  ```

### Get Article Detail
- **Endpoint**: `GET /articles/{slug}`
- **Response**: Mengembalikan satu objek artikel lengkap dengan `content` penuh.

---

## 3. Churches (Pencarian Gereja)

### Get All Churches (Search & Filter)
- **Endpoint**: `GET /churches`
- **Query Params**:
  - `search` (opsional): Mencari nama gereja.
  - `category` (opsional): Filter berdasarkan slug kategori (misal: `gereja-toraja`).
- **Response**:
  ```json
  {
      "success": true,
      "data": [
          {
              "id": 1,
              "name": "Gereja Toraja Jemaat Makassar",
              "address": "Jl. Gunung Bawakaraeng No. 1...",
              "latitude": -5.1345100,
              "longitude": 119.4182300,
              "main_image_path": "churches/example-image.jpg",
              "category": { "name": "Gereja Toraja" }
          }
      ]
  }
  ```

### Get Nearby Churches
Menggunakan formula Haversine untuk mencari gereja terdekat dari titik pengguna.
- **Endpoint**: `GET /churches/nearby`
- **Query Params**:
  - `lat` (wajib): Latitude pengguna (misal: `-5.135399`).
  - `lng` (wajib): Longitude pengguna (misal: `119.423790`).
- **Response**:
  ```json
  {
      "success": true,
      "data": [
          {
              "id": 1,
              "name": "Gereja Toraja Jemaat Makassar",
              "main_image_path": "churches/example-image.jpg",
              "distance": 1.25 // Jarak dalam Kilometer
          }
      ]
  }
  ```

### Get Church Detail
Mengambil profil lengkap satu gereja, beserta jadwal ibadah, fasilitas, dan kegiatan (relasi).
- **Endpoint**: `GET /churches/{slug}`
- **Response**:
  ```json
  {
      "success": true,
      "data": {
          "id": 1,
          "name": "Gereja Toraja Jemaat Makassar",
          "description": "Gereja ini melayani jemaat...",
          "main_image_path": "churches/example-image.jpg",
          "schedules": [
              {
                  "id": 1,
                  "title": "Pagi (06.00)",
                  "start_time": "08:00:00"
              }
          ],
          "facilities": [
              {
                  "name": "Gedung Gereja"
              }
          ],
          "activities": [
              {
                  "title": "Ibadah Sekolah Minggu (Minggu, 08.00)"
              }
          ]
      }
  }
  ```

---

## 4. User Interactions (Membutuhkan Auth)

### Get Favorites
- **Endpoint**: `GET /favorites`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Mengembalikan daftar gereja yang difavoritkan oleh user.

### Toggle Favorite
Menambah atau menghapus gereja dari daftar favorit.
- **Endpoint**: `POST /favorites/{church_id}`
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
  ```json
  {
      "success": true,
      "message": "Church added to favorites." // Atau "Church removed from favorites."
  }
  ```

### Get Reminders
- **Endpoint**: `GET /reminders`
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Mengembalikan jadwal ibadah yang diingatkan (reminders).

### Toggle Reminder
Menyalakan atau mematikan pengingat untuk suatu jadwal ibadah.
- **Endpoint**: `POST /reminders/{schedule_id}`
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
  ```json
  {
      "success": true,
      "message": "Reminder set successfully."
  }
  ```
