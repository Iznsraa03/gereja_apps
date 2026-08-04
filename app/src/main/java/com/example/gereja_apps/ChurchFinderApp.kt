package com.example.gereja_apps

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import android.content.Intent
import android.provider.Settings
import com.example.gereja_apps.ui.screen.*
import com.example.gereja_apps.ui.theme.*

private data class NavItem(val route: String, val icon: ImageVector, val label: String)

private val navItems = listOf(
    NavItem("home",      Icons.Default.Home,      "Beranda"),
    NavItem("explore",   Icons.Default.Search,    "Jelajahi"),
    NavItem("favorites", Icons.Default.Favorite,  "Favorit")
)

private val topLevelRoutes = navItems.map { it.route }.toSet()

@Composable
fun ChurchFinderApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route
    val showBottomBar  = currentRoute in topLevelRoutes
    
    val context = LocalContext.current
    var hasLocationPermission by remember { 
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) 
    }
    
    val locationManager = remember { context.getSystemService(android.content.Context.LOCATION_SERVICE) as android.location.LocationManager }
    var isGpsEnabled by remember { mutableStateOf(locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                hasLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (!hasLocationPermission || !isGpsEnabled) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = Primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Akses Lokasi Dibutuhkan",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Aplikasi ini mewajibkan akses dan fitur GPS aktif untuk menemukan gereja terdekat di sekitar Anda secara real-time.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        if (!hasLocationPermission) {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        } else if (!isGpsEnabled) {
                            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary)
                ) {
                    Text(if (!hasLocationPermission) "Beri Izin Lokasi" else "Aktifkan GPS")
                }
            }
        }
        return // Block app rendering until location is enabled
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter   = slideInVertically(tween(250)) { it },
                exit    = slideOutVertically(tween(200)) { it }
            ) {
                NavigationBar(
                    containerColor = Surface,
                    tonalElevation = 0.dp
                ) {
                    navItems.forEach { item ->
                        val selected = backStackEntry?.destination
                            ?.hierarchy
                            ?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick  = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState    = true
                                }
                            },
                            icon  = { Icon(item.icon, item.label) },
                            label = { Text(item.label, style = MaterialTheme.typography.labelMedium) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor   = Primary,
                                selectedTextColor   = Primary,
                                indicatorColor      = PrimaryContainer,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController      = navController,
            startDestination   = "splash",
            modifier           = Modifier.padding(innerPadding),
            // ponytail: simple fade+slide for all transitions
            enterTransition    = {
                fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 12 }
            },
            exitTransition     = {
                fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { -it / 12 }
            },
            popEnterTransition = {
                fadeIn(tween(220)) + slideInHorizontally(tween(220)) { -it / 12 }
            },
            popExitTransition  = {
                fadeOut(tween(180)) + slideOutHorizontally(tween(180)) { it / 12 }
            }
        ) {
            composable("splash") {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen(
                    onChurchClick = { navController.navigate("church_detail/$it") },
                    onSeeAllClick = {
                        navController.navigate("explore") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                )
            }
            composable("explore") {
                ExploreScreen(onChurchClick = { navController.navigate("church_detail/$it") })
            }
            composable("favorites") {
                FavoritesScreen(onChurchClick = { navController.navigate("church_detail/$it") })
            }
            composable("church_detail/{churchId}") { back ->
                val id = back.arguments?.getString("churchId") ?: return@composable
                ChurchDetailScreen(
                    churchId     = id,
                    onBackClick  = { navController.popBackStack() },
                    onRouteClick = { navController.navigate("route_navigation/$it") }
                )
            }
            composable("route_navigation/{churchId}") { back ->
                val id = back.arguments?.getString("churchId") ?: return@composable
                RouteNavigationScreen(
                    churchId    = id,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
