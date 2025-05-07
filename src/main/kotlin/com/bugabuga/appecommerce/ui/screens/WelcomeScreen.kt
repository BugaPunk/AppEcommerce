package com.bugabuga.appecommerce.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bugabuga.appecommerce.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val userRepository = remember { UserRepository() }
        val coroutineScope = rememberCoroutineScope()
        
        val currentUser by userRepository.currentUser.collectAsState()
        
        // Auto-navigate to HomeScreen after 3 seconds
        LaunchedEffect(Unit) {
            delay(3000)
            navigator.replace(HomeScreen())
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido${currentUser?.nombre?.let { ", $it" } ?: ""}!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Gracias por iniciar sesión en nuestra tienda.",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = { navigator.replace(HomeScreen()) }
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home"
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text("Ir a la Tienda")
            }
        }
    }
}