# Architecture

## Church Finder Makassar

Dokumen ini menjelaskan arsitektur teknis aplikasi **Church Finder Makassar**, yaitu aplikasi Android untuk pencarian gereja di Kota Makassar berdasarkan kategori dan jarak geografis menggunakan Haversine Formula.

Arsitektur sistem dibagi menjadi tiga bagian utama:

1. aplikasi Android;
2. backend REST API dan portal administrator;
3. database dan layanan eksternal.

---

# 1. Architecture Overview

```text
┌─────────────────────────────────────────────┐
│              Android Application            │
│          Kotlin + Jetpack Compose           │
│                                             │
│  Presentation Layer                         │
│  - Screen                                   │
│  - Component                                │
│  - ViewModel                                │
│  - UI State                                 │
│                                             │
│  Domain Layer                               │
│  - Model                                    │
│  - Use Case                                 │
│  - Repository Interface                     │
│                                             │
│  Data Layer                                 │
│  - Retrofit                                 │
│  - Room                                     │
│  - DataStore                                │
│  - Repository Implementation                │
└──────────────────────┬──────────────────────┘
                       │
                       │ HTTPS / REST API / JSON
                       │
┌──────────────────────▼──────────────────────┐
│                 Laravel 13                  │
│                                             │
│  API Layer                                  │
│  - Route                                    │
│  - Controller                               │
│  - Form Request                             │
│  - API Resource                             │
│                                             │
│  Application Layer                          │
│  - Service                                  │
│  - Authorization                            │
│  - Haversine Service                        │
│                                             │
│  Data Layer                                 │
│  - Eloquent Model                           │
│  - Migration                                │
│  - Seeder                                   │
│  - MySQL                                    │
│                                             │
│  Admin Portal                               │
│  - Blade                                    │
│  - Bootstrap / Tailwind                     │
└──────────────────────┬──────────────────────┘
                       │
             ┌─────────▼─────────┐
             │       MySQL       │
             └───────────────────┘

External Services:
- Google Play Services Location
- Google Maps
- Android WorkManager / AlarmManager
```

---

# 2. Tech Stack

## 2.1 Android Application

| Teknologi | Fungsi |
|---|---|
| Kotlin | Bahasa utama pengembangan Android |
| Jetpack Compose | Membangun antarmuka deklaratif |
| Material 3 | Sistem desain aplikasi |
| Navigation Compose | Navigasi antarhalaman |
| ViewModel | Mengelola state dan lifecycle |
| StateFlow | Menyediakan state reaktif ke UI |
| Kotlin Coroutines | Menjalankan proses asynchronous |
| Retrofit | HTTP client untuk REST API |
| OkHttp | Network interceptor, logging, dan konfigurasi koneksi |
| Kotlin Serialization | Serialisasi dan deserialisasi JSON |
| Hilt | Dependency Injection |
| Room Database | Cache dan penyimpanan lokal terstruktur |
| DataStore | Menyimpan preferensi dan token ringan |
| Google Play Services Location | Mengambil lokasi perangkat |
| Google Maps Intent | Membuka rute perjalanan |
| WorkManager | Menjadwalkan pekerjaan latar belakang |
| Coil | Memuat gambar dari URL |
| JUnit | Unit testing |
| MockK | Mocking dependency pada unit test |
| Compose UI Test | Pengujian antarmuka Compose |

## 2.2 Backend

| Teknologi | Fungsi |
|---|---|
| Laravel 13 | Framework backend dan portal admin |
| PHP | Bahasa backend |
| Laravel Sanctum | Autentikasi token API |
| Eloquent ORM | Akses dan relasi database |
| Form Request | Validasi request |
| API Resource | Standardisasi respons JSON |
| Service Layer | Menampung logika bisnis |
| Policy / Gate | Otorisasi pengguna |
| Laravel Scheduler | Menjalankan pekerjaan terjadwal |
| Laravel Queue | Opsional untuk pekerjaan berat |
| Blade | Antarmuka portal administrator |
| Bootstrap atau Tailwind CSS | Styling portal admin |
| PHPUnit / Pest | Pengujian backend |
| Laravel Storage | Pengelolaan file dan gambar |

## 2.3 Database dan Infrastruktur

