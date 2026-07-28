# Design

## Church Finder Makassar

Dokumen ini menjelaskan desain produk dan antarmuka **Church Finder Makassar**, aplikasi Android berbasis Kotlin dan Jetpack Compose untuk membantu pengguna menemukan gereja di Kota Makassar berdasarkan kategori, informasi ibadah, dan jarak geografis menggunakan Haversine Formula.

Dokumen ini menjadi acuan bagi proses desain UI/UX, implementasi Jetpack Compose, pengujian antarmuka, dan pengembangan portal administrator.

---

# 1. Design Goals

Desain aplikasi harus memenuhi tujuan berikut:

1. memudahkan pengguna menemukan gereja tanpa harus memahami fitur teknis;
2. menampilkan jarak dan kategori gereja secara jelas;
3. memungkinkan pengguna membuka detail gereja dalam maksimal tiga interaksi dari halaman utama;
4. tetap dapat digunakan meskipun pengguna menolak izin lokasi;
5. menyediakan state loading, empty, error, offline, dan success secara konsisten;
6. menggunakan komponen Material 3 yang familiar bagi pengguna Android;
7. menjaga konsistensi visual antara seluruh halaman;
8. mendukung ukuran layar smartphone yang berbeda;
9. mengutamakan aksesibilitas, keterbacaan, dan ukuran target sentuh yang memadai;
10. membedakan secara jelas fungsi Haversine sebagai perhitungan jarak dan Google Maps sebagai penyedia rute.

---

# 2. Design Principles

## 2.1 Simple First

Fitur utama harus langsung terlihat. Pengguna tidak diwajibkan login untuk mencari, memfilter, melihat detail, atau membuka navigasi gereja.

## 2.2 Location with Consent

Izin lokasi hanya diminta ketika fitur jarak terdekat digunakan. Penolakan izin tidak boleh menghalangi akses terhadap daftar gereja.

## 2.3 Progressive Disclosure

Informasi ringkas ditampilkan pada kartu daftar. Informasi lengkap, seperti jadwal, fasilitas, kegiatan, dan kontak, ditampilkan pada halaman detail.

## 2.4 One Primary Action

Setiap halaman memiliki satu aksi utama yang paling menonjol, misalnya:

- **Izinkan Lokasi** pada permission screen;
- **Cari Gereja** pada beranda;
- **Buka Rute** pada detail gereja;
- **Simpan** pada form administrator.

## 2.5 Clear System Feedback

Setiap aksi harus memberikan umpan balik berupa:

- indikator loading;
- snackbar berhasil;
- pesan validasi;
- dialog konfirmasi;
- empty state;
- error state.

## 2.6 Consistent Data Presentation

Format jarak, hari, waktu, alamat, kategori, dan status harus konsisten pada seluruh aplikasi.

---

# 3. User Experience Scope

## 3.1 Android Application

Pengalaman pengguna Android mencakup:

- splash screen;
- permintaan izin lokasi;
- beranda;
- pencarian dan filter;
- daftar gereja;
- detail gereja;
- jadwal ibadah;
- navigasi Google Maps;
- login dan registrasi;
- favorit;
- pengingat;
- profil dan pengaturan;
- kondisi offline.

## 3.2 Admin Portal

Pengalaman administrator mencakup:

- login;
- dashboard;
- pengelolaan kategori;
- pengelolaan gereja;
- pengelolaan jadwal;
- pengelolaan fasilitas;
- pengelolaan kegiatan;
- pengelolaan artikel;
- pengelolaan pengumuman;
- upload gambar;
- validasi data.

---

# 4. Information Architecture

## 4.1 Android Sitemap

