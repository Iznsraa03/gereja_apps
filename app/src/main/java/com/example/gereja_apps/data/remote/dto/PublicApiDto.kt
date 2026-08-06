package com.example.gereja_apps.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryDto(
    val id: Int = 0,
    val name: String,
    val slug: String = "",
    val icon_path: String? = null
)

@JsonClass(generateAdapter = true)
data class ArticleDto(
    val id: Int,
    val title: String,
    val slug: String,
    val excerpt: String? = null,
    val thumbnail_path: String? = null,
    val published_at: String? = null
)

@JsonClass(generateAdapter = true)
data class ChurchDto(
    val id: Int,
    @Json(name = "nama_gereja") val name: String,
    val slug: String,
    @Json(name = "alamat") val address: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    @Json(name = "distance_km") val distance: Double? = null,
    val gambar: List<String>? = emptyList(),
    @Json(name = "kategori") val categoryString: String? = null
) {
    val imageUrl: String?
        get() = gambar?.firstOrNull()?.let { if (it.startsWith("http")) it else "${com.example.gereja_apps.data.remote.NetworkClient.STORAGE_BASE_URL}$it" }

    val category: CategoryDto?
        get() = categoryString?.let { CategoryDto(name = it) }
}

@JsonClass(generateAdapter = true)
data class WorshipScheduleDto(
    @Json(name = "judul") val title: String,
    @Json(name = "waktu") val start_time: String,
    @Json(name = "pengkhotbah") val preacher_name: String? = null
)

@JsonClass(generateAdapter = true)
data class FacilityDto(
    val name: String
)

@JsonClass(generateAdapter = true)
data class ActivityDto(
    @Json(name = "judul") val title: String,
    @Json(name = "deskripsi") val deskripsi: String? = null,
    @Json(name = "mulai") val mulai: String? = null
)

@JsonClass(generateAdapter = true)
data class ChurchDetailDto(
    val id: Int,
    @Json(name = "nama_gereja") val name: String,
    val slug: String? = null,
    @Json(name = "alamat") val address: String? = null,
    @Json(name = "kecamatan") val city: String? = null,
    val latitude: String? = null,
    val longitude: String? = null,
    @Json(name = "deskripsi") val description: String? = null,
    val phone: String? = null,
    val website_url: String? = null,
    val gambar: List<String>? = emptyList(),
    @Json(name = "kategori") val categoryString: String? = null,
    @Json(name = "jadwal_ibadah") val schedules: List<WorshipScheduleDto>? = emptyList(),
    @Json(name = "fasilitas") val fasilitasStrings: List<String>? = emptyList(),
    @Json(name = "kegiatan_gereja") val activities: List<ActivityDto>? = emptyList()
) {
    val imageUrl: String?
        get() = gambar?.firstOrNull()?.let { if (it.startsWith("http")) it else "${com.example.gereja_apps.data.remote.NetworkClient.STORAGE_BASE_URL}$it" }

    val category: CategoryDto?
        get() = categoryString?.let { CategoryDto(name = it) }

    val facilities: List<FacilityDto>
        get() = fasilitasStrings?.map { FacilityDto(name = it) } ?: emptyList()
}
