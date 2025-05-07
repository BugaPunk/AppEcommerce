package com.bugabuga.appecommerce.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bugabuga.appecommerce.model.Review
import com.bugabuga.appecommerce.model.ReviewResponse
import com.bugabuga.appecommerce.network.ReviewService
import com.bugabuga.appecommerce.repository.UserRepository
import kotlinx.coroutines.launch

class ReviewsScreen(private val productId: Int? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val reviewService = remember { ReviewService() }
        val userRepository = remember { UserRepository() }
        val coroutineScope = rememberCoroutineScope()
        
        val currentUser by userRepository.currentUser.collectAsState()
        
        var reviews by remember { mutableStateOf<List<Review>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(Unit) {
            isLoading = true
            errorMessage = null
            
            try {
                val response: ReviewResponse = if (productId != null) {
                    reviewService.getProductReviews(productId)
                } else if (currentUser != null) {
                    reviewService.getUserReviews(currentUser!!.id!!)
                } else {
                    ReviewResponse(
                        reseñas = emptyList(),
                        currentPage = 0,
                        totalItems = 0,
                        totalPages = 0
                    )
                }
                
                reviews = response.reseñas
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar reseñas: ${e.message}"
                isLoading = false
            }
        }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { 
                        Text(
                            if (productId != null) "Reseñas del Producto" else "Mis Reseñas"
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (errorMessage != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { navigator.pop() }
                        ) {
                            Text("Volver")
                        }
                    }
                } else if (reviews.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (productId != null) 
                                "Este producto aún no tiene reseñas" 
                            else 
                                "No has realizado ninguna reseña",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { navigator.pop() }
                        ) {
                            Text("Volver")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(reviews) { review ->
                            ReviewCard(review)
                        }
                    }
                }
            }
        }
    }
    
    @Composable
    fun ReviewCard(review: Review) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${review.usuario?.nombre ?: ""} ${review.usuario?.apellido ?: ""}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = if (index < review.calificacion) 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = review.comentario,
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Producto: ${review.producto?.nombre ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                
                if (review.fechaCreacion != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "Fecha: ${review.fechaCreacion}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}
