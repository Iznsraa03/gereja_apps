# Ringkasan Sesi Pengembangan (Gereja Apps)

Dokumen ini merangkum seluruh perubahan, fitur baru, dan perbaikan bug yang telah dilakukan pada kode sumber aplikasi `gereja_apps` (Android/Kotlin) dan `gereja_backend` (Laravel/PHP) selama sesi ini.

## 1. Implementasi Peta Menggunakan Leaflet JS (Tanpa Google Maps SDK)
- **Tujuan**: Memasukkan peta ke dalam aplikasi tanpa redirect ke aplikasi pihak ketiga dan tanpa perlu menggunakan API Key dari Google Maps (menghindari biaya/pendaftaran API Key).
- **Eksekusi**: 
  - Seluruh *dependencies* Google Maps dihapus dari `build.gradle.kts` dan `AndroidManifest.xml`.
  - Halaman `RouteNavigationScreen.kt` ditulis ulang menggunakan komponen Jetpack Compose `AndroidView` yang memuat `WebView`.
  - Peta di-*render* secara lokal menggunakan HTML *string* yang memuat pustaka *open-source* **Leaflet JS** dan _tiles_ dari **OpenStreetMap**.

## 2. Fitur Tracking Lokasi dan Rute Real-Time
- **Pendeteksi Lokasi Native**: Menggunakan `LocationManager` bawaan Android untuk mendapatkan kordinat (_latitude/longitude_) dari pengguna (`ACCESS_FINE_LOCATION`).
- **Kalkulasi Jarak**: Sistem kini dapat menghitung jarak lurus (menggunakan `Location.distanceTo`) dan menampilkannya pada UI (_Bottom Sheet_) dalam satuan Meter (m) atau Kilometer (km).
- **Garis Rute di Peta**: Disuntikkan _library_ `leaflet-routing-machine` ke dalam WebView. Jika lokasi pengguna ditemukan, peta akan langsung menarik garis jalan dari lokasi pengguna ke gereja tujuan tanpa perlu API perhitungan rute pihak ketiga (menggunakan rute OSRM bawaan Leaflet Routing).

## 3. Kewajiban Izin Lokasi dan GPS (Mandatory Location Guard)
- **Logika Pencegatan**: Agar fitur jarak dan rute berfungsi sempurna, aplikasi didesain untuk mencegat pengguna di pintu masuk aplikasi jika Lokasi (GPS) belum menyala atau Izin Lokasi belum diberikan.
- **Deteksi Real-Time**: Menggunakan `LifecycleEventObserver` (Jetpack Compose) untuk terus memonitor status `isProviderEnabled` dan `checkSelfPermission` setiap kali layar di-resume. 
- Jika tidak valid, layar tertutup penuh oleh pesan peringatan dan satu tombol yang bisa meminta izin (*permission request*) atau melempar pengguna secara otomatis ke halaman Pengaturan GPS HP (`ACTION_LOCATION_SOURCE_SETTINGS`).

## 4. Penghapusan Konsep Akun (No User Data)
- **Eksperimen**: Sempat dibuat halaman `LoginScreen.kt` dan `RegisterScreen.kt` beserta sistem navigasinya di `ChurchFinderApp.kt`.
- **Keputusan Final**: Sesuai instruksi untuk menjaga aplikasi bersih dari pendataan _user_ (hanya sebagai direktori informasi publik), ketiga fitur tersebut (Login, Register, dan Profil) akhirnya **dicabut penuh** dan dihapus (_deleted_) dari kode aplikasi. _Splash screen_ kini dikembalikan ke rute `home`.

## 5. Perbaikan Bug (Troubleshooting) yang Diselesaikan
- **Crash WebView Parcel (NULL String)**: Memperbaiki *crash* yang disebabkan oleh argumen parameter ke-5 (`historyUrl`) bernilai `null` pada fungsi `loadDataWithBaseURL()`.
- **Blank White Map Screen**: Memperbaiki peta yang tidak muncul dengan memaksa batas layar `layoutParams = MATCH_PARENT` pada `WebView` Compose, serta memastikan pemuatan HTML dilakukan *setelah* data dari backend tiba.
- **Crash Gambar (Skia Unimplemented Decoder)**: Backend sempat mengembalikan `main_image_path` dengan nilai `null`. Tautan *placeholder fallback* dari `placehold.co` sebelumnya mengembalikan berkas **SVG** yang tidak didukung secara natif oleh _library_ Coil di Android. Diselesaikan dengan mengubah ekstensinya menjadi eksplisit `.png`.
- **PHP 8.5 JSON Parser Breaking Bug**: Backend Laravel menyisipkan pesan *error HTML* di dalam respons JSON (`Deprecated: Constant PDO::MYSQL_ATTR_SSL_CA`). Ini merusak _parser_ JSON di Android. Bug diselesaikan dengan menghapus parameter SSL bawaan MySQL yang kedaluwarsa di dalam `config/database.php` pada _backend_ Laravel.
- **Missing Compose Imports**: Memperbaiki segala *error* gagal _build_ pada Compose (`Unresolved reference` untuk Column, Spacer, Alignment, size, height) dengan meluruskan _wildcard imports_ di `ChurchFinderApp.kt`.

***

Semua kode berhasil dikompilasi ulang dan berjalan (Status: `BUILD SUCCESSFUL`).
