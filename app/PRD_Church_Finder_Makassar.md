# Product Requirements Document (PRD)

## Aplikasi Pencarian Gereja di Kota Makassar Berbasis Android dengan Metode Haversine

---

## 1. Informasi Produk

| Komponen | Deskripsi |
|---|---|
| **Nama Produk** | Church Finder Makassar |
| **Jenis Produk** | Aplikasi pencarian gereja berbasis lokasi |
| **Platform Pengguna** | Android |
| **Platform Administrator** | Web |
| **Frontend Mobile** | Kotlin dan Jetpack Compose |
| **Backend** | Laravel 13 |
| **Database** | MySQL |
| **Komunikasi Data** | REST API dengan format JSON |
| **Metode Perhitungan Jarak** | Haversine Formula |
| **Layanan Navigasi** | Google Maps |
| **Wilayah Implementasi** | Kota Makassar |

---

## 2. Problem Statement (Pernyataan Masalah)

Kota Makassar memiliki banyak gereja dengan denominasi, tata ibadah, jadwal, fasilitas, serta karakteristik pelayanan yang berbeda. Kondisi tersebut dapat menyulitkan masyarakat, khususnya pendatang atau pengguna yang belum mengenal wilayah Makassar, dalam menemukan gereja yang sesuai dengan denominasi dan kebutuhan ibadah mereka.

Aplikasi pemetaan umum dapat menampilkan lokasi gereja, tetapi informasi yang tersedia sering kali terbatas pada nama, alamat, ulasan, dan petunjuk arah. Informasi khusus seperti denominasi, jadwal ibadah, nama pengkhotbah, kapasitas, fasilitas, kegiatan, dan panduan ibadah belum selalu tersedia secara lengkap dan terstruktur.

Pengguna juga kesulitan menentukan gereja terdekat berdasarkan lokasi aktual perangkat. Tanpa sistem khusus, pengguna perlu membuka dan membandingkan beberapa lokasi secara manual. Proses tersebut kurang efisien dan berpotensi menghasilkan pilihan lokasi yang tidak sesuai dengan denominasi atau kebutuhan pengguna.

Oleh karena itu, diperlukan aplikasi Android yang menyediakan informasi gereja secara terpusat, memungkinkan pencarian berdasarkan kategori atau denominasi, menghitung jarak geografis menggunakan Haversine Formula, serta menyediakan akses navigasi melalui Google Maps.

---

## 3. Goals (Tujuan)

### 3.1 Tujuan Utama

Membangun aplikasi Android yang membantu pengguna menemukan gereja di Kota Makassar berdasarkan kategori, informasi gereja, dan jarak dari lokasi pengguna.

### 3.2 Tujuan Produk

1. Menyediakan data gereja yang terstruktur berdasarkan denominasi atau kategori.
2. Menghitung jarak antara lokasi pengguna dan gereja menggunakan Haversine Formula.
3. Mengurutkan daftar gereja dari jarak terdekat ke jarak terjauh.
4. Menyediakan informasi detail gereja, meliputi alamat, jadwal ibadah, fasilitas, kapasitas, kegiatan, dan kontak.
5. Memungkinkan pengguna membuka rute menuju gereja melalui Google Maps.
6. Menyediakan portal administrator untuk mengelola data gereja dan informasi pendukung.
7. Menyediakan mekanisme penyimpanan data lokal agar informasi terakhir tetap dapat ditampilkan ketika koneksi internet tidak stabil.

### 3.3 Indikator Keberhasilan

| Indikator | Target |
|---|---|
| Waktu respons API daftar gereja | Maksimal 2 detik pada koneksi normal |
| Akurasi perhitungan Haversine | Selisih maksimal 0,05 km dari perhitungan referensi |
| Data awal gereja | Minimal 25 gereja |
| Kategori awal | 5 kategori gereja |
| Kelengkapan data inti | Minimal 90% data gereja memiliki nama, kategori, alamat, koordinat, dan jadwal |
| Keberhasilan CRUD admin | Seluruh proses tambah, baca, ubah, dan hapus berjalan sesuai validasi |
| Keberhasilan pembukaan navigasi | Google Maps dapat dibuka menggunakan koordinat gereja |
| Keberhasilan pengujian fitur utama | Seluruh skenario utama lulus pengujian black-box |

---

## 4. Target Users (Pengguna Sasaran)

### 4.1 Pengguna Publik

Pengguna publik merupakan masyarakat atau jemaat yang menggunakan aplikasi Android untuk mencari dan melihat informasi gereja.

Karakteristik pengguna:

- berdomisili atau sedang berada di Kota Makassar;
- menggunakan perangkat Android;
- membutuhkan informasi gereja berdasarkan denominasi;
- membutuhkan informasi jadwal ibadah;
- membutuhkan gereja terdekat dari lokasi saat ini;
- membutuhkan petunjuk navigasi menuju gereja.

