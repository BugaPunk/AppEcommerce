package com.bugabuga.appecommerce.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bugabuga.appecommerce.repository.CartRepository
import com.bugabuga.appecommerce.repository.UserRepository
import com.bugabuga.appecommerce.ui.components.CartItemRow
import kotlinx.coroutines.launch

class CartScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val cartRepository = remember { CartRepository() }
        val userRepository = remember { UserRepository() }
        val coroutineScope = rememberCoroutineScope()
        
        val currentUser by userRepository.currentUser.collectAsState()
        val cart by cartRepository.cart.collectAsState()
        
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(currentUser) {
            if (currentUser != null) {
                isLoading = true
                errorMessage = null
                
                try {
                    cartRepository.getUserCart(currentUser!!.id!!)
                    isLoading = false
                } catch (e: Exception) {
                    errorMessage = "Error al cargar el carrito: ${e.message}"
                    isLoading = false
                }
            }
        }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Carrito de Compras") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        if (cart != null && cart!!.items.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (currentUser != null) {
                                            cartRepository.clearCart(currentUser!!.id!!)
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Clear Cart"
                                )
                            }
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
                if (currentUser == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Inicia sesión para ver tu carrito",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { navigator.push(LoginScreen()) }
                        ) {
                            Text("Iniciar Sesión")
                        }
                    }
                } else if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                } else if (cart == null || cart!!.items.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Tu carrito está vacío",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { navigator.push(HomeScreen()) }
                        ) {
                            Text("Ir a Comprar")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(cart!!.items) { item ->
                                CartItemRow(
                                    item = item,
                                    onIncreaseQuantity = {
                                        coroutineScope.launch {
                                            cartRepository.updateProductQuantity(
                                                currentUser!!.id!!,
                                                item.productoId,
                                                item.cantidad + 1
                                            )
                                        }
                                    },
                                    onDecreaseQuantity = {
                                        coroutineScope.launch {
                                            if (item.cantidad > 1) {
                                                cartRepository.updateProductQuantity(
                                                    currentUser!!.id!!,
                                                    item.productoId,
                                                    item.cantidad - 1
                                                )
                                            }
                                        }
                                    },
                                    onRemoveItem = {
                                        coroutineScope.launch {
                                            cartRepository.removeProductFromCart(
                                                currentUser!!.id!!,
                                                item.productoId
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        
                        // Cart Summary
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total:",
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    
                                    Text(
                                        text = "$${cart!!.total}",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = { navigator.push(CheckoutScreen()) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Proceder al Pago")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
