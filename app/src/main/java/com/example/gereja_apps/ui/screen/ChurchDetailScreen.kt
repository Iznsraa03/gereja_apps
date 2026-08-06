package com.example.gereja_apps.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.draw.scale
import com.example.gereja_apps.data.remote.dto.ChurchDetailDto
import com.example.gereja_apps.data.remote.dto.WorshipScheduleDto
import com.example.gereja_apps.ui.theme.*
import com.example.gereja_apps.ui.theme.shimmerEffect

@Composable
fun ChurchDetailScreen(
    churchId: String,
    viewModel: ChurchDetailViewModel = viewModel(),
    onBackClick: () -> Unit,
    onRouteClick: (String) -> Unit
) {
    val church    by viewModel.churchDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(churchId) { viewModel.loadChurch(churchId) }

    when {
        isLoading  -> DetailShimmer(onBack = onBackClick)
        church == null -> DetailError(onBack = onBackClick)
        else -> DetailContent(church!!, onBackClick, onRouteClick)
    }
}

// ── Main content ────────────────────────────────────────────

@Composable
private fun DetailContent(
    church: ChurchDetailDto,
    onBack: () -> Unit,
    onRoute: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    // Parallax: hero shrinks as user scrolls
    val heroHeight by animateDpAsState(
        targetValue   = (280 - (scrollState.value * 0.15f).toInt()).coerceAtLeast(140).dp,
        animationSpec = tween(0), // ponytail: instant tracking, no lag
        label         = "heroHeight"
    )

    Box(modifier = Modifier.fillMaxSize().background(Background)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
        ) {
            // ── Hero ──────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(heroHeight)
            ) {
                AsyncImage(
                    model          = church.imageUrl
                        ?: "https://placehold.co/800x450/004D64/FFFFFF.png?text=Gereja",
                    contentDescription = church.name,
                    contentScale   = ContentScale.Crop,
                    modifier       = Modifier
                        .fillMaxSize()
                        .background(SurfaceContainer)
                )
                // Gradient scrim so top-bar icons are readable
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent, Color.Transparent)
                            )
                        )
                )
                // Back button
                IconButton(
                    onClick  = onBack,
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                        .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, "Kembali", tint = Color.White)
                }
                // ponytail: Removed favorite button because user auth is gone (YAGNI)
            }

            // ── Church Info Card ───────────────────────────────
            Surface(
                color     = Surface,
                shape     = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                modifier  = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Badges row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // ponytail: verification_status removed as it's not in Api_Docs.md
                        church.category?.let { cat ->
                            Surface(
                                color = SurfaceVariant,
                                shape = CircleShape
                            ) {
                                Text(
                                    cat.name,
                                    style    = MaterialTheme.typography.labelSmall,
                                    color    = TextSecondary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                        // ponytail: distance not in ChurchDetailDto, shown on list cards only
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(church.name,
                        style = MaterialTheme.typography.headlineMedium)

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.LocationOn, null,
                            tint = TextSecondary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(
                            church.address ?: "Alamat tidak tersedia",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    church.description?.let { desc ->
                        Spacer(Modifier.height(16.dp))
                        Text(desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary)
                    }

                    // ponytail: Action buttons (Call/Share) removed as phone is not in API docs and sharing is YAGNI

                    // ── Jadwal Ibadah ──────────────────────────
                    if (!church.schedules.isNullOrEmpty()) {
                        Spacer(Modifier.height(28.dp))
                        DetailSectionTitle("Jadwal Ibadah")
                        Spacer(Modifier.height(12.dp))
                        church.schedules.forEach { schedule ->
                            ScheduleItem(schedule)
                            Spacer(Modifier.height(10.dp))
                        }
                    }

                    // ── Fasilitas ──────────────────────────────
                    if (!church.facilities.isNullOrEmpty()) {
                        Spacer(Modifier.height(20.dp))
                        DetailSectionTitle("Fasilitas")
                        Spacer(Modifier.height(12.dp))
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement   = Arrangement.spacedBy(8.dp)
                        ) {
                            church.facilities.forEach { f ->
                                Surface(
                                    color  = SurfaceVariant,
                                    shape  = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier          = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.CheckCircle, null,
                                            tint = SuccessGreen, modifier = Modifier.size(15.dp))
                                        Spacer(Modifier.width(6.dp))
                                        Text(f.name, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }

                    // ── Kegiatan ──────────────────────────────
                    if (!church.activities.isNullOrEmpty()) {
                        Spacer(Modifier.height(20.dp))
                        DetailSectionTitle("Kegiatan")
                        Spacer(Modifier.height(12.dp))
                        church.activities.forEach { activity ->
                            Surface(
                                color = SurfaceVariant,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Event, null, tint = Primary, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(12.dp))
                                    Text(activity.title, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }

                    // ponytail: Contact section removed as phone and website_url are not in API docs

                    Spacer(Modifier.height(96.dp)) // room for sticky button
                }
            }
        }

        // ── Sticky "Buka Rute" button ──────────────────────────
        Button(
            onClick  = { onRoute(church.slug ?: "") },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .height(52.dp),
            shape  = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(containerColor = Primary)
        ) {
            Icon(Icons.Default.Directions, null)
            Spacer(Modifier.width(10.dp))
            Text("Buka Rute Peta", style = MaterialTheme.typography.labelLarge) // ponytail: removed gmaps mention
        }
    }
}

// ponytail: AnimatedFavoriteButton removed completely

// ── Small helpers ───────────────────────────────────────────

@Composable
private fun DetailSectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge)
}

@Composable
private fun ScheduleItem(schedule: WorshipScheduleDto) {
    // ponytail: simplified ScheduleItem, removed expansion and preacher_name as it's not in API docs
    Surface(
        color     = SurfaceVariant,
        shape     = MaterialTheme.shapes.large,
        modifier  = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier              = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(schedule.title, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, null,
                        tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        schedule.start_time, // ponytail: removed day_of_week
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

// ── Loading & Error states ──────────────────────────────────

@Composable
private fun DetailShimmer(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(Background)) {
        Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
            Box(modifier = Modifier.fillMaxSize().shimmerEffect())
            IconButton(
                onClick  = onBack,
                modifier = Modifier.padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, "Kembali", tint = Color.White)
            }
        }
        Column(modifier = Modifier.padding(20.dp)) {
            Box(Modifier.fillMaxWidth(0.5f).height(14.dp).clip(CircleShape).shimmerEffect())
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth(0.8f).height(22.dp).clip(CircleShape).shimmerEffect())
            Spacer(Modifier.height(10.dp))
            Box(Modifier.fillMaxWidth().height(14.dp).clip(CircleShape).shimmerEffect())
            Spacer(Modifier.height(6.dp))
            Box(Modifier.fillMaxWidth(0.7f).height(14.dp).clip(CircleShape).shimmerEffect())
        }
    }
}

@Composable
private fun DetailError(onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
            Icon(Icons.Default.WifiOff, null,
                tint = Outline, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Gereja tidak ditemukan",
                style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedButton(onClick = onBack) { Text("Kembali") }
        }
    }
}
