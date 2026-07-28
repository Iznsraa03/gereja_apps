package com.example.gereja_apps.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gereja_apps.data.remote.dto.CategoryDto
import com.example.gereja_apps.data.remote.dto.ChurchDto
import com.example.gereja_apps.ui.theme.*
import com.example.gereja_apps.ui.theme.shimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onChurchClick: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    val categories     by viewModel.categories.collectAsState()
    val nearbyChurches by viewModel.nearbyChurches.collectAsState()
    val articles       by viewModel.articles.collectAsState()
    val isLoading      by viewModel.isLoading.collectAsState()

    var searchQuery     by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Header ─────────────────────────────────────────────
        HomeHeader()

        Spacer(Modifier.height(16.dp))

        // ── Floating Search Bar ────────────────────────────────
        SearchBar(
            query    = searchQuery,
            onChange = { searchQuery = it },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(20.dp))

        // ── Category Chips ─────────────────────────────────────
        if (categories.isNotEmpty()) {
            LazyRow(
                contentPadding         = PaddingValues(horizontal = 16.dp),
                horizontalArrangement  = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    AnimatedCategoryChip(
                        category   = cat,
                        isSelected = cat.slug == selectedCategory,
                        onClick    = {
                            selectedCategory = if (selectedCategory == cat.slug) null else cat.slug
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Nearby Section ─────────────────────────────────────
        SectionHeader(title = "Gereja Terdekat", actionLabel = "Lihat Semua", onAction = onSeeAllClick)
        Spacer(Modifier.height(12.dp))

        if (isLoading) {
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) { ChurchCardShimmer() }
            }
        } else {
            LazyRow(
                contentPadding        = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(nearbyChurches) { idx, church ->
                    AnimatedVisibility(
                        visible      = true,
                        enter        = fadeIn(tween(300, delayMillis = idx * 80)) +
                                       slideInHorizontally(tween(300, delayMillis = idx * 80)) { it / 4 }
                    ) {
                        ChurchCard(church = church, onClick = { onChurchClick(church.slug) })
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // ── Announcements ──────────────────────────────────────
        SectionHeader(title = "Pengumuman Terbaru")
        Spacer(Modifier.height(12.dp))

        Column(
            modifier              = Modifier.padding(horizontal = 16.dp),
            verticalArrangement   = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                repeat(2) { AnnouncementShimmer() }
            } else {
                articles.forEachIndexed { idx, article ->
                    val isFeatured = idx == 0
                    AnnouncementCard(
                        title    = article.title,
                        excerpt  = article.excerpt ?: "",
                        featured = isFeatured
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
    }
}

// ── Sub-components ──────────────────────────────────────────

@Composable
private fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Primary, PrimaryLight)))
            .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 28.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Pulsing location dot
                val infiniteTransition = rememberInfiniteTransition(label = "loc")
                val pulse by infiniteTransition.animateFloat(
                    initialValue  = 0.7f, targetValue = 1.3f,
                    animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
                    label         = "pulse"
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .scale(pulse)
                        .clip(CircleShape)
                        .background(Color(0xFF6EE7B7)) // emerald-300
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text  = "Makassar, Sulawesi Selatan",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnPrimary.copy(alpha = 0.85f)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text  = "Shalom! 🙏",
                style = MaterialTheme.typography.headlineMedium,
                color = OnPrimary
            )
            Text(
                text  = "Temukan gereja di sekitar Anda",
                style = MaterialTheme.typography.bodyMedium,
                color = OnPrimary.copy(alpha = 0.75f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier      = modifier
            .fillMaxWidth()
            .shadow(elevation = 4.dp, shape = CircleShape, clip = false),
        shape         = CircleShape,
        color         = Surface,
        tonalElevation = 0.dp
    ) {
        TextField(
            value           = query,
            onValueChange   = onChange,
            placeholder     = { Text("Cari gereja…", color = TextSecondary) },
            leadingIcon     = { Icon(Icons.Default.Search, null, tint = Primary) },
            trailingIcon    = if (query.isNotEmpty()) ({
                IconButton(onClick = { onChange("") }) {
                    Icon(Icons.Default.Clear, null, tint = TextSecondary)
                }
            }) else null,
            singleLine      = true,
            colors          = TextFieldDefaults.colors(
                focusedContainerColor    = Color.Transparent,
                unfocusedContainerColor  = Color.Transparent,
                focusedIndicatorColor    = Color.Transparent,
                unfocusedIndicatorColor  = Color.Transparent
            )
        )
    }
}

@Composable
private fun AnimatedCategoryChip(
    category: CategoryDto,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue    = if (isSelected) Primary else SurfaceVariant,
        animationSpec  = tween(220),
        label          = "chipBg"
    )
    val textColor by animateColorAsState(
        targetValue    = if (isSelected) OnPrimary else TextPrimary,
        animationSpec  = tween(220),
        label          = "chipText"
    )

    Surface(
        shape    = CircleShape,
        color    = bgColor,
        modifier = Modifier
            .clickable(onClick = onClick)
            .shadow(if (isSelected) 2.dp else 0.dp, CircleShape)
    ) {
        Text(
            text      = category.name,
            color     = textColor,
            style     = MaterialTheme.typography.labelLarge,
            modifier  = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
        )
    }
}

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text  = title,
            style = MaterialTheme.typography.titleLarge
        )
        if (actionLabel != null && onAction != null) {
            Text(
                text      = actionLabel,
                style     = MaterialTheme.typography.labelLarge,
                color     = Primary,
                modifier  = Modifier.clickable(onClick = onAction)
            )
        }
    }
}

