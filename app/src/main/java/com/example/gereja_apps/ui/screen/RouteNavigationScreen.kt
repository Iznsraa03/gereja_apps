package com.example.gereja_apps.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("SetJavaScriptEnabled")
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
    
    // Default location (Makassar) if not loaded yet
    val lat = church?.latitude?.toDoubleOrNull() ?: -5.147665
    val lng = church?.longitude?.toDoubleOrNull() ?: 119.432731
    
    val safeName = church?.name?.replace("\"", "\\\"")?.replace("\n", " ") ?: "Lokasi"
    val safeAddress = church?.address?.replace("\"", "\\\"")?.replace("\n", " ") ?: ""
    
    val htmlContent = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                body, html { margin: 0; padding: 0; width: 100%; height: 100%; }
                #map { width: 100%; height: 100%; }
                .leaflet-control-attribution { display: none; }
            </style>
        </head>
        <body>
            <div id="map"></div>
            <script>
                var map = L.map('map', {zoomControl: false}).setView([$lat, $lng], 16);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                    maxZoom: 19
                }).addTo(map);
                var name = "$safeName";
                var address = "$safeAddress";
                var marker = L.marker([$lat, $lng]).addTo(map);
                marker.bindPopup("<b>" + name + "</b><br>" + address).openPopup();
            </script>
        </body>
        </html>
    """.trimIndent()

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    webViewClient = WebViewClient()
                    webChromeClient = WebChromeClient()
                    
                    if (church != null) {
                        tag = church?.slug
                        loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", "")
                    }
                }
            },
            update = { webView ->
                if (church != null && webView.tag != church?.slug) {
                    webView.tag = church?.slug
                    webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", "")
                }
            }
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
        
        // Bottom Sheet Card (Info Rute)
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
                        text = church?.name ?: "Memuat Lokasi...",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Peta OpenStreetMap (Leaflet JS)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF004D64)
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    val context = androidx.compose.ui.platform.LocalContext.current
                    Button(
                        onClick = { 
                            val uri = android.net.Uri.parse("geo:$lat,$lng?q=$lat,$lng($safeName)")
                            val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, uri)
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Ignore or fallback
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF004D64))
                    ) {
                        Icon(Icons.Default.Directions, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Buka di Aplikasi Maps")
                    }
                }
            }
        }
    }
}