```text
Splash
│
├── Home
│   ├── Search
│   ├── Category Filter
│   ├── Nearby Churches
│   ├── Latest Announcements
│   └── Church Detail
│       ├── Overview
│       ├── Worship Schedules
│       ├── Facilities
│       ├── Activities
│       ├── Contact
│       ├── Favorite
│       ├── Reminder
│       └── Open Google Maps
│
├── Explore
│   ├── Church List
│   ├── Filter Sheet
│   ├── Sort Sheet
│   └── Church Detail
│
├── Favorites
│   ├── Favorite List
│   └── Church Detail
│
└── Profile
    ├── Login
    ├── Register
    ├── Reminder Preferences
    ├── Category Preferences
    ├── About Application
    └── Logout
```

## 4.2 Admin Sitemap

```text
Admin Login
│
└── Dashboard
    ├── Categories
    ├── Churches
    │   ├── List
    │   ├── Create
    │   ├── Detail
    │   └── Edit
    ├── Worship Schedules
    ├── Facilities
    ├── Activities
    ├── Articles
    ├── Announcements
    ├── Users
    └── Profile / Logout
```

---

# 5. Navigation Design

## 5.1 Bottom Navigation

Aplikasi Android menggunakan empat menu utama:

| Menu | Fungsi |
|---|---|
| Home | Ringkasan, kategori, gereja terdekat, dan pengumuman |
| Explore | Daftar, pencarian, filter, dan sorting gereja |
| Favorites | Gereja yang disimpan pengguna |
| Profile | Akun, preferensi, pengingat, dan informasi aplikasi |

Bottom navigation hanya ditampilkan pada halaman tingkat utama. Halaman detail, autentikasi, dan form menggunakan top app bar dengan tombol kembali.

## 5.2 Navigation Rules

1. back button harus mengembalikan pengguna ke halaman sebelumnya;
2. memilih item bottom navigation yang sedang aktif tidak membuat halaman duplikat;
3. state pencarian dan filter dipertahankan ketika pengguna membuka detail lalu kembali;
4. deep link detail gereja dapat diarahkan menggunakan slug atau ID;
5. setelah login berhasil, pengguna kembali ke fitur yang sebelumnya membutuhkan autentikasi;
6. setelah logout, data personal dan token dibersihkan, tetapi cache gereja publik tetap tersedia.

---

# 6. Primary User Flows

## 6.1 Mencari Gereja Terdekat

```text
Buka Aplikasi
↓
Splash Screen
↓
Home
↓
Pilih "Gunakan Lokasi Saya"
↓
Izin Lokasi
├── Disetujui
│   ↓
│ Ambil Koordinat
│   ↓
│ Kirim ke Laravel API
│   ↓
│ Hitung Haversine
│   ↓
│ Tampilkan Gereja Terdekat
│
└── Ditolak
    ↓
    Tampilkan Daftar Umum
    ↓
    Sediakan Tombol Coba Lagi / Lokasi Manual
```

## 6.2 Mencari Berdasarkan Kategori

```text
Home / Explore
↓
Pilih Kategori
↓
Daftar Difilter
↓
Pilih Gereja
↓
Lihat Detail
↓
Buka Rute
```

## 6.3 Menyimpan Favorit

```text
Church Detail
↓
Tekan Ikon Favorit
↓
Sudah Login?
├── Ya → Simpan → Tampilkan Snackbar
└── Tidak → Arahkan ke Login → Kembali ke Detail → Simpan
```

## 6.4 Membuat Pengingat Ibadah

```text
Church Detail
↓
Pilih Jadwal
↓
Tekan "Buat Pengingat"
↓
Pilih Waktu Pengingat
↓
Konfirmasi
↓
Simpan Preferensi
↓
Jadwalkan WorkManager / AlarmManager
↓
Tampilkan Status Aktif
```

## 6.5 CRUD Gereja oleh Admin

```text
Admin Login
↓
Dashboard
↓
Menu Gereja
↓
Tambah / Edit
↓
Isi Form
↓
Validasi
├── Gagal → Tampilkan Error per Field
└── Berhasil → Simpan → Redirect ke Daftar → Flash Message
```

---

# 7. Android Screen Specifications

## 7.1 Splash Screen

### Tujuan