Pengguna publik dapat mengakses fitur pencarian tanpa login. Login hanya diperlukan untuk fitur personal seperti favorit, preferensi kategori, dan pengingat jadwal.

### 4.2 Pengguna Terdaftar

Pengguna terdaftar merupakan pengguna publik yang membuat akun untuk menggunakan fitur personal.

Fitur khusus pengguna terdaftar:

- menyimpan gereja favorit;
- menyimpan preferensi kategori;
- mengatur pengingat jadwal ibadah;
- melihat riwayat gereja yang pernah dibuka;
- menyinkronkan data personal dengan server.

### 4.3 Administrator

Administrator merupakan pengguna yang mengakses portal web Laravel untuk mengelola data aplikasi.

Tanggung jawab administrator:

- mengelola kategori gereja;
- mengelola data gereja;
- mengelola jadwal ibadah;
- mengelola fasilitas;
- mengelola kegiatan;
- mengelola artikel dan pengumuman;
- memverifikasi dan memperbarui informasi gereja;
- menonaktifkan data yang tidak lagi digunakan.

### 4.4 Sistem Eksternal

Sistem eksternal yang digunakan:

- **Google Play Services Location** untuk mendapatkan lokasi perangkat;
- **Google Maps** untuk menampilkan rute dan navigasi;
- **REST API Laravel** sebagai penghubung aplikasi Android dengan database;
- **Firebase Cloud Messaging**, apabila notifikasi dari server dikembangkan pada tahap lanjutan.

---

## 5. User Stories (Kisah Pengguna)

### 5.1 Pengguna Publik

1. Sebagai pengguna publik, saya ingin melihat daftar gereja tanpa harus login, agar saya dapat langsung menggunakan aplikasi.
2. Sebagai pengguna publik, saya ingin mencari gereja berdasarkan nama, agar saya dapat menemukan gereja tertentu dengan cepat.
3. Sebagai pengguna publik, saya ingin memilih kategori gereja, agar hasil pencarian sesuai dengan denominasi yang saya inginkan.
4. Sebagai pengguna publik, saya ingin mengizinkan aplikasi mengakses lokasi perangkat, agar sistem dapat menghitung jarak menuju gereja.
5. Sebagai pengguna publik, saya ingin melihat daftar gereja yang diurutkan dari jarak terdekat, agar saya dapat menentukan gereja yang paling mudah dijangkau.
6. Sebagai pengguna publik, saya ingin melihat detail gereja, agar saya mengetahui alamat, jadwal ibadah, fasilitas, kapasitas, kegiatan, dan kontak gereja.
7. Sebagai pengguna publik, saya ingin membuka lokasi gereja di Google Maps, agar saya dapat mengikuti rute perjalanan.
8. Sebagai pengguna publik, saya ingin tetap melihat data terakhir yang pernah dimuat, agar aplikasi tetap berguna ketika koneksi internet tidak stabil.
9. Sebagai pengguna publik, saya ingin melihat pengumuman dan kegiatan gereja, agar saya memperoleh informasi terbaru.

### 5.2 Pengguna Terdaftar

1. Sebagai pengguna terdaftar, saya ingin login ke aplikasi, agar saya dapat menggunakan fitur personal.
2. Sebagai pengguna terdaftar, saya ingin menyimpan gereja sebagai favorit, agar saya dapat mengaksesnya kembali dengan cepat.
3. Sebagai pengguna terdaftar, saya ingin menghapus gereja dari favorit, agar daftar favorit tetap sesuai kebutuhan saya.
4. Sebagai pengguna terdaftar, saya ingin menyimpan preferensi kategori, agar aplikasi dapat menampilkan kategori yang paling relevan.
5. Sebagai pengguna terdaftar, saya ingin mengatur pengingat jadwal ibadah, agar saya tidak melewatkan waktu ibadah.
6. Sebagai pengguna terdaftar, saya ingin logout dari aplikasi, agar akun saya tetap aman ketika perangkat digunakan orang lain.

### 5.3 Administrator

1. Sebagai administrator, saya ingin login ke portal admin, agar hanya pengguna berwenang yang dapat mengelola data.
2. Sebagai administrator, saya ingin menambah kategori gereja, agar klasifikasi data dapat diperbarui.
3. Sebagai administrator, saya ingin mengelola data gereja, agar informasi yang ditampilkan tetap akurat.
4. Sebagai administrator, saya ingin menentukan latitude dan longitude gereja, agar perhitungan jarak dapat dilakukan.
5. Sebagai administrator, saya ingin mengelola jadwal ibadah, agar pengguna memperoleh jadwal terbaru.
6. Sebagai administrator, saya ingin mengelola fasilitas gereja, agar pengguna dapat mengetahui layanan yang tersedia.
7. Sebagai administrator, saya ingin mengelola kegiatan dan pengumuman, agar informasi gereja dapat disampaikan kepada pengguna.
8. Sebagai administrator, saya ingin mengunggah foto gereja, agar informasi visual dapat ditampilkan di aplikasi.
9. Sebagai administrator, saya ingin mengaktifkan atau menonaktifkan data gereja, agar data yang tidak valid tidak ditampilkan.
10. Sebagai administrator, saya ingin melihat validasi ketika input tidak lengkap atau salah, agar kesalahan data dapat dicegah.

