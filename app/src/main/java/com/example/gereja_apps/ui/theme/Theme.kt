package com.example.gereja_apps.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.view.WindowCompat

// ponytail: light-only as per requirements; dark mode skipped
private val LightColors = lightColorScheme(
    primary             = Primary,
    onPrimary           = OnPrimary,
    primaryContainer    = PrimaryContainer,
    onPrimaryContainer  = OnPrimaryContainer,
    secondary           = Secondary,
    onSecondary         = OnPrimary,
    secondaryContainer  = SecondaryContainer,
    onSecondaryContainer= OnSecondaryContainer,
    background          = Background,
    onBackground        = TextPrimary,
    surface             = Surface,
    onSurface           = TextPrimary,
    surfaceVariant      = SurfaceVariant,
    onSurfaceVariant    = TextSecondary,
    outline             = Outline,
    outlineVariant      = OutlineVariant,
    error               = ErrorRed,
    onError             = OnPrimary,
)

// ponytail: consistent shapes baked into theme — screens don't need to specify
private val AppShapes = Shapes(
    extraSmall  = RoundedCornerShape(4.dp),
    small       = RoundedCornerShape(8.dp),
    medium      = RoundedCornerShape(12.dp),
    large       = RoundedCornerShape(16.dp),
    extraLarge  = RoundedCornerShape(28.dp),
)

private val AppTypography = Typography(
    headlineMedium  = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold,   letterSpacing = (-0.5).sp),
    headlineSmall   = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    titleLarge      = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
    titleMedium     = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge       = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal,  lineHeight = 24.sp),
    bodyMedium      = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal,  lineHeight = 20.sp),
    bodySmall       = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal,  lineHeight = 16.sp),
    labelLarge      = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium,  letterSpacing = 0.1.sp),
    labelMedium     = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
    labelSmall      = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Bold,    letterSpacing = 0.5.sp),
)

@Composable
fun ChurchFinderTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(
        colorScheme = LightColors,
        typography  = AppTypography,
        shapes      = AppShapes,
        content     = content
    )
}