Menampilkan identitas aplikasi dan menentukan tujuan navigasi awal.

### Elemen

- logo aplikasi;
- nama aplikasi;
- indikator loading sederhana;
- latar belakang menggunakan warna primary container.

### Perilaku

- tidak lebih lama dari waktu inisialisasi aplikasi;
- memeriksa token dan preferensi awal;
- tidak menampilkan tombol yang tidak diperlukan.

---

## 7.2 Location Permission Screen

### Tujuan

Menjelaskan manfaat akses lokasi sebelum sistem menampilkan dialog izin Android.

### Elemen

- ilustrasi lokasi;
- judul: **Temukan gereja di sekitar Anda**;
- penjelasan singkat;
- tombol utama: **Izinkan Lokasi**;
- tombol sekunder: **Lanjut Tanpa Lokasi**.

### Aturan

- jangan menyatakan bahwa izin lokasi wajib;
- jelaskan bahwa lokasi digunakan untuk menghitung jarak;
- bila ditolak permanen, tampilkan tombol **Buka Pengaturan**.

---

## 7.3 Home Screen

### Struktur

```text
Top App Bar
Location Summary
Search Bar
Category Section
Nearby Churches Section
Announcement Section
Bottom Navigation
```

### Elemen

- sapaan singkat;
- status lokasi;
- search bar;
- horizontal category chips;
- maksimal lima kartu gereja terdekat;
- tombol **Lihat Semua**;
- pengumuman terbaru;
- loading skeleton ketika data belum tersedia.

### Aksi Utama

Mencari atau memilih gereja terdekat.

---

## 7.4 Explore / Church List Screen

### Elemen

- top app bar;
- search field;
- filter chips aktif;
- tombol filter;
- tombol sorting;
- jumlah hasil;
- daftar church card;
- pagination atau infinite scroll;
- pull-to-refresh.

### Sorting

- terdekat;
- nama A–Z;
- kategori.

### Empty State

Contoh pesan:

> Gereja tidak ditemukan. Coba ubah kata pencarian atau filter yang digunakan.

---

## 7.5 Filter Bottom Sheet

### Elemen

- kategori;
- radius;
- status lokasi;
- tombol **Reset**;
- tombol **Terapkan Filter**.

### Aturan

- filter baru diterapkan ketika pengguna menekan tombol;
- jumlah filter aktif ditampilkan pada tombol filter;
- radius dinonaktifkan apabila koordinat pengguna tidak tersedia.

---

## 7.6 Church Card

### Informasi

- thumbnail gereja;
- nama gereja;
- kategori;
- alamat ringkas;
- jarak;
- jadwal terdekat;
- status terverifikasi;
- ikon favorit.

### Aturan Tampilan

- nama maksimal dua baris;
- alamat maksimal dua baris;
- jarak ditampilkan sebagai `1,69 km`;
- jika lokasi tidak tersedia, jarak diganti dengan `Aktifkan lokasi`;
- seluruh area kartu dapat ditekan.

---

## 7.7 Church Detail Screen

### Struktur

```text
Hero Image
Top App Bar Overlay
Church Summary
Primary Actions
Information Sections
Sticky Bottom Action
```

### Informasi Utama

- nama gereja;
- kategori;
- status verifikasi;
- alamat;
- jarak;
- nomor kontak;
- deskripsi.

### Primary Actions

- **Buka Rute**;
- **Favorit**;
- **Bagikan**.

### Bagian Konten

1. jadwal ibadah;
2. fasilitas;
3. kegiatan;
4. pengumuman;
5. kontak;
6. lokasi koordinat.

### Sticky Action

Tombol **Buka Rute** tetap mudah diakses pada bagian bawah layar.

---

## 7.8 Worship Schedule Component

### Informasi

- nama ibadah;
- hari;
- waktu mulai–selesai;
- pengkhotbah;
- status pengingat.

### Aksi

- buat pengingat;
- ubah pengingat;
- hapus pengingat.

