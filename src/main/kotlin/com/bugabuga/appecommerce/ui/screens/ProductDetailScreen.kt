package com.bugabuga.appecommerce.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bugabuga.appecommerce.repository.CartRepository
import com.bugabuga.appecommerce.repository.ProductRepository
import com.bugabuga.appecommerce.repository.UserRepository
import com.seiko.imageloader.rememberImagePainter
import kotlinx.coroutines.launch

class ProductDetailScreen(private val productId: Int) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val productRepository = remember { ProductRepository() }
        val cartRepository = remember { CartRepository() }
        val userRepository = remember { UserRepository() }
        val coroutineScope = rememberCoroutineScope()
        
        val currentUser by userRepository.currentUser.collectAsState()
        val selectedProduct by productRepository.selectedProduct.collectAsState()
        
        var quantity by remember { mutableStateOf(1) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(productId) {
            isLoading = true
            errorMessage = null
            
            try {
                productRepository.getProductById(productId)
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar el producto: ${e.message}"
                isLoading = false
            }
        }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedProduct?.nombre ?: "Detalle del Producto") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { navigator.push(CartScreen()) }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart"
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
                } else if (selectedProduct != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Product Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            val painter = rememberImagePainter(selectedProduct!!.imagenUrl)
                            androidx.compose.foundation.Image(
                                painter = painter,
                                contentDescription = selectedProduct!!.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Product Info
                        Text(
                            text = selectedProduct!!.nombre,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "$${selectedProduct!!.precio}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            
                            Spacer(modifier = Modifier.width(4.dp))
                            
                            Text(
                                text = "${selectedProduct!!.calificacionPromedio} (${selectedProduct!!.cantidadReseñas} reseñas)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Disponibles: ${selectedProduct!!.stock}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = selectedProduct!!.descripcion,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Quantity Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cantidad:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    enabled = quantity > 1
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Decrease Quantity"
                                    )
                                }
                                
                                Text(
                                    text = "$quantity",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                
                                IconButton(
                                    onClick = { if (quantity < selectedProduct!!.stock) quantity++ },
                                    enabled = quantity < selectedProduct!!.stock
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Increase Quantity"
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Add to Cart Button
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (currentUser != null) {
                                        cartRepository.addProductToCart(
                                            currentUser!!.id!!,
                                            selectedProduct!!.id!!,
                                            quantity
                                        )
                                        // Show snackbar or notification
                                    } else {
                                        navigator.push(LoginScreen())
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Add to Cart"
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text("Agregar al Carrito")
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // View Reviews Button
                        OutlinedButton(
                            onClick = { navigator.push(ReviewsScreen(selectedProduct!!.id!!)) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Reviews"
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text("Ver Reseñas")
                        }
                    }
                }
            }
        }
    }
}
