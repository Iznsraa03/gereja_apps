package com.example.gereja_apps.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gereja_apps.data.remote.dto.ChurchDto
import com.example.gereja_apps.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(onChurchClick: (String) -> Unit) {
    // ponytail: empty until auth layer wires favorites
    val favorites = emptyList<ChurchDto>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        TopAppBar(
            title = { Text("Favorit") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Surface,
                titleContentColor = TextPrimary
            )
        )

        if (favorites.isEmpty()) {
            FavoritesEmptyState()
        } else {
            LazyColumn(
                contentPadding      = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(favorites) { idx, church ->
                    AnimatedVisibility(
                        visible = true,
                        enter   = fadeIn(tween(280, delayMillis = idx * 60)) +
                                  slideInVertically(tween(280, delayMillis = idx * 60)) { it / 5 }
                    ) {
                        ExploreChurchCard(church = church, onClick = { onChurchClick(church.slug) })
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesEmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(40.dp)
        ) {
            // Gently pulsing heart
            val infiniteTransition = rememberInfiniteTransition(label = "heart")
            val scale by infiniteTransition.animateFloat(
                initialValue  = 0.92f,
                targetValue   = 1.08f,
                animationSpec = infiniteRepeatable(tween(1100), RepeatMode.Reverse),
                label         = "heartScale"
            )
            Icon(
                imageVector    = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint           = Outline,
                modifier       = Modifier.size(72.dp).scale(scale)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Belum ada favorit",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Simpan gereja dari halaman detail agar mudah ditemukan kembali.",
                style     = MaterialTheme.typography.bodyMedium,
                color     = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { /* ponytail: navigate to explore handled by caller */ },
                colors  = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Jelajahi Gereja")
            }
        }
    }
}