Jadwal terdekat diberi penanda visual tanpa mengandalkan warna saja.

---

## 7.9 Login Screen

### Elemen

- logo kecil;
- email field;
- password field;
- show/hide password;
- tombol login;
- link registrasi;
- opsi kembali sebagai pengguna publik.

### Validasi

- email wajib dan valid;
- password wajib;
- loading pada tombol;
- error backend ditampilkan dekat field atau sebagai banner.

---

## 7.10 Register Screen

### Elemen

- nama;
- email;
- password;
- konfirmasi password;
- persetujuan kebijakan privasi;
- tombol daftar.

### Aturan

- password tidak ditampilkan secara default;
- syarat password dijelaskan sebelum submit;
- keberhasilan registrasi mengarahkan pengguna ke aplikasi atau login sesuai implementasi backend.

---

## 7.11 Favorites Screen

### Elemen

- daftar gereja favorit;
- sorting sederhana;
- empty state;
- aksi hapus favorit.

### Empty State

> Belum ada gereja favorit. Simpan gereja dari halaman detail agar mudah ditemukan kembali.

---

## 7.12 Profile Screen

### Pengguna Publik

- tombol login;
- tentang aplikasi;
- kebijakan privasi;
- bantuan;
- pengaturan tema opsional.

### Pengguna Terdaftar

- nama dan email;
- preferensi kategori;
- daftar pengingat;
- tentang aplikasi;
- logout.

---

# 8. Admin Portal Screen Specifications

## 8.1 Admin Login

Elemen:

- email atau username;
- password;
- show/hide password;
- tombol login;
- pesan error autentikasi.

Portal admin tidak menyediakan registrasi publik.

## 8.2 Dashboard

Kartu ringkasan:

- total gereja;
- total kategori;
- jadwal aktif;
- artikel terbit;
- pengumuman aktif;
- data belum terverifikasi.

Bagian tambahan:

- aktivitas terbaru;
- shortcut tambah gereja;
- daftar data yang perlu diperbarui.

## 8.3 Data Table Pattern

Setiap halaman CRUD menggunakan pola:

- judul halaman;
- tombol tambah;
- pencarian;
- filter status;
- tabel;
- pagination;
- aksi lihat, edit, hapus;
- modal atau dialog konfirmasi hapus.

## 8.4 Church Form

Form dibagi menjadi beberapa section:

### Informasi Dasar

- nama;
- kategori;
- alamat;
- deskripsi;
- kapasitas;
- telepon;
- email.

### Lokasi

- latitude;
- longitude;
- preview lokasi opsional;
- tombol ambil koordinat dari input peta pada tahap lanjutan.

### Media

- gambar utama;
- preview gambar;
- batas ukuran dan format.

### Status

- aktif;
- terverifikasi;
- tanggal pembaruan.

### Relasi

- fasilitas;
- jadwal;
- kegiatan.

## 8.5 Form Validation Design

- error ditampilkan di bawah field;
- field error menggunakan outline dan pesan, bukan hanya warna;
- data pengguna tidak hilang ketika validasi gagal;
- tombol submit memiliki loading state;
- perubahan yang belum disimpan memunculkan peringatan saat keluar.

---

# 9. Design System

## 9.1 Material Foundation

Aplikasi Android menggunakan Material 3 dengan dynamic color opsional. Untuk menjaga identitas aplikasi, warna brand tetap menjadi default utama.

## 9.2 Color Roles

Gunakan semantic color roles, bukan warna langsung pada komponen.

| Role | Penggunaan |
|---|---|
| Primary | Tombol utama, elemen aktif, indikator pilihan |
| On Primary | Teks atau ikon di atas primary |
| Primary Container | Hero ringan, selected card, highlight |
| Secondary | Aksi pendukung dan kategori |
| Surface | Latar kartu dan layar |
| Surface Variant | Chip, divider, dan field background |
| Error | Error validasi dan kegagalan |
| Success | Status terverifikasi dan aksi berhasil |
| Warning | Data perlu diperbarui atau belum terverifikasi |

Rekomendasi karakter visual:

- primary bernuansa biru atau teal yang tenang;
- secondary bernuansa hijau lembut;
- latar netral dan tidak terlalu padat;
- hindari simbol keagamaan berlebihan pada setiap komponen.

## 9.3 Typography

Gunakan skala Material 3:

| Style | Penggunaan |
|---|---|
| Display Small | Hero atau landing khusus |
| Headline Medium | Judul halaman utama |
| Title Large | Judul detail dan section |
| Title Medium | Nama gereja pada kartu |
| Body Large | Konten utama |
| Body Medium | Deskripsi dan alamat |
| Label Large | Tombol |
| Label Medium | Chip, badge, metadata |

Aturan:

- minimum body text 14sp;
- teks utama disarankan 16sp;
- line height cukup untuk keterbacaan;
- nama gereja tidak ditulis seluruhnya dengan huruf kapital.

## 9.4 Spacing

Gunakan sistem kelipatan 4dp:

```text
4dp  = micro spacing
8dp  = compact spacing
12dp = component internal spacing
16dp = standard page spacing
24dp = section spacing
32dp = major separation
```

Margin horizontal layar smartphone: `16dp`.

## 9.5 Shape

| Komponen | Radius |
|---|---|
| Button | 12dp |
| Card | 16dp |
| Search Field | 16–24dp |
| Chip | Full / pill |
| Bottom Sheet | 28dp pada bagian atas |
| Image | 12–16dp |

## 9.6 Elevation

Gunakan elevation minimal:

- card normal: 0–1dp;
- floating action atau sticky action: 3–6dp;
- dialog dan bottom sheet mengikuti Material 3.

Pemisahan konten lebih mengandalkan surface color dan spacing daripada bayangan berlebihan.

## 9.7 Iconography

Gunakan Material Symbols atau ikon konsisten untuk:

- lokasi;
- kategori;
- jadwal;
- fasilitas;
- favorit;
- navigasi;
- pengumuman;
- akun.

Setiap ikon penting disertai label atau content description.

---

# 10. Reusable UI Components

Komponen Jetpack Compose yang disarankan:

```text
AppTopBar
AppBottomNavigation
ChurchSearchBar
CategoryChip
ChurchCard
DistanceBadge
VerificationBadge
ScheduleCard
FacilityChip
AnnouncementCard
SectionHeader
PrimaryButton
SecondaryButton
LoadingButton
EmptyState
ErrorState
OfflineBanner
LoadingSkeleton
ConfirmDialog
FilterBottomSheet
SortBottomSheet
LocationPermissionCard
```

Aturan komponen:

1. menerima state dan callback melalui parameter;
2. tidak mengakses repository secara langsung;
3. mendukung preview;
4. mendukung light dan dark mode;
5. memiliki content description yang sesuai;
6. tidak menyimpan state bisnis sendiri kecuali state UI lokal.

---

# 11. UI State Design

Setiap screen utama menggunakan state berikut:

```text
Idle
Loading
Success
Empty
Error
OfflineWithCache
Refreshing
```

Contoh model state:

```kotlin
data class ChurchListUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val churches: List<ChurchUiModel> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryId: Long? = null,
    val selectedRadiusKm: Double? = null,
    val sortOption: ChurchSortOption = ChurchSortOption.NEAREST,
    val isOffline: Boolean = false,
    val errorMessage: String? = null
)
```

## 11.1 Loading State

- gunakan skeleton untuk daftar;
- gunakan progress indicator pada aksi submit;
- jangan memblokir seluruh halaman untuk refresh kecil.

## 11.2 Empty State

Empty state harus menjelaskan:

- apa yang kosong;
- kemungkinan penyebab;
- tindakan yang dapat dilakukan.

## 11.3 Error State

Error dibedakan menjadi:

- koneksi internet;
- timeout;
- autentikasi;
- validasi;
- data tidak ditemukan;
- server error.

Setiap error yang dapat diperbaiki memiliki tombol **Coba Lagi**.

## 11.4 Offline State

Jika cache tersedia:

- tampilkan data lokal;
- tampilkan banner **Menampilkan data tersimpan**;
- sediakan tombol refresh.

---

# 12. Interaction Design

## 12.1 Search

- pencarian mulai setelah minimal dua karakter;
- gunakan debounce sekitar 300–500 ms;
- tampilkan tombol clear;
- keyboard action menggunakan Search;
- query dipertahankan ketika kembali dari detail.

## 12.2 Filter

- filter aktif terlihat sebagai chip;
- pengguna dapat menghapus satu filter langsung dari chip;
- tombol reset tersedia;
- radius hanya aktif jika lokasi tersedia.

## 12.3 Favorite

- gunakan optimistic update;
- tampilkan snackbar berhasil;
- rollback UI bila request gagal;
- pengguna yang belum login diarahkan ke login dengan konteks aksi yang dipertahankan.

## 12.4 Destructive Action

Aksi hapus pada portal admin membutuhkan dialog konfirmasi dengan:

- nama data;
- dampak penghapusan;
- tombol batal;
- tombol hapus.

## 12.5 Form Submission

- tombol dinonaktifkan saat submit;
- cegah submit berulang;
- fokus diarahkan ke error pertama;
- keberhasilan menampilkan feedback yang jelas.

---

# 13. Content Design

## 13.1 Tone

Teks menggunakan bahasa Indonesia yang:

- sederhana;
- sopan;
- langsung;
- tidak terlalu teknis;
- tidak menghakimi denominasi tertentu.

## 13.2 Terminology

Gunakan istilah berikut secara konsisten:

| Gunakan | Hindari |
|---|---|
| Gereja terdekat | Gereja tercepat |
| Jarak garis lurus | Jarak rute Haversine |
| Buka rute | Hitung rute dengan Haversine |
| Kategori gereja | Agama gereja |
| Jadwal ibadah | Jam operasional |
| Lokasi Anda | Posisi tracking Anda |

## 13.3 Example Microcopy

### Location

> Lokasi digunakan untuk menghitung jarak antara posisi Anda dan gereja. Lokasi tidak disimpan secara permanen.

### Offline

> Anda sedang offline. Data yang ditampilkan berasal dari penyimpanan terakhir.

### Empty Search

> Belum ada gereja yang sesuai dengan pencarian ini.

### Navigation

> Rute perjalanan akan dibuka melalui Google Maps.

### Reminder

> Pengingat berhasil dibuat untuk 30 menit sebelum ibadah.

---

# 14. Accessibility

Persyaratan aksesibilitas:

1. target sentuh minimal 48dp;
2. seluruh ikon interaktif memiliki content description;
3. rasio kontras teks mengikuti WCAG AA;
4. informasi tidak disampaikan hanya melalui warna;
5. dukung font scaling Android;
6. urutan fokus screen reader mengikuti urutan visual;
7. field memiliki label permanen;
8. error dibacakan oleh accessibility service;
9. gambar dekoratif tidak membutuhkan deskripsi;
10. gambar gereja memiliki deskripsi ringkas yang relevan.

---

# 15. Responsive and Adaptive Design

## 15.1 Compact Width

Untuk smartphone umum:

- bottom navigation;
- satu kolom card;
- detail vertikal;
- filter menggunakan bottom sheet.

## 15.2 Medium Width

Untuk foldable atau tablet kecil:

- navigation rail opsional;
- grid dua kolom untuk daftar;
- detail memiliki content width terbatas;
- filter dapat menjadi side sheet.

## 15.3 Large Width Admin Portal

- sidebar tetap;
- tabel penuh;
- form dua kolom untuk field yang berkaitan;
- content max-width agar keterbacaan terjaga.

---

# 16. Image and Media Guidelines