| Teknologi | Fungsi |
|---|---|
| MySQL | Database utama |
| XAMPP atau Laravel Herd | Lingkungan pengembangan lokal |
| Composer | Dependency manager PHP |
| Gradle | Build system Android |
| Git | Version control |
| GitHub | Repository dan kolaborasi |
| Postman | Pengujian REST API |
| HTTPS | Keamanan komunikasi client-server |

## 2.4 External Services

| Service | Fungsi |
|---|---|
| Google Play Services Location | Mendapatkan koordinat pengguna |
| Google Maps | Menampilkan rute menuju gereja |
| Android Notification Service | Menampilkan pengingat jadwal ibadah |
| Firebase Cloud Messaging | Opsional untuk push notification tahap lanjutan |

---

# 3. Architectural Pattern

## 3.1 Android: MVVM dan Clean Architecture Sederhana

Aplikasi Android menggunakan pola:

```text
UI
↓
ViewModel
↓
Use Case
↓
Repository
↓
Remote API / Local Database
```

Lapisan utama:

```text
Presentation
Domain
Data
```

### Presentation Layer

Bertanggung jawab terhadap:

- tampilan Jetpack Compose;
- state halaman;
- user event;
- navigasi;
- penyajian loading, error, empty, dan success state.

### Domain Layer

Bertanggung jawab terhadap:

- aturan bisnis aplikasi;
- model domain;
- use case;
- kontrak repository.

Domain layer tidak bergantung langsung pada Retrofit, Room, atau Compose.

### Data Layer

Bertanggung jawab terhadap:

- komunikasi dengan Laravel API;
- cache Room;
- DataStore;
- mapping DTO ke domain model;
- implementasi repository.

## 3.2 Backend: Layered Architecture

Backend menggunakan pola berlapis:

```text
Route
↓
Controller
↓
Form Request
↓
Service
↓
Eloquent Model
↓
Database
↓
API Resource
```

### Controller

Controller hanya bertugas menerima request, memanggil service, dan mengembalikan response.

### Form Request

Form Request menangani validasi input dan authorization sederhana.

### Service Layer

Service menampung Haversine Formula, filter gereja, sorting, upload file, proses favorit, aturan bisnis jadwal, dan transaksi multi-model.

### Model

Eloquent Model bertanggung jawab terhadap relasi tabel, query scope, casting, accessor, dan soft delete.

### API Resource

API Resource menjaga format response, menyembunyikan field internal, dan membentuk nested response.

---

# 4. Android Folder Structure

```text
app/
└── src/main/java/com/churchfinder/makassar/
    ├── ChurchFinderApplication.kt
    ├── MainActivity.kt
    ├── core/
    │   ├── common/
    │   ├── network/
    │   ├── database/
    │   ├── datastore/
    │   ├── location/
    │   ├── navigation/
    │   └── ui/
    ├── data/
    │   ├── remote/
    │   │   ├── api/
    │   │   └── dto/
    │   ├── local/
    │   │   ├── dao/
    │   │   └── entity/
    │   ├── mapper/
    │   └── repository/
    ├── domain/
    │   ├── model/
    │   ├── repository/
    │   └── usecase/
    ├── feature/
    │   ├── splash/
    │   ├── auth/
    │   ├── home/
    │   ├── churchlist/
    │   ├── churchdetail/
    │   ├── favorite/
    │   ├── reminder/
    │   ├── profile/
    │   └── settings/
    └── di/
        ├── NetworkModule.kt
        ├── DatabaseModule.kt
        ├── RepositoryModule.kt
        └── LocationModule.kt
```

Contoh isi feature:

```text
feature/churchlist/
├── ChurchListScreen.kt
├── ChurchListViewModel.kt
├── ChurchListUiState.kt
├── ChurchListUiEvent.kt
└── component/
```

---

# 5. Laravel Folder Structure

```text
app/
├── Enums/
├── Http/
│   ├── Controllers/
│   │   ├── Api/V1/
│   │   └── Admin/
│   ├── Middleware/
│   ├── Requests/
│   └── Resources/
├── Models/
├── Policies/
├── Services/
│   ├── AuthService.php
│   ├── ChurchService.php
│   ├── HaversineService.php
│   ├── FavoriteService.php
│   ├── ImageService.php
│   └── NotificationService.php
├── Support/
└── Exceptions/

database/
├── factories/
├── migrations/
└── seeders/

routes/
├── api.php
├── web.php
└── console.php

resources/
├── views/
│   ├── layouts/
│   ├── admin/
│   ├── auth/
│   └── components/
├── css/
└── js/

tests/
├── Feature/
└── Unit/
```