---

## 6. Functional Requirements (Kebutuhan Fungsional)

### 6.1 Autentikasi

#### FR-AUTH-01 — Registrasi Pengguna

Sistem harus menyediakan registrasi pengguna menggunakan:

- nama;
- email;
- password;
- konfirmasi password.

Sistem harus memastikan email bersifat unik.

#### FR-AUTH-02 — Login Pengguna

Sistem harus menerima email dan password, kemudian mengembalikan token autentikasi apabila data valid.

#### FR-AUTH-03 — Logout Pengguna

Sistem harus menghapus atau menonaktifkan token autentikasi pengguna.

#### FR-AUTH-04 — Login Administrator

Portal admin harus menyediakan autentikasi khusus administrator.

#### FR-AUTH-05 — Akses Tanpa Login

Sistem harus mengizinkan pengguna publik mengakses daftar gereja, detail gereja, pencarian, kategori, dan navigasi tanpa login.

---

### 6.2 Lokasi Pengguna

#### FR-LOC-01 — Permintaan Izin Lokasi

Aplikasi harus meminta izin lokasi ketika fitur pencarian gereja terdekat digunakan.

#### FR-LOC-02 — Pengambilan Koordinat

Aplikasi harus mengambil latitude dan longitude perangkat menggunakan layanan lokasi Android.

#### FR-LOC-03 — Penolakan Izin Lokasi

Apabila izin lokasi ditolak, aplikasi harus tetap menampilkan daftar gereja tanpa pengurutan berdasarkan lokasi.

#### FR-LOC-04 — Lokasi Manual

Sistem dapat menyediakan input atau pemilihan lokasi manual sebagai alternatif ketika GPS tidak tersedia.

---

### 6.3 Pencarian dan Filter Gereja

#### FR-SEARCH-01 — Daftar Gereja

Sistem harus menampilkan daftar gereja aktif dari REST API.

#### FR-SEARCH-02 — Pencarian Nama

Sistem harus memungkinkan pencarian berdasarkan nama gereja.

#### FR-SEARCH-03 — Filter Kategori

Sistem harus memungkinkan pengguna memfilter gereja berdasarkan kategori.

Kategori awal:

1. Gereja Toraja;
2. Gereja Pentakosta;
3. Gereja Katolik;
4. Gereja Advent;
5. Gereja Kibaid.

#### FR-SEARCH-04 — Filter Radius

Sistem dapat menyediakan filter radius, misalnya:

- kurang dari 2 km;
- kurang dari 5 km;
- kurang dari 10 km;
- semua jarak.

#### FR-SEARCH-05 — Pengurutan

Sistem harus dapat mengurutkan data berdasarkan:

- jarak terdekat;
- nama gereja;
- kategori.

---

### 6.4 Perhitungan Haversine

#### FR-HAV-01 — Input Koordinat

Backend harus menerima:

- latitude pengguna;
- longitude pengguna.

#### FR-HAV-02 — Perhitungan Jarak

Backend harus menghitung jarak antara koordinat pengguna dan koordinat gereja menggunakan Haversine Formula.

Rumus:

```text
Δlat = lat2 - lat1
Δlon = lon2 - lon1

a = sin²(Δlat / 2)
    + cos(lat1) × cos(lat2) × sin²(Δlon / 2)

c = 2 × atan2(√a, √(1 - a))

d = R × c
```

Keterangan:

```text
R = 6371 km
d = jarak dalam kilometer
```

#### FR-HAV-03 — Konversi Radian

Seluruh nilai latitude dan longitude harus dikonversi dari derajat ke radian sebelum perhitungan.

#### FR-HAV-04 — Pengurutan Jarak

Backend harus mengurutkan hasil perhitungan dari nilai jarak terkecil ke terbesar.

#### FR-HAV-05 — Pembulatan

Jarak harus ditampilkan dengan maksimal dua angka di belakang koma.

#### FR-HAV-06 — Validasi Koordinat

Sistem harus menolak latitude di luar rentang `-90` sampai `90` dan longitude di luar rentang `-180` sampai `180`.

---

### 6.5 Detail Gereja

#### FR-CHURCH-01 — Informasi Dasar

Sistem harus menampilkan:

- nama gereja;
- kategori;
- alamat;
- deskripsi;
- latitude;
- longitude;
- nomor telepon;
- email;
- foto;
- kapasitas;
- status verifikasi.

#### FR-CHURCH-02 — Jadwal Ibadah

Sistem harus menampilkan jadwal ibadah berdasarkan gereja.

Data jadwal minimal:

- nama ibadah;
- hari;
- waktu mulai;
- waktu selesai;
- nama pengkhotbah;
- deskripsi.