## 16.1 Church Images

- rasio rekomendasi `16:9`;
- thumbnail card menggunakan crop yang konsisten;
- detail menggunakan hero image;
- placeholder ditampilkan bila gambar gagal;
- gambar tidak boleh menampilkan watermark yang mengganggu.

## 16.2 Upload Admin

- format JPG, JPEG, PNG, atau WebP;
- ukuran maksimal 2 MB;
- preview sebelum upload;
- rekomendasi resolusi minimal 1280 × 720;
- kompresi dilakukan di backend atau sebelum upload jika diperlukan.

---

# 17. Data Presentation Rules

## 17.1 Distance

- kurang dari 1 km dapat ditampilkan dalam meter, contoh `850 m`;
- 1 km atau lebih ditampilkan dua angka desimal, contoh `1,69 km`;
- hasil merupakan jarak geografis, bukan jarak jalan.

## 17.2 Date and Time

- hari menggunakan bahasa Indonesia;
- format waktu `08.00–10.00 WITA`;
- tanggal menggunakan format `27 Juli 2026`;
- timezone utama: WITA.

## 17.3 Coordinates

Koordinat tidak perlu menjadi informasi utama. Tampilkan pada bagian lokasi atau informasi teknis dengan maksimal tujuh angka desimal.

## 17.4 Status

Status yang digunakan:

- aktif;
- tidak aktif;
- terverifikasi;
- belum terverifikasi;
- terbit;
- draft;
- kedaluwarsa.

---

# 18. Validation and Feedback

## 18.1 Android Form Validation

Validasi dasar dilakukan di client untuk pengalaman pengguna. Validasi final tetap dilakukan backend.

Contoh:

- email tidak valid;
- password kosong;
- konfirmasi password tidak sama;
- waktu pengingat belum dipilih.

## 18.2 Admin Form Validation

Contoh pesan:

- `Nama gereja wajib diisi.`
- `Latitude harus berada antara -90 dan 90.`
- `Longitude harus berada antara -180 dan 180.`
- `Gambar maksimal berukuran 2 MB.`
- `Waktu selesai harus setelah waktu mulai.`

## 18.3 Feedback Components

| Kondisi | Komponen |
|---|---|
| Berhasil singkat | Snackbar |
| Error field | Supporting text pada field |
| Error halaman | Error state atau banner |
| Konfirmasi hapus | Dialog |
| Informasi penting | Alert / info card |
| Offline | Persistent banner |

---

# 19. Motion and Animation

Animasi digunakan secara ringan:

- fade untuk pergantian loading ke konten;
- animate content size pada section yang diperluas;
- shared transition opsional untuk gambar gereja;
- snackbar dan bottom sheet mengikuti motion Material 3;
- hindari animasi panjang yang memperlambat akses informasi.

Durasi disarankan:

- micro interaction: 100–200 ms;
- screen transition: 200–300 ms;
- expandable content: 200–300 ms.

Dukung pengaturan reduced motion jika tersedia.

---

# 20. Dark Mode

Dark mode harus:

- menggunakan semantic color role;
- tidak sekadar membalik warna;
- menjaga keterbacaan foto dan teks;
- menggunakan elevation dan surface tone Material 3;
- menghindari latar hitam absolut kecuali diperlukan.

Theme options:

- mengikuti sistem;
- light;
- dark.

Fitur pemilihan tema dapat menjadi fitur sekunder.

---

# 21. Design Tokens

Contoh struktur token pada Compose:

```kotlin
object AppSpacing {
    val Xs = 4.dp
    val Sm = 8.dp
    val Md = 12.dp
    val Lg = 16.dp
    val Xl = 24.dp
    val Xxl = 32.dp
}

object AppSize {
    val MinTouchTarget = 48.dp
    val ChurchCardImageHeight = 160.dp
    val HeroImageHeight = 240.dp
}
```

Token harus digunakan daripada angka acak di setiap komponen.

---

# 22. Compose Screen Pattern