---

# 6. Data Flow Antar-Service

## 6.1 Memuat Daftar Gereja

```text
User
↓
ChurchListScreen
↓
ChurchListViewModel
↓
GetChurchesUseCase
↓
ChurchRepository
↓
ChurchRepositoryImpl
├── ChurchApi
└── ChurchDao
```

Urutan proses:

1. pengguna membuka daftar gereja;
2. ViewModel menjalankan use case;
3. repository membaca cache Room;
4. cache ditampilkan jika tersedia;
5. repository memanggil Laravel API;
6. backend membaca MySQL;
7. API mengirim JSON;
8. DTO dipetakan ke domain model;
9. data disimpan ulang ke Room;
10. StateFlow diperbarui;
11. Compose melakukan recomposition.

## 6.2 Mengambil Gereja Terdekat

```text
ChurchListScreen
↓
LocationProvider
↓
Google Play Services Location
↓
ChurchListViewModel
↓
GetNearbyChurchesUseCase
↓
ChurchRepository
↓
GET /api/v1/churches/nearby
↓
ChurchController
↓
ChurchService
↓
HaversineService
↓
Church Model
↓
MySQL
```

Urutan proses:

1. aplikasi meminta izin lokasi;
2. Android mengambil latitude dan longitude;
3. ViewModel menerima koordinat;
4. repository mengirim koordinat ke API;
5. Laravel memvalidasi koordinat;
6. ChurchService mengambil gereja aktif;
7. HaversineService menghitung jarak;
8. hasil diurutkan dari jarak terkecil;
9. API Resource membentuk response;
10. Android menampilkan hasil.

## 6.3 Pencarian dan Filter

```text
Search Input
↓
ChurchListViewModel
↓
Debounce
↓
SearchChurchesUseCase
↓
ChurchRepository
↓
GET /api/v1/churches
    ?search=
    &category=
    &latitude=
    &longitude=
    &radius=
    &sort=
↓
Laravel Query Builder
↓
MySQL
```

Keputusan:

- input pencarian menggunakan debounce;
- filter dan sorting dilakukan di backend;
- endpoint daftar menggunakan pagination.

## 6.4 Detail Gereja

```text
ChurchDetailScreen
↓
ChurchDetailViewModel
↓
GetChurchDetailUseCase
↓
ChurchRepository
↓
GET /api/v1/churches/{slug}
↓
ChurchController
↓
ChurchService
↓
ChurchDetailResource
```

## 6.5 Login Pengguna

```text
LoginScreen
↓
AuthViewModel
↓
LoginUseCase
↓
AuthRepository
↓
POST /api/v1/login
↓
AuthController
↓
AuthService
↓
Laravel Sanctum
↓
Token Response
↓
AuthDataStore
```

## 6.6 Menambah Favorit

```text
ChurchDetailScreen
↓
FavoriteViewModel
↓
ToggleFavoriteUseCase
↓
FavoriteRepository
↓
POST /api/v1/favorites/{church}
↓
FavoriteController
↓
FavoriteService
↓
Favorite Model
↓
MySQL
```

## 6.7 Membuka Navigasi

```text
ChurchDetailScreen
↓
User menekan "Buka Rute"
↓
Navigator
↓
Google Maps Intent
↓
Google Maps
```

Laravel hanya menyediakan nama, alamat, latitude, dan longitude. Navigasi dijalankan oleh aplikasi peta.

## 6.8 Pengingat Jadwal

```text
WorshipScheduleScreen
↓
ReminderViewModel
↓
CreateReminderUseCase
↓
ReminderRepository
├── Notification Preference API
└── WorkManager
```

Untuk MVP, notifikasi dijalankan secara lokal.

## 6.9 CRUD Administrator

```text
Admin Browser
↓
Laravel Blade Form
↓
Admin Controller
↓
Form Request
↓
Service
↓
Eloquent Model
↓
MySQL
↓
Redirect + Flash Message
```

---

# 7. API Response Contract

Respons berhasil:

```json
{
  "success": true,
  "message": "Data berhasil diambil",
  "data": {},
  "meta": {}
}
```

Respons gagal:

```json
{
  "success": false,
  "message": "Validasi gagal",
  "errors": {
    "latitude": [
      "Latitude harus berada pada rentang -90 sampai 90."
    ]
  }
}
```

---

# 8. Database Design Principles