#### FR-CHURCH-03 — Fasilitas

Sistem harus menampilkan fasilitas yang dimiliki gereja.

#### FR-CHURCH-04 — Kegiatan

Sistem harus menampilkan kegiatan gereja yang masih aktif atau akan datang.

#### FR-CHURCH-05 — Status Data

Aplikasi hanya boleh menampilkan gereja yang berstatus aktif.

---

### 6.6 Navigasi

#### FR-NAV-01 — Buka Google Maps

Sistem harus menyediakan tombol untuk membuka Google Maps menggunakan koordinat gereja.

#### FR-NAV-02 — Tujuan Navigasi

Aplikasi harus mengirim latitude, longitude, dan nama gereja sebagai tujuan navigasi.

#### FR-NAV-03 — Aplikasi Maps Tidak Tersedia

Apabila Google Maps tidak tersedia, sistem harus menawarkan pembukaan lokasi melalui browser atau aplikasi peta lain.

> Haversine digunakan untuk menghitung jarak geografis, sedangkan penentuan rute jalan dan estimasi waktu perjalanan dilakukan oleh Google Maps.

---

### 6.7 Favorit

#### FR-FAV-01 — Tambah Favorit

Pengguna terautentikasi harus dapat menyimpan gereja ke daftar favorit.

#### FR-FAV-02 — Hapus Favorit

Pengguna terautentikasi harus dapat menghapus gereja dari daftar favorit.

#### FR-FAV-03 — Mencegah Duplikasi

Sistem tidak boleh menyimpan kombinasi pengguna dan gereja yang sama lebih dari satu kali.

---

### 6.8 Pengingat Jadwal

#### FR-REM-01 — Pilih Jadwal

Pengguna terdaftar harus dapat memilih jadwal ibadah yang ingin diingatkan.

#### FR-REM-02 — Waktu Pengingat

Pengguna dapat menentukan pengingat, misalnya:

- 15 menit sebelumnya;
- 30 menit sebelumnya;
- 1 jam sebelumnya;
- 1 hari sebelumnya.

#### FR-REM-03 — Notifikasi Lokal

Aplikasi harus dapat menjadwalkan notifikasi lokal menggunakan WorkManager atau AlarmManager.

#### FR-REM-04 — Pembatalan

Pengguna harus dapat menonaktifkan pengingat yang sudah dibuat.

---

### 6.9 Artikel dan Pengumuman

#### FR-CONTENT-01 — Daftar Artikel

Sistem harus menampilkan artikel yang berstatus terbit.

#### FR-CONTENT-02 — Detail Artikel

Sistem harus menampilkan detail artikel berdasarkan slug atau ID.

#### FR-CONTENT-03 — Pengumuman Aktif

Sistem harus menampilkan pengumuman berdasarkan periode aktif.

---

### 6.10 Portal Administrator

#### FR-ADMIN-01 — Kategori

Administrator harus dapat melakukan CRUD kategori gereja.

#### FR-ADMIN-02 — Gereja

Administrator harus dapat melakukan CRUD data gereja.

#### FR-ADMIN-03 — Jadwal

Administrator harus dapat melakukan CRUD jadwal ibadah.

#### FR-ADMIN-04 — Fasilitas

Administrator harus dapat melakukan CRUD fasilitas.

#### FR-ADMIN-05 — Kegiatan

Administrator harus dapat melakukan CRUD kegiatan gereja.

#### FR-ADMIN-06 — Artikel

Administrator harus dapat melakukan CRUD artikel.

#### FR-ADMIN-07 — Pengumuman

Administrator harus dapat melakukan CRUD pengumuman.

#### FR-ADMIN-08 — Upload Gambar

Administrator harus dapat mengunggah gambar dengan validasi:

- format JPG, JPEG, PNG, atau WebP;
- ukuran maksimal 2 MB;
- nama file dibuat unik oleh sistem.

#### FR-ADMIN-09 — Validasi Form

Sistem harus menampilkan pesan kesalahan pada setiap input yang tidak valid.

#### FR-ADMIN-10 — Soft Delete

Data utama dapat menggunakan soft delete agar data yang dihapus masih dapat dipulihkan.

---

### 6.11 REST API

#### FR-API-01 — Format Respons

API harus menggunakan format JSON yang konsisten.

Contoh:

```json
{
  "success": true,
  "message": "Data gereja berhasil diambil",
  "data": [],
  "meta": {}
}
```

#### FR-API-02 — Versioning

Endpoint harus menggunakan versioning:

```text
/api/v1
```

#### FR-API-03 — Pagination

Endpoint daftar data harus mendukung pagination.

#### FR-API-04 — Filter

Endpoint daftar gereja harus mendukung parameter:

- `search`;
- `category`;
- `latitude`;
- `longitude`;
- `radius`;
- `sort`;
- `page`;
- `per_page`.

#### FR-API-05 — Status HTTP

API harus menggunakan status HTTP yang sesuai:

| Status | Penggunaan |
|---|---|
| `200` | Permintaan berhasil |
| `201` | Data berhasil dibuat |
| `400` | Permintaan tidak valid |
| `401` | Belum terautentikasi |
| `403` | Tidak memiliki hak akses |
| `404` | Data tidak ditemukan |
| `422` | Validasi gagal |
| `500` | Kesalahan internal server |

---

### 6.12 Cache Lokal

#### FR-CACHE-01 — Penyimpanan Data

Aplikasi harus menyimpan kategori dan data gereja terakhir menggunakan Room Database.

#### FR-CACHE-02 — Tampilan Cache

Aplikasi harus menampilkan data cache ketika API tidak dapat diakses.

#### FR-CACHE-03 — Sinkronisasi

Aplikasi harus memperbarui cache setelah memperoleh data terbaru dari API.

---

## 7. Non-Functional Requirements (Kebutuhan Non-Fungsional)

### 7.1 Performa

| ID | Kebutuhan |
|---|---|
| NFR-PERF-01 | Waktu respons endpoint daftar gereja maksimal 2 detik pada kondisi normal. |
| NFR-PERF-02 | Halaman daftar harus menampilkan indikator loading selama proses pengambilan data. |
| NFR-PERF-03 | API harus menggunakan pagination untuk mencegah pengiriman data berlebihan. |
| NFR-PERF-04 | Gambar harus dikompresi atau disediakan dalam ukuran yang sesuai untuk perangkat mobile. |
| NFR-PERF-05 | Perhitungan Haversine untuk 25 data gereja harus selesai kurang dari 1 detik pada server pengembangan normal. |

### 7.2 Keamanan

| ID | Kebutuhan |
|---|---|
| NFR-SEC-01 | Password harus disimpan menggunakan hashing Argon2id atau bcrypt. |
| NFR-SEC-02 | API pengguna terautentikasi harus menggunakan Laravel Sanctum. |
| NFR-SEC-03 | Portal admin harus dilindungi middleware autentikasi dan otorisasi. |
| NFR-SEC-04 | Seluruh input harus divalidasi di backend. |
| NFR-SEC-05 | API produksi harus menggunakan HTTPS. |
| NFR-SEC-06 | Token autentikasi tidak boleh disimpan sebagai teks terbuka yang mudah diakses aplikasi lain. |
| NFR-SEC-07 | Secret key dan kredensial tidak boleh disimpan di repository publik. |
| NFR-SEC-08 | Endpoint sensitif harus menggunakan rate limiting. |
| NFR-SEC-09 | File upload harus divalidasi berdasarkan tipe MIME dan ukuran. |
| NFR-SEC-10 | Aplikasi tidak boleh menyimpan password pengguna pada perangkat. |

### 7.3 Keandalan

| ID | Kebutuhan |
|---|---|
| NFR-REL-01 | Aplikasi harus menangani kegagalan jaringan tanpa berhenti secara paksa. |
| NFR-REL-02 | Aplikasi harus menampilkan pesan kesalahan yang mudah dipahami. |
| NFR-REL-03 | Data cache harus tetap dapat dibaca saat server tidak tersedia. |
| NFR-REL-04 | Proses CRUD harus menggunakan transaksi database apabila melibatkan beberapa tabel. |
| NFR-REL-05 | Sistem harus mencatat kesalahan backend melalui Laravel Logging. |

### 7.4 Usability

| ID | Kebutuhan |
|---|---|
| NFR-USE-01 | Antarmuka Android harus menggunakan Jetpack Compose dan Material 3. |
| NFR-USE-02 | Navigasi utama harus mudah dijangkau dan konsisten. |
| NFR-USE-03 | Pengguna harus dapat mencapai detail gereja maksimal dalam tiga interaksi dari halaman utama. |
| NFR-USE-04 | Sistem harus menyediakan tampilan loading, kosong, berhasil, dan error. |
| NFR-USE-05 | Ukuran teks dan komponen harus mengikuti pedoman aksesibilitas Android. |
| NFR-USE-06 | Tombol navigasi, favorit, dan filter harus memiliki label yang jelas. |

### 7.5 Kompatibilitas

| ID | Kebutuhan |
|---|---|
| NFR-COMP-01 | Aplikasi harus berjalan pada Android 8.0 atau versi lebih baru. |
| NFR-COMP-02 | Antarmuka harus dapat menyesuaikan berbagai ukuran layar smartphone. |
| NFR-COMP-03 | Backend harus berjalan pada lingkungan yang memenuhi kebutuhan Laravel 13. |
| NFR-COMP-04 | Database menggunakan MySQL yang kompatibel dengan Laravel 13. |

### 7.6 Maintainability

