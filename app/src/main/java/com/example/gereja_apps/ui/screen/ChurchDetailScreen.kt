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
    var isFavorite by remember { mutableStateOf(false) }
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
                    model          = church.main_image_path
                        ?: "https://placehold.co/800x450/004D64/FFFFFF.png?text=Gereja",
                    contentDescription = null,
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
                // Favorite button — top right
                AnimatedFavoriteButton(
                    isFavorite = isFavorite,
                    onClick    = { isFavorite = !isFavorite },
                    modifier   = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                )
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
                        if (church.verification_status == "verified") {
                            Surface(
                                color = SuccessGreen.copy(alpha = 0.1f),
                                shape = CircleShape
                            ) {
                                Row(
                                    modifier          = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
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
                            "${church.address}, ${church.city}",
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

                    // ── Action buttons ─────────────────────────
                    Spacer(Modifier.height(20.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        church.phone?.let { phone ->
                            OutlinedButton(
                                onClick = { /* dial intent */ },
                                modifier = Modifier.weight(1f),
                                border   = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 1.5.dp
                                )
                            ) {
                                Icon(Icons.Default.Call, null, Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Telepon")
                            }
                        }
                        OutlinedButton(
                            onClick  = { /* share */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Bagikan")
                        }
                    }

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

                    // ── Kontak ─────────────────────────────────
                    if (church.phone != null || church.website_url != null) {
                        Spacer(Modifier.height(20.dp))
                        DetailSectionTitle("Kontak")
                        Spacer(Modifier.height(12.dp))
                        Surface(
                            color    = SurfaceVariant,
                            shape    = MaterialTheme.shapes.large,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                church.phone?.let {
                                    ContactRow(Icons.Default.Call, it)
                                }
                                church.website_url?.let {
                                    if (church.phone != null) Spacer(Modifier.height(14.dp))
                                    ContactRow(Icons.Default.Public, it)
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(96.dp)) // room for sticky button
                }
            }
        }

        // ── Sticky "Buka Rute" button ──────────────────────────
        Button(
            onClick  = { onRoute(church.slug) },
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
            Text("Buka Rute di Google Maps", style = MaterialTheme.typography.labelLarge)
        }
    }
}

// ── Animated heart button ───────────────────────────────────

@Composable
private fun AnimatedFavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue    = if (isFavorite) 1.25f else 1f,
        animationSpec  = spring(Spring.DampingRatioHighBouncy, Spring.StiffnessMedium),
        label          = "heartScale",
        finishedListener = { /* scale back */ }
    )
    val tintColor by animateColorAsState(
        targetValue   = if (isFavorite) FavoriteRed else Color.White,
        animationSpec = tween(200),
        label         = "heartColor"
    )

    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector    = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorit",
            tint           = tintColor,
            modifier       = Modifier.scale(scale)
        )
    }
}

// ── Small helpers ───────────────────────────────────────────

@Composable
private fun DetailSectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleLarge)
}

@Composable
private fun ScheduleItem(schedule: WorshipScheduleDto) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color     = SurfaceVariant,
        shape     = MaterialTheme.shapes.large,
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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
                            "Hari ${schedule.day_of_week} · ${schedule.start_time}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                Icon(
                    imageVector    = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint           = TextSecondary
                )
            }
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    schedule.preacher_name?.let { p ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, null,
                                tint = TextSecondary, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(p, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    // ponytail: WorshipScheduleDto has no description field
                }
            }
        }
    }
}

@Composable
private fun ContactRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(color = PrimaryContainer, shape = CircleShape) {
            Icon(icon, null,
                tint     = Primary,
                modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
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