## 8.1 Normalisasi

Kategori, jadwal, fasilitas, kegiatan, artikel, dan pengumuman disimpan pada tabel terpisah untuk mengurangi redundansi.

## 8.2 Coordinate Type

Latitude dan longitude menggunakan:

```sql
DECIMAL(10,7)
```

Alasan:

- presisi cukup untuk lokasi bangunan;
- hasil stabil dibanding floating-point;
- mudah divalidasi;
- kompatibel dengan MySQL dan Laravel.

## 8.3 Soft Delete

Soft delete digunakan pada data utama seperti churches, articles, announcements, dan activities.

## 8.4 Unique Constraint

```text
users.email
church_categories.slug
churches.slug
favorites(user_id, church_id)
```

---

# 9. Technical Decisions

## 9.1 Kotlin

Dipilih karena null safety, dukungan coroutine, interoperabilitas Java, dan dukungan resmi Android.

## 9.2 Jetpack Compose

Dipilih karena UI deklaratif, integrasi kuat dengan state, reusable component, dan lebih sesuai untuk Android modern.

## 9.3 MVVM

Dipilih untuk memisahkan UI dan logika, menjaga state terhadap lifecycle, dan mempermudah pengujian.

## 9.4 Clean Architecture Sederhana

Dipilih agar domain tidak bergantung pada framework, tetapi tetap menghindari struktur yang terlalu kompleks untuk skala skripsi.

## 9.5 StateFlow

Dipilih karena terintegrasi dengan coroutines dan cocok untuk unidirectional data flow pada Compose.

## 9.6 Coroutines

Digunakan untuk network request, query Room, location request, dan operasi asynchronous tanpa memblokir main thread.

## 9.7 Retrofit dan OkHttp

Retrofit menyederhanakan definisi endpoint. OkHttp digunakan untuk token interceptor, logging, timeout, dan header global.

## 9.8 Hilt

Dipilih untuk dependency injection terpusat dan integrasi dengan ViewModel.

## 9.9 Room

Dipilih sebagai cache lokal resmi Android yang mendukung Flow dan validasi query saat kompilasi.

## 9.10 DataStore

Dipilih untuk menyimpan token, status onboarding, preferensi kategori, dan konfigurasi ringan.

## 9.11 Laravel 13

Dipilih karena menyediakan routing, Eloquent, validation, middleware, authentication, migration, seeder, testing, storage, dan portal admin dalam satu framework.

## 9.12 Laravel Sanctum

Dipilih karena sederhana untuk token authentication aplikasi mobile dan lebih ringan daripada OAuth2.

## 9.13 Service Layer

Dipilih untuk mencegah controller terlalu besar dan memusatkan logika Haversine serta aturan bisnis.

## 9.14 Tidak Menggunakan Repository Pattern Penuh di Laravel

Eloquent sudah menjadi abstraction database. Repository tambahan belum diperlukan untuk skala project ini dan dapat menambah boilerplate.

## 9.15 API Resource

Dipilih untuk menjaga contract JSON tetap konsisten dan memisahkan struktur database dari response publik.

## 9.16 Haversine di Backend

Dipilih karena:

- hasil konsisten pada semua perangkat;
- mudah diuji;
- mendukung sorting dan pagination;
- tidak perlu mengunduh seluruh data ke Android;
- logika penelitian terpusat.

## 9.17 Google Maps Intent

Dipilih untuk MVP karena lebih ringan dibanding menanam peta penuh dan navigasi tetap ditangani Google Maps.

## 9.18 WorkManager

Dipilih untuk pengingat yang harus tetap berjalan walaupun aplikasi ditutup. AlarmManager hanya digunakan jika presisi waktu sangat ketat.

## 9.19 REST API

Dipilih karena sederhana, mudah diuji di Postman, kompatibel dengan Android, dan cocok untuk operasi CRUD.

## 9.20 MySQL

Dipilih karena kompatibel dengan Laravel, mendukung relasi dan transaksi, serta tersedia luas di lingkungan hosting.

---

# 10. Error Handling Strategy

## Android

State utama:

```kotlin
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
```

Kategori error:

- tidak ada koneksi;
- timeout;
- unauthorized;
- validation error;
- data tidak ditemukan;
- server error;
- unknown error.

## Laravel

Laravel menggunakan Form Request, exception handler, HTTP status yang sesuai, response helper, dan logging.

---

