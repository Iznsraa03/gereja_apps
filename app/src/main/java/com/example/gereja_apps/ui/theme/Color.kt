package com.example.gereja_apps.ui.theme

import androidx.compose.ui.graphics.Color

// ponytail: semantic tokens only — no raw hex scattered in screens
val Primary         = Color(0xFF004D64)
val PrimaryLight    = Color(0xFF006684)
val OnPrimary       = Color(0xFFFFFFFF)
val PrimaryContainer= Color(0xFFB8E8FA)
val OnPrimaryContainer = Color(0xFF001F2A)

val Secondary       = Color(0xFF4D6357)
val SecondaryContainer = Color(0xFFCDE6D6)
val OnSecondaryContainer = Color(0xFF0B1F17)

val AccentAmber     = Color(0xFFD97706) // distance badge, featured highlight
val AccentAmberLight= Color(0xFFFEF3C7)

val Background      = Color(0xFFF8FAFC) // Slate-50 clean canvas
val Surface         = Color(0xFFFFFFFF)
val SurfaceVariant  = Color(0xFFF1F5F9) // Slate-100
val SurfaceContainer= Color(0xFFE2E8F0) // Slate-200

val TextPrimary     = Color(0xFF0F172A) // Slate-900
val TextSecondary   = Color(0xFF475569) // Slate-600

val FavoriteRed     = Color(0xFFE11D48) // heart active
val SuccessGreen    = Color(0xFF059669)
val WarningOrange   = Color(0xFFF59E0B)
val ErrorRed        = Color(0xFFB00020)

val Outline         = Color(0xFFCBD5E1) // Slate-300
val OutlineVariant  = Color(0xFFE2E8F0)

object AppColors {
    val Success   = SuccessGreen
    val Warning   = WarningOrange
    val Amber     = AccentAmber
    val AmberBg   = AccentAmberLight
    val FavActive = FavoriteRed
}