| ID | Kebutuhan |
|---|---|
| NFR-MAIN-01 | Android menggunakan pola MVVM dengan pemisahan presentation, domain, dan data. |
| NFR-MAIN-02 | Backend memisahkan controller, form request, service, model, dan API resource. |
| NFR-MAIN-03 | Nama class, function, endpoint, dan tabel harus konsisten. |
| NFR-MAIN-04 | Logika Haversine harus ditempatkan pada service terpisah agar mudah diuji. |
| NFR-MAIN-05 | API harus memiliki dokumentasi endpoint. |
| NFR-MAIN-06 | Migration dan seeder harus tersedia untuk membangun database pengembangan. |

### 7.7 Skalabilitas

| ID | Kebutuhan |
|---|---|
| NFR-SCALE-01 | Struktur database harus mendukung penambahan kategori dan gereja tanpa perubahan besar pada aplikasi. |
| NFR-SCALE-02 | API menggunakan pagination dan filter di server. |
| NFR-SCALE-03 | Penyimpanan gambar harus dapat dipindahkan dari local storage ke object storage pada tahap lanjutan. |
| NFR-SCALE-04 | Proses notifikasi harus dapat dikembangkan dari notifikasi lokal menjadi push notification. |

### 7.8 Privasi

| ID | Kebutuhan |
|---|---|
| NFR-PRIV-01 | Aplikasi harus menjelaskan tujuan penggunaan lokasi sebelum meminta izin. |
| NFR-PRIV-02 | Lokasi pengguna hanya diambil ketika dibutuhkan. |
| NFR-PRIV-03 | Lokasi pengguna tidak disimpan permanen tanpa persetujuan. |
| NFR-PRIV-04 | Pengguna tetap dapat mengakses daftar gereja ketika menolak izin lokasi. |

### 7.9 Pengujian

| ID | Kebutuhan |
|---|---|
| NFR-TEST-01 | Formula Haversine harus diuji menggunakan unit test. |
| NFR-TEST-02 | Endpoint API harus diuji menggunakan Laravel Feature Test atau Postman. |
| NFR-TEST-03 | Fitur utama harus diuji menggunakan metode black-box. |
| NFR-TEST-04 | Pengujian harus mencakup kondisi internet tersedia dan tidak tersedia. |
| NFR-TEST-05 | Pengujian harus mencakup izin lokasi diterima dan ditolak. |
| NFR-TEST-06 | Hasil perhitungan sistem harus dibandingkan dengan perhitungan manual atau referensi. |

---

## 8. Scope (Ruang Lingkup)

### 8.1 In-Scope

Fitur dan komponen yang dikerjakan pada fase utama:

#### Aplikasi Android

- splash screen;
- permintaan izin lokasi;
- halaman beranda;
- daftar kategori gereja;
- pencarian berdasarkan nama;
- filter berdasarkan kategori;
- pengambilan lokasi pengguna;
- perhitungan dan pengurutan jarak;
- daftar gereja terdekat;
- detail gereja;
- jadwal ibadah;
- fasilitas;
- kegiatan dan pengumuman;
- pembukaan rute melalui Google Maps;
- login dan registrasi pengguna;
- gereja favorit;
- pengingat jadwal ibadah;
- cache lokal menggunakan Room;
- pengaturan preferensi sederhana.

#### Backend Laravel

- autentikasi menggunakan Laravel Sanctum;
- REST API versi pertama;
- CRUD kategori gereja;
- CRUD data gereja;
- CRUD jadwal ibadah;
- CRUD fasilitas;
- CRUD kegiatan;
- CRUD artikel;
- CRUD pengumuman;
- upload foto;
- Haversine service;
- pencarian, filter, sorting, dan pagination;
- portal administrator;
- migration dan seeder;
- validasi dan API Resource;
- unit test dan feature test dasar.

#### Data

- wilayah Kota Makassar;
- lima kategori gereja;
- minimal lima gereja per kategori;
- total minimal 25 data gereja;
- koordinat latitude dan longitude;
- data jadwal, fasilitas, dan informasi dasar gereja.

---

### 8.2 Out-of-Scope

Fitur berikut tidak dikerjakan pada fase utama:

- aplikasi iOS;
- aplikasi publik berbasis web;
- pencarian gereja di luar Kota Makassar;
- navigasi turn-by-turn buatan sendiri;
- perhitungan kondisi lalu lintas;
- rekomendasi rute tercepat secara mandiri;
- integrasi pembayaran atau donasi;
- siaran langsung ibadah;
- sistem komunitas atau forum;
- chat antara pengguna dan pengelola gereja;
- ulasan dan rating publik;
- penambahan gereja langsung oleh pengguna;
- verifikasi identitas resmi pengelola gereja;
- integrasi kalender eksternal;
- push notification berbasis Firebase pada fase awal;
- multi-language;
- dashboard analitik kompleks;
- machine learning untuk rekomendasi gereja;
- dukungan lokasi seluruh Indonesia;
- mode navigasi offline penuh.

---

## 9. Arsitektur Sistem