# 11. Caching Strategy

```text
Load cache
↓
Tampilkan data lokal
↓
Request API
↓
API berhasil?
├── Ya → Simpan Room → Update UI
└── Tidak → Pertahankan cache → Tampilkan pesan
```

Data cache:

- kategori;
- daftar gereja;
- detail gereja terakhir;
- favorit lokal;
- riwayat pencarian.

Lokasi aktual pengguna dan password tidak disimpan sebagai cache.

---

# 12. Security Architecture

## Android

- token disimpan di DataStore;
- password tidak disimpan;
- request produksi menggunakan HTTPS;
- secret tidak ditulis di source code;
- R8 digunakan pada release build;
- izin lokasi diminta hanya saat diperlukan.

## Laravel

- password menggunakan Argon2id atau bcrypt;
- token menggunakan Sanctum;
- validasi server-side;
- middleware dan policy;
- rate limiting;
- CSRF protection pada portal web;
- validasi file upload;
- konfigurasi sensitif disimpan di `.env`;
- log tidak menyimpan password atau token.

---

# 13. Testing Strategy

## Android

### Unit Test

- use case;
- repository;
- mapper;
- ViewModel;
- error mapping.

### UI Test

- loading state;
- daftar gereja;
- filter;
- detail;
- favorit;
- error state.

## Laravel

### Unit Test

- HaversineService;
- ChurchService;
- response helper;
- slug generator.

### Feature Test

- login;
- endpoint gereja;
- filter kategori;
- validasi koordinat;
- favorit;
- authorization;
- CRUD admin.

---

# 14. Deployment Architecture

## Development

```text
Android Emulator / Physical Device
↓
Local Network
↓
Laravel Local Server
↓
MySQL Local
```

## Production

```text
Android Application
↓
HTTPS
↓
Nginx / Apache
↓
PHP-FPM + Laravel 13
↓
MySQL
↓
Storage
```

Kebutuhan production:

- HTTPS;
- `APP_DEBUG=false`;
- backup database;
- log rotation;
- storage terkelola;
- queue worker jika queue digunakan;
- cron untuk scheduler.

---

# 15. Data Flow Summary

## Android ke Backend

```text
Compose Screen
→ ViewModel
→ Use Case
→ Repository
→ Retrofit
→ Laravel API
```

## Backend ke Database

```text
Route
→ Controller
→ Form Request
→ Service
→ Eloquent
→ MySQL
```

## Backend ke Android

```text
MySQL
→ Eloquent
→ Service
→ API Resource
→ JSON
→ Retrofit
→ Mapper
→ Domain Model
→ StateFlow
→ Compose UI
```

## Offline Flow

```text
Room
→ Repository
→ Use Case
→ ViewModel
→ Compose UI
```

---

# 16. Final Technical Principles

1. backend menjadi sumber data utama;
2. Room hanya digunakan sebagai cache;
3. Haversine dihitung di Laravel;
4. Google Maps menangani rute perjalanan;
5. ViewModel tidak mengakses Retrofit langsung;
6. controller Laravel tidak menyimpan logika bisnis kompleks;
7. response API harus konsisten;
8. token tidak boleh disimpan sembarangan;
9. koordinat pengguna tidak disimpan permanen tanpa persetujuan;
10. setiap halaman memiliki loading, success, empty, dan error state;
11. struktur dibuat modular tetapi tetap realistis untuk skripsi;
12. fitur inti diselesaikan sebelum fitur tambahan.

---

# 17. Kesimpulan

Arsitektur Church Finder Makassar menggunakan pendekatan client-server. Aplikasi Android berbasis Kotlin dan Jetpack Compose bertindak sebagai client, sedangkan Laravel 13 bertindak sebagai backend REST API sekaligus portal administrator.

Pada Android, MVVM dan Clean Architecture sederhana digunakan untuk memisahkan UI, logika bisnis, dan sumber data. Pada backend, layered architecture digunakan melalui controller, form request, service, Eloquent model, dan API resource.

Haversine Formula ditempatkan di backend agar hasil perhitungan konsisten, mudah diuji, dan mendukung filter serta pengurutan. Google Maps digunakan untuk navigasi agar project tidak perlu membangun algoritma rute sendiri.

Arsitektur ini dipilih karena seimbang antara keteraturan, kemudahan pengujian, kecepatan pengembangan, dan tingkat kompleksitas yang realistis untuk project penelitian.
