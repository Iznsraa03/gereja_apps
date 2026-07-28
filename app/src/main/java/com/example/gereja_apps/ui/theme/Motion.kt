package com.example.gereja_apps.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

// ponytail: one file for all shared motion helpers

/** Spring spec for card/button press — bouncy but not excessive */
fun motionSpring() = spring<Float>(
    dampingRatio = Spring.DampingRatioMediumBouncy,
    stiffness    = Spring.StiffnessLow
)

/** Standard enter tween (220 ms) */
fun motionTween() = tween<Float>(durationMillis = 220, easing = FastOutSlowInEasing)

/** Shimmer brush modifier — attach to any placeholder Box */
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateX by transition.animateFloat(
        initialValue   = -1000f,
        targetValue    = 1000f,
        animationSpec  = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label          = "shimmerX"
    )
    val brush = Brush.linearGradient(
        colors  = listOf(
            SurfaceContainer,
            SurfaceVariant,
            SurfaceContainer,
        ),
        start   = Offset(translateX - 500f, 0f),
        end     = Offset(translateX + 500f, 0f)
    )
    background(brush)
}