```text
┌───────────────────────────────┐
│       Android Application     │
│ Kotlin + Jetpack Compose      │
│                               │
│ - UI Compose                  │
│ - ViewModel                   │
│ - StateFlow                   │
│ - Retrofit                    │
│ - Room                        │
│ - Location Services           │
└───────────────┬───────────────┘
                │
                │ REST API / JSON / HTTPS
                │
┌───────────────▼───────────────┐
│          Laravel 13           │
│                               │
│ - Authentication              │
│ - REST API                    │
│ - Haversine Service           │
│ - Validation                  │
│ - Business Logic              │
│ - Admin Portal                │
└───────────────┬───────────────┘
                │
┌───────────────▼───────────────┐
│            MySQL              │
│                               │
│ - Users                       │
│ - Categories                  │
│ - Churches                    │
│ - Schedules                   │
│ - Facilities                  │
│ - Activities                  │
│ - Articles                    │
│ - Announcements               │
└───────────────────────────────┘

External Services:
- Google Play Services Location
- Google Maps
```

---

## 10. Arsitektur Android

Aplikasi menggunakan MVVM dan Clean Architecture sederhana.

```text
Presentation Layer
├── Compose Screen
├── Reusable Component
├── ViewModel
├── UI State
└── UI Event

Domain Layer
├── Domain Model
├── Repository Interface
└── Use Case

Data Layer
├── Retrofit API
├── DTO
├── Room Database
├── Mapper
└── Repository Implementation
```

Package yang disarankan:

```text
com.churchfinder.makassar
├── core
│   ├── common
│   ├── network
│   ├── database
│   ├── location
│   └── navigation
├── data
│   ├── remote
│   ├── local
│   ├── mapper
│   └── repository
├── domain
│   ├── model
│   ├── repository
│   └── usecase
├── feature
│   ├── splash
│   ├── auth
│   ├── home
│   ├── church
│   ├── category
│   ├── favorite
│   ├── reminder
│   └── profile
└── di
```

---

## 11. Struktur Backend Laravel

```text
app/
├── Http/
│   ├── Controllers/
│   │   ├── Api/V1/
│   │   └── Admin/
│   ├── Requests/
│   ├── Resources/
│   └── Middleware/
├── Models/
├── Services/
├── Policies/
├── Notifications/
└── Enums/
```

Alur backend:

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
  ↓
JSON Response
```

---

## 12. Entitas Database Utama

| Tabel | Fungsi |
|---|---|
| `users` | Menyimpan pengguna dan administrator |
| `church_categories` | Menyimpan kategori atau denominasi |
| `churches` | Menyimpan informasi utama dan koordinat gereja |
| `worship_schedules` | Menyimpan jadwal ibadah |
| `facilities` | Menyimpan master fasilitas |
| `church_facility` | Menyimpan relasi gereja dan fasilitas |
| `activities` | Menyimpan kegiatan gereja |
| `articles` | Menyimpan artikel |
| `announcements` | Menyimpan pengumuman |
| `favorites` | Menyimpan gereja favorit pengguna |
| `notification_preferences` | Menyimpan preferensi pengingat |

Relasi utama:

```text
ChurchCategory 1 ─── * Church
Church 1 ─── * WorshipSchedule
Church * ─── * Facility
Church 1 ─── * Activity
Church 1 ─── * Announcement
User * ─── * Church melalui Favorite
User 1 ─── * NotificationPreference
WorshipSchedule 1 ─── * NotificationPreference
```

---

## 13. Endpoint Utama

Base URL:

```text
/api/v1
```

### Autentikasi

```http
POST /api/v1/register
POST /api/v1/login
POST /api/v1/logout
GET  /api/v1/me
```

### Kategori

```http
GET /api/v1/categories
GET /api/v1/categories/{slug}
```

### Gereja

```http
GET /api/v1/churches
GET /api/v1/churches/{slug}
GET /api/v1/churches/nearby
```

Contoh:

```http
GET /api/v1/churches?search=toraja
GET /api/v1/churches?category=gereja-toraja
GET /api/v1/churches/nearby?latitude=-5.135399&longitude=119.423790
```

### Jadwal dan Konten

```http
GET /api/v1/churches/{church}/schedules
GET /api/v1/activities
GET /api/v1/articles
GET /api/v1/articles/{slug}
GET /api/v1/announcements
```

### Favorit

```http
GET    /api/v1/favorites
POST   /api/v1/favorites/{church}
DELETE /api/v1/favorites/{church}
```

---

## 14. Alur Utama Pengguna

```text
Pengguna membuka aplikasi
        ↓
Aplikasi menampilkan splash screen
        ↓
Pengguna masuk ke halaman utama
        ↓
Aplikasi meminta izin lokasi
        ↓
Lokasi diterima?
   ├── Ya
   │    ↓
   │ Android mengambil koordinat
   │    ↓
   │ Koordinat dikirim ke Laravel API
   │    ↓
   │ Laravel menghitung Haversine
   │    ↓
   │ Data diurutkan berdasarkan jarak
   │    ↓
   │ Aplikasi menampilkan gereja terdekat
   │
   └── Tidak
        ↓
        Aplikasi menampilkan daftar tanpa jarak
        ↓
