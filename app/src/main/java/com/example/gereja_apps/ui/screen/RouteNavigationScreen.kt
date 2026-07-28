package com.example.gereja_apps.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RouteNavigationScreen(
    churchId: String,
    viewModel: ChurchDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val church by viewModel.churchDetail.collectAsState()
    
    LaunchedEffect(churchId) {
        if (church?.slug != churchId) {
            viewModel.loadChurch(churchId)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Map Background
        AsyncImage(
            model = "https://placehold.co/800x800?text=Map+Route", // ponytail: map mockup
            contentDescription = "Map",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().background(Color(0xFFE5E3DF))
        )
        
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, CircleShape)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        
        // FABs
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .padding(top = 48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { /* ponytail */ },
                modifier = Modifier.background(Color.White, CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = "Location")
            }
            IconButton(
                onClick = { /* ponytail */ },
                modifier = Modifier.background(Color.White, CircleShape).size(40.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Layers")
            }
        }
        
        // Bottom Sheet Card (ponytail: fake bottom sheet)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Drag handle
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(4.dp)
                        .background(Color(0xFFE0E3E5), RoundedCornerShape(2.dp))
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = church?.name ?: "Loading...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "8 min • 2.4 km • Fastest route",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF004D64) // Primary
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Next step
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF2F4F6), RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF006684),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Next Step • In 200m", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("Turn right onto Jl. Kajaolalido", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { /* ponytail */ },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D64))
                        ) {
                            Text("Start Navigation")
                        }
                        OutlinedButton(
                            onClick = { /* ponytail */ },
                            modifier = Modifier.size(48.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Info, contentDescription = "Preview")
                        }
                    }
                }
            }
        }
    }
}
