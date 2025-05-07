package com.bugabuga.appecommerce.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bugabuga.appecommerce.model.Product
import com.bugabuga.appecommerce.repository.CartRepository
import com.bugabuga.appecommerce.repository.ProductRepository
import com.bugabuga.appecommerce.repository.UserRepository
import com.bugabuga.appecommerce.ui.components.LargeAppBar
import com.bugabuga.appecommerce.ui.components.ProductCard
import com.bugabuga.appecommerce.ui.components.SearchBar
import kotlinx.coroutines.launch

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val productRepository = remember { ProductRepository() }
        val cartRepository = remember { CartRepository() }
        val userRepository = remember { UserRepository() }
        val coroutineScope = rememberCoroutineScope()
        
        val currentUser by userRepository.currentUser.collectAsState()
        val products by productRepository.products.collectAsState()
        
        var searchQuery by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        
        // Configuración para el scroll behavior del TopAppBar
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
        
        LaunchedEffect(Unit) {
            isLoading = true
            try {
                val result = productRepository.getRecentProducts()
                if (result.isSuccess) {
                    // Si no hay productos reales, carga algunos de prueba
                    if (products.isEmpty()) {
                        println("No hay productos reales, cargando productos de prueba")
                        productRepository.loadDummyProducts()
                    }
                    errorMessage = null
                } else {
                    errorMessage = "Error al cargar productos: ${result.exceptionOrNull()?.message}"
                    // Si hay un error, también cargamos productos de prueba
                    println("Error al cargar productos, cargando productos de prueba")
                    productRepository.loadDummyProducts()
                }
            } catch (e: Exception) {
                errorMessage = "Error al cargar productos: ${e.message}"
                // Si hay una excepción, también cargamos productos de prueba
                println("Excepción al cargar productos, cargando productos de prueba")
                productRepository.loadDummyProducts()
            } finally {
                isLoading = false
            }
        }
        
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeAppBar(
                    title = "E-Commerce App",
                    onCartClick = { navigator.push(CartScreen()) },
                    onProfileClick = { 
                        if (currentUser != null) {
                            navigator.push(ProfileScreen())
                        } else {
                            navigator.push(LoginScreen())
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = {
                        coroutineScope.launch {
                            isLoading = true
                            errorMessage = null
                            
                            try {
                                if (searchQuery.isNotEmpty()) {
                                    productRepository.searchProducts(searchQuery)
                                } else {
                                    productRepository.getRecentProducts()
                                }
                                isLoading = false
                            } catch (e: Exception) {
                                errorMessage = "Error en la búsqueda: ${e.message}"
                                isLoading = false
                            }
                        }
                    }
                )
                
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (errorMessage != null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else if (products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No hay productos disponibles",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isLoading = true
                                        try {
                                            // Intentar cargar productos reales
                                            val result = productRepository.getRecentProducts()
                                            if (result.isSuccess && products.isEmpty()) {
                                                // Si no hay productos reales, cargar productos de prueba
                                                productRepository.loadDummyProducts()
                                            }
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            ) {
                                Text("Actualizar")
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Botón adicional para cargar productos de prueba directamente
                            Button(
                                onClick = {
                                    productRepository.loadDummyProducts()
                                }
                            ) {
                                Text("Cargar Productos de Prueba")
                            }
                        }
                    }
                } else {
                    // Agregar un log para verificar que hay productos
                    println("Mostrando ${products.size} productos")
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(products) { product ->
                            ProductCard(
                                product = product,
                                onClick = {
                                    coroutineScope.launch {
                                        productRepository.getProductById(product.id!!)
                                        navigator.push(ProductDetailScreen(product.id))
                                    }
                                },
                                onAddToCart = {
                                    coroutineScope.launch {
                                        if (currentUser != null) {
                                            cartRepository.addProductToCart(
                                                currentUser!!.id!!,
                                                product.id!!,
                                                1
                                            )
                                            // Show snackbar or notification
                                        } else {
                                            navigator.push(LoginScreen())
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
