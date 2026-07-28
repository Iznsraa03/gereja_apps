package com.example.gereja_apps.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gereja_apps.ui.theme.OnPrimary
import com.example.gereja_apps.ui.theme.Primary
import com.example.gereja_apps.ui.theme.PrimaryLight
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHome: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onNavigateToHome()
    }

    // Animated scale entrance for logo
    val scale = remember { Animatable(0.6f) }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f, tween(500))
        scale.animateTo(1f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Primary, PrimaryLight))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scale.value)
        ) {
            // Logo placeholder circle
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(OnPrimary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✝",
                    fontSize = 48.sp,
                    color = OnPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Church Finder",
                style = MaterialTheme.typography.headlineMedium,
                color = OnPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Makassar",
                style = MaterialTheme.typography.titleMedium,
                color = OnPrimary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Pulsing dots loader
            PulsingDots()
        }
    }
}

@Composable
private fun PulsingDots() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(0, 150, 300).forEach { delay ->
            val alpha by infiniteTransition.animateFloat(
                initialValue   = 0.3f,
                targetValue    = 1f,
                animationSpec  = infiniteRepeatable(
                    animation = tween(600, delayMillis = delay, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "dot$delay"
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(OnPrimary.copy(alpha = alpha))
            )
        }
    }
}