Pengguna memilih gereja
        ↓
Aplikasi menampilkan detail
        ↓
Pengguna membuka Google Maps
```

---

## 15. Asumsi dan Batasan Teknis

1. Lokasi pengguna diperoleh dari perangkat Android.
2. Akurasi lokasi bergantung pada GPS, jaringan, dan perangkat pengguna.
3. Haversine menghasilkan jarak garis lurus, bukan jarak jalan.
4. Rute perjalanan ditentukan oleh Google Maps.
5. Data gereja diperbarui oleh administrator.
6. Informasi tidak dianggap real-time kecuali lokasi pengguna dan data yang baru diperbarui.
7. Penggunaan aplikasi memerlukan internet untuk memperoleh data terbaru dan membuka navigasi.
8. Cache lokal hanya menampilkan data terakhir yang berhasil disimpan.
9. Laravel 13 harus dijalankan pada versi PHP yang memenuhi kebutuhan framework.
10. Data awal penelitian dibatasi pada 25 gereja di Kota Makassar.

---

## 16. Acceptance Criteria Utama

Produk dianggap memenuhi fase MVP apabila:

1. Aplikasi Android dapat mengambil data kategori dan gereja dari Laravel API.
2. Administrator dapat melakukan CRUD data gereja melalui portal web.
3. Aplikasi dapat memperoleh lokasi pengguna setelah izin diberikan.
4. Backend dapat menghitung jarak Haversine secara benar.
5. Daftar gereja dapat diurutkan berdasarkan jarak terdekat.
6. Pengguna dapat melakukan pencarian dan filter kategori.
7. Pengguna dapat melihat detail dan jadwal ibadah.
8. Pengguna dapat membuka navigasi menuju gereja melalui Google Maps.
9. Aplikasi tetap menampilkan data cache ketika API tidak dapat diakses.
10. Password disimpan menggunakan hashing yang aman.
11. Endpoint terproteksi tidak dapat diakses tanpa token yang valid.
12. Seluruh skenario fitur utama lulus pengujian black-box.

---

## 17. Risiko dan Mitigasi

| Risiko | Dampak | Mitigasi |
|---|---|---|
| Koordinat gereja tidak akurat | Hasil jarak dan marker salah | Verifikasi koordinat sebelum data diterbitkan |
| Pengguna menolak izin lokasi | Jarak tidak dapat dihitung | Tetap tampilkan daftar dan sediakan lokasi manual |
| Data jadwal tidak diperbarui | Informasi pengguna menjadi salah | Sediakan tanggal pembaruan dan proses verifikasi admin |
| Server tidak tersedia | Aplikasi gagal mengambil data | Gunakan cache Room dan pesan error |
| Gambar terlalu besar | Aplikasi lambat | Batasi ukuran upload dan kompres gambar |
| API token bocor | Akses akun tidak sah | HTTPS, penyimpanan aman, revoke token saat logout |
| Hasil Haversine berbeda | Validitas penelitian dipertanyakan | Gunakan unit test dan aturan pembulatan konsisten |
| Ruang lingkup melebar | Project terlambat selesai | Prioritaskan fitur In-Scope dan tunda fitur lanjutan |

---

## 18. Prioritas Pengembangan

### Prioritas 1 — Inti Penelitian

- data kategori dan gereja;
- lokasi pengguna;
- Haversine Formula;
- pencarian dan filter;
- daftar gereja terdekat;
- detail gereja;
- navigasi Google Maps;
- portal CRUD administrator.

### Prioritas 2 — Pengalaman Pengguna

- autentikasi pengguna;
- favorit;
- cache lokal;
- pengingat jadwal;
- artikel dan pengumuman.

### Prioritas 3 — Pengembangan Lanjutan

- push notification;
- peta interaktif dengan banyak marker;
- verifikasi pengelola gereja;
- ulasan pengguna;
- cakupan wilayah lebih luas;
- analitik penggunaan.

---

## 19. Kesimpulan Produk

Church Finder Makassar dirancang sebagai aplikasi Android yang mengintegrasikan Kotlin, Jetpack Compose, Laravel 13, MySQL, Haversine Formula, dan Google Maps. Sistem berfokus pada penyediaan informasi gereja yang terstruktur dan pencarian berdasarkan jarak geografis.

Haversine Formula digunakan sebagai inti logika pencarian gereja terdekat, sedangkan Google Maps digunakan untuk rute perjalanan. Pembagian tanggung jawab tersebut membuat sistem lebih jelas, dapat diuji, dan sesuai dengan lingkup penelitian.

Portal Laravel menyediakan pusat pengelolaan data, sedangkan aplikasi Jetpack Compose memberikan antarmuka modern kepada pengguna. Dengan pembatasan awal sebanyak lima kategori dan 25 gereja di Kota Makassar, project tetap realistis untuk dikembangkan sekaligus memiliki ruang pengembangan pada tahap berikutnya.
