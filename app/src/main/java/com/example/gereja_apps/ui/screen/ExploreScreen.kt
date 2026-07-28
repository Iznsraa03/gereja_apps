package com.example.gereja_apps.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gereja_apps.data.remote.dto.ChurchDto
import com.example.gereja_apps.ui.theme.*
import com.example.gereja_apps.ui.theme.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = viewModel(),
    onChurchClick: (String) -> Unit
) {
    val churches  by viewModel.churches.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var searchQuery      by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // ponytail: static filter labels — real filtering wired through VM later
    val filters = listOf("Semua", "Toraja", "Pentakosta", "Katolik", "Advent", "Kibaid")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // ── Sticky top bar ─────────────────────────────────────
        Surface(
            shadowElevation = 2.dp,
            color           = Surface
        ) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Spacer(Modifier.height(12.dp))
                SearchBar(
                    query    = searchQuery,
                    onChange = { searchQuery = it },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(10.dp))
                LazyRow(
                    contentPadding        = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filters) { label ->
                        val active = label == (selectedCategory ?: "Semua")
                        val bg by animateColorAsState(
                            if (active) Primary else SurfaceVariant, tween(200), label = "exCat"
                        )
                        val tc by animateColorAsState(
                            if (active) OnPrimary else TextPrimary, tween(200), label = "exCatT"
                        )
                        Surface(
                            shape    = CircleShape,
                            color    = bg,
                            modifier = Modifier.clickable {
                                selectedCategory = if (label == "Semua") null else label
                            }
                        ) {
                            Text(
                                label,
                                style    = MaterialTheme.typography.labelLarge,
                                color    = tc,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // ── Results count row ──────────────────────────────────
        AnimatedContent(
            targetState   = churches.size,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label         = "count"
        ) { count ->
            Row(
                modifier              = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    "$count gereja ditemukan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.clickable { /* sort sheet */ }
                ) {
                    Icon(Icons.Default.FilterList, null, tint = Primary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Terdekat", style = MaterialTheme.typography.labelLarge, color = Primary)
                }
            }
        }

        // ── Church list ────────────────────────────────────────
        if (isLoading) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(4) { ExploreCardShimmer() }
            }
        } else if (churches.isEmpty()) {
            ExploreEmptyState()
        } else {
            LazyColumn(
                contentPadding        = PaddingValues(bottom = 24.dp),
                verticalArrangement   = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(churches) { idx, church ->
                    AnimatedVisibility(
                        visible      = true,
                        enter        = fadeIn(tween(280, delayMillis = idx * 60)) +
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
fun ExploreChurchCard(church: ChurchDto, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed         by interactionSource.collectIsPressedAsState()
    val scale             by animateFloatAsState(
        if (isPressed) 0.98f else 1f,
        spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "exCardScale"
    )

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        shape     = MaterialTheme.shapes.large,
        colors    = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model          = "https://placehold.co/800x350/004D64/FFFFFF?text=Gereja",
                    contentDescription = church.name,
                    contentScale   = ContentScale.Crop,
                    modifier       = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(SurfaceContainer)
                )
                // Category badge
                church.category?.let { cat ->
                    Surface(
                        color    = Primary.copy(alpha = 0.88f),
                        shape    = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            cat.name, color = OnPrimary,
                            style    = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
                // Favorite
                Icon(
                    Icons.Default.FavoriteBorder, "Favorit",
                    tint     = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(22.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top
                ) {
                    Text(
                        church.name,
                        style    = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    if (church.distance != null) {
                        Spacer(Modifier.width(8.dp))
                        DistanceBadge(church.distance)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null,
                        tint = TextSecondary, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${church.address}, ${church.city}",
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                // Verification badge
                if (church.verification_status == "verified") {
                    Spacer(Modifier.height(8.dp))
                    Surface(
                        color  = SuccessGreen.copy(alpha = 0.1f),
                        shape  = CircleShape
                    ) {
                        Row(
                            modifier          = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, null,
                                tint = SuccessGreen, modifier = Modifier.size(13.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Terverifikasi",
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessGreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExploreEmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier.padding(32.dp)
        ) {
            val infiniteTransition = rememberInfiniteTransition(label = "empty")
            val scale by infiniteTransition.animateFloat(
                0.95f, 1.05f,
                infiniteRepeatable(tween(1200), RepeatMode.Reverse),
                label = "emptyScale"
            )
            Icon(
                Icons.Default.ManageSearch, null,
                tint     = Outline,
                modifier = Modifier.size(72.dp).scale(scale)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Gereja tidak ditemukan",
                style      = MaterialTheme.typography.titleMedium,
                color      = TextPrimary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Coba ubah kata pencarian atau filter yang digunakan.",
                style      = MaterialTheme.typography.bodyMedium,
                color      = TextSecondary,
                textAlign  = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

