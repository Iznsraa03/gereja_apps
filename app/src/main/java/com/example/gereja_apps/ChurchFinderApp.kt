package com.example.gereja_apps

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
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
import com.example.gereja_apps.ui.screen.*
import com.example.gereja_apps.ui.theme.*

private data class NavItem(val route: String, val icon: ImageVector, val label: String)

private val navItems = listOf(
    NavItem("home",      Icons.Default.Home,      "Beranda"),
    NavItem("explore",   Icons.Default.Search,    "Jelajahi"),
    NavItem("favorites", Icons.Default.Favorite,  "Favorit"),
    NavItem("profile",   Icons.Default.Person,    "Profil"),
)

private val topLevelRoutes = navItems.map { it.route }.toSet()

@Composable
fun ChurchFinderApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route
    val showBottomBar  = currentRoute in topLevelRoutes

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
            composable("profile") {
                ProfileScreen()
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