Setiap feature direkomendasikan memiliki struktur:

```text
feature/churchlist/
├── ChurchListRoute.kt
├── ChurchListScreen.kt
├── ChurchListViewModel.kt
├── ChurchListUiState.kt
├── ChurchListUiEvent.kt
└── component/
    ├── ChurchCard.kt
    ├── FilterBar.kt
    └── ChurchListSkeleton.kt
```

Pembagian tanggung jawab:

- `Route`: menghubungkan ViewModel dan navigasi;
- `Screen`: composable stateless sebanyak mungkin;
- `ViewModel`: state dan event bisnis;
- `UiState`: seluruh state tampilan;
- `UiEvent`: aksi satu kali seperti snackbar atau navigasi;
- `component`: komponen khusus feature.

---

# 23. Admin UI Pattern

Portal admin menggunakan layout:

```text
Sidebar
Top Header
Breadcrumb
Page Title + Primary Action
Filter / Search Area
Content Table or Form
Pagination
```

Prinsip:

- aksi utama berada di kanan atas;
- aksi per baris menggunakan menu atau tombol kecil;
- status menggunakan badge;
- tabel tetap dapat digulir pada layar sempit;
- form panjang dibagi menjadi card section;
- jangan menampilkan semua relasi dalam satu modal kecil.

---

# 24. Design Acceptance Criteria

Desain dianggap siap diimplementasikan apabila:

1. sitemap Android dan admin telah ditentukan;
2. seluruh fitur utama memiliki user flow;
3. setiap screen memiliki tujuan, informasi, dan aksi utama;
4. loading, empty, error, offline, dan success state telah didefinisikan;
5. komponen reusable telah diidentifikasi;
6. aturan warna, tipografi, spacing, shape, dan ikon telah ditentukan;
7. alur tanpa izin lokasi tetap tersedia;
8. alur pengguna tanpa login tetap tersedia;
9. alur login untuk fitur personal telah ditentukan;
10. desain detail gereja membedakan jarak Haversine dan rute Google Maps;
11. form admin memiliki validasi dan feedback;
12. desain mendukung aksesibilitas dan ukuran layar berbeda;
13. format jarak, waktu, status, dan koordinat telah konsisten;
14. seluruh komponen dapat dipetakan ke Jetpack Compose atau Blade.

---

# 25. Recommended Implementation Priority

## Phase 1 — Core Experience

- design system dasar;
- splash;
- location permission;
- home;
- church list;
- search dan kategori;
- church card;
- detail gereja;
- buka Google Maps;
- loading, empty, error, dan offline state.

## Phase 2 — Personal Features

- login;
- registrasi;
- favorit;
- pengingat;
- profile.

## Phase 3 — Admin Portal

- login admin;
- dashboard;
- data table pattern;
- church form;
- kategori;
- jadwal;
- fasilitas;
- kegiatan;
- artikel dan pengumuman.

## Phase 4 — Polish

- dark mode;
- animation;
- tablet adaptation;
- advanced map preview;
- visual regression testing.

---

# 26. Final Design Summary

Desain Church Finder Makassar mengutamakan pencarian gereja yang cepat, informasi yang terstruktur, dan akses navigasi yang sederhana. Pengguna dapat mencari gereja tanpa login, memilih kategori, mengaktifkan lokasi untuk melihat jarak, membuka detail, dan melanjutkan perjalanan melalui Google Maps.

Jetpack Compose dan Material 3 digunakan untuk membangun antarmuka Android yang modern, reaktif, dan reusable. Pola screen dibangun berdasarkan state yang jelas agar aplikasi mampu menangani kondisi loading, error, offline, dan data kosong secara konsisten.

Portal administrator menggunakan pola dashboard, data table, dan form terstruktur agar pengelolaan data gereja tetap efisien. Desain keseluruhan disusun agar selaras dengan PRD dan Architecture, serta tetap realistis untuk dikembangkan sebagai project penelitian.
