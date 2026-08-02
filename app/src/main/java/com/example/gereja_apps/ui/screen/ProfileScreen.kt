package com.example.gereja_apps.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.gereja_apps.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val currentUser  by viewModel.currentUser.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        AnimatedContent(
            targetState  = currentUser,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label        = "profileState"
        ) { user ->
            if (user == null) {
                LoginForm(
                    isLoading    = isLoading,
                    errorMessage = errorMessage,
                    onLogin      = { email, pw -> viewModel.login(email, pw) }
                )
            } else {
                AuthenticatedProfile(
                    name    = user.name,
                    email   = user.email,
                    onLogout = { viewModel.logout() }
                )
            }
        }
    }
}

// ── Login form ──────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginForm(
    isLoading: Boolean,
    errorMessage: String?,
    onLogin: (String, String) -> Unit
) {
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier            = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header banner
        Box(
            modifier         = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Brush.verticalGradient(listOf(Primary, PrimaryLight))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier         = Modifier
                        .size(72.dp)
                        .background(OnPrimary.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null,
                        tint = OnPrimary, modifier = Modifier.size(36.dp))
                }
                Spacer(Modifier.height(12.dp))
                Text("Masuk ke Akun", style = MaterialTheme.typography.titleLarge,
                    color = OnPrimary, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value         = email,
                onValueChange = { email = it },
                label         = { Text("Email") },
                leadingIcon   = { Icon(Icons.Default.Email, null, tint = Primary) },
                singleLine    = true,
                modifier      = Modifier.fillMaxWidth(),
                shape         = MaterialTheme.shapes.medium
            )
            OutlinedTextField(
                value                = password,
                onValueChange        = { password = it },
                label                = { Text("Password") },
                leadingIcon          = { Icon(Icons.Default.Lock, null, tint = Primary) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine           = true,
                modifier             = Modifier.fillMaxWidth(),
                shape                = MaterialTheme.shapes.medium
            )

            AnimatedVisibility(visible = errorMessage != null) {
                Surface(
                    color  = ErrorRed.copy(alpha = 0.1f),
                    shape  = MaterialTheme.shapes.small
                ) {
                    Text(
                        errorMessage ?: "",
                        color    = ErrorRed,
                        style    = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick  = { onLogin(email, password) },
                enabled  = !isLoading && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = MaterialTheme.shapes.medium,
                colors   = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color  = OnPrimary,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Masuk", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

// ── Authenticated profile ───────────────────────────────────

@Composable
private fun AuthenticatedProfile(
    name: String,
    email: String,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Avatar header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Primary, PrimaryLight)))
                .padding(vertical = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainer)
                ) {
                    AsyncImage(
                        model              = "https://placehold.co/200x200/FFFFFF/004D64.png?text=${name.take(1)}",
                        contentDescription = "Avatar",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize()
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(name,  style = MaterialTheme.typography.titleLarge,
                    color = OnPrimary, fontWeight = FontWeight.Bold)
                Text(email, style = MaterialTheme.typography.bodyMedium,
                    color = OnPrimary.copy(alpha = 0.75f))
            }
        }

        Spacer(Modifier.height(8.dp))

        // Menu items
        Surface(color = Surface, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileMenuItem("Preferensi Kategori", Icons.Default.List)         {}
                ProfileMenuItem("Pengingat Jadwal",   Icons.Default.Notifications){}
                ProfileMenuItem("Tentang Aplikasi",   Icons.Default.Info)         {}
                ProfileMenuItem("Kebijakan Privasi",  Icons.Default.Security)      {}
            }
        }

        Spacer(Modifier.height(8.dp))

        Surface(color = Surface, modifier = Modifier.fillMaxWidth()) {
            ProfileMenuItem(
                title       = "Keluar",
                icon        = Icons.AutoMirrored.Filled.ExitToApp,
                isDestructive = true,
                onClick     = onLogout
            )
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun ProfileMenuItem(
    title: String,
    icon: ImageVector,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val fg = if (isDestructive) ErrorRed else TextPrimary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color  = if (isDestructive) ErrorRed.copy(alpha = 0.1f) else SurfaceVariant,
            shape  = CircleShape
        ) {
            Icon(icon, null, tint = fg, modifier = Modifier.padding(8.dp).size(18.dp))
        }
        Spacer(Modifier.width(16.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, color = fg, modifier = Modifier.weight(1f))
        if (!isDestructive) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Outline, modifier = Modifier.size(16.dp))
        }
    }
}