// ── Church Card with spring press scale ────────────────────

@Composable
fun ChurchCard(church: ChurchDto, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed         by interactionSource.collectIsPressedAsState()
    val scale             by animateFloatAsState(
        targetValue    = if (isPressed) 0.97f else 1f,
        animationSpec  = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label          = "cardScale"
    )

    Card(
        modifier   = Modifier
            .width(270.dp)
            .scale(scale)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        shape      = MaterialTheme.shapes.large,
        colors     = CardDefaults.cardColors(containerColor = Surface),
        elevation  = CardDefaults.cardElevation(defaultElevation = 2.dp, pressedElevation = 6.dp)
    ) {
        Column {
            Box {
                AsyncImage(
                    model          = "https://placehold.co/400x200/004D64/FFFFFF?text=Gereja",
                    contentDescription = church.name,
                    contentScale   = ContentScale.Crop,
                    modifier       = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(SurfaceContainer)
                )
                // Category badge
                church.category?.let { cat ->
                    Surface(
                        color     = Primary.copy(alpha = 0.88f),
                        shape     = RoundedCornerShape(bottomEnd = 8.dp),
                        modifier  = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            cat.name,
                            color     = OnPrimary,
                            style     = MaterialTheme.typography.labelSmall,
                            modifier  = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                // Favorite icon
                Icon(
                    imageVector    = Icons.Default.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint           = Color.White,
                    modifier       = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .size(22.dp)
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text     = church.name,
                        style    = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (church.distance != null) {
                        DistanceBadge(church.distance)
                    }
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null,
                        tint = TextSecondary, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(2.dp))
                    Text(
                        "${church.address}, ${church.city}",
                        style    = MaterialTheme.typography.bodySmall,
                        color    = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun DistanceBadge(distance: Double) {
    val text = String.format("%.1f km", distance)
    Surface(
        color    = AccentAmberLight,
        shape    = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(start = 6.dp)
    ) {
        Text(
            text     = text,
            style    = MaterialTheme.typography.labelSmall,
            color    = AccentAmber,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}

// ── Shimmer placeholders ────────────────────────────────────

@Composable
fun ChurchCardShimmer() {
    Card(
        modifier  = Modifier.width(270.dp),
        shape     = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .shimmerEffect())
            Column(modifier = Modifier.padding(12.dp)) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(16.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect())
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(12.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect())
            }
        }
    }
}

@Composable
fun ExploreCardShimmer() {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape     = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .shimmerEffect())
            Column(modifier = Modifier.padding(16.dp)) {
                Box(modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(18.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect())
                Spacer(Modifier.height(10.dp))
                Box(modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(13.dp)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .shimmerEffect())
            }
        }
    }
}

@Composable
private fun AnnouncementShimmer() {
    Surface(shape = MaterialTheme.shapes.large, color = SurfaceVariant, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).shimmerEffect())
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(14.dp).clip(MaterialTheme.shapes.extraSmall).shimmerEffect())
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth().height(11.dp).clip(MaterialTheme.shapes.extraSmall).shimmerEffect())
            }
        }
    }
}

@Composable
private fun AnnouncementCard(title: String, excerpt: String, featured: Boolean) {
    val bg        = if (featured) Primary else SurfaceVariant
    val titleColor = if (featured) OnPrimary else TextPrimary
    val subColor   = if (featured) OnPrimary.copy(alpha = 0.75f) else TextSecondary

    Surface(
        shape    = MaterialTheme.shapes.large,
        color    = bg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier         = Modifier
                    .size(42.dp)
                    .background(titleColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, null, tint = titleColor, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title,  fontWeight = FontWeight.SemiBold, color = titleColor,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (excerpt.isNotBlank()) {
                    Text(excerpt, style = MaterialTheme.typography.bodySmall, color = subColor,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            Icon(Icons.Default.ArrowForward, null, tint = titleColor.copy(alpha = 0.7f))
        }
    }
}
