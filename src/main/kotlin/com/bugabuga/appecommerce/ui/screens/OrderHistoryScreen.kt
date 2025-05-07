package com.bugabuga.appecommerce.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.bugabuga.appecommerce.network.PaymentService
import com.bugabuga.appecommerce.repository.UserRepository
import kotlinx.coroutines.launch

class OrderHistoryScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val paymentService = remember { PaymentService() }
        val userRepository = remember { UserRepository() }
        val coroutineScope = rememberCoroutineScope()
        
        val currentUser by userRepository.currentUser.collectAsState()
        
        var paymentHistory by remember { mutableStateOf<Map<String, Any>?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(Unit) {
            if (currentUser != null) {
                isLoading = true
                errorMessage = null
                
                try {
                    val history = paymentService.getUserPaymentHistory(currentUser!!.id!!)
                    paymentHistory = history
                    isLoading = false
                } catch (e: Exception) {
                    errorMessage = "Error al cargar historial de pagos: ${e.message}"
                    isLoading = false
                }
            }
        }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Historial de Pedidos") },
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
                if (currentUser == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Inicia sesión para ver tu historial de pedidos",
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
                } else if (paymentHistory == null || (paymentHistory!!["pagos"] as? List<*>)?.isEmpty() == true) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No tienes pedidos realizados",
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
                    val payments = paymentHistory!!["pagos"] as List<Map<String, Any>>
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(payments) { payment ->
                            OrderHistoryItem(payment)
                        }
                    }
                }
            }
        }
    }
    
    @Composable
    fun OrderHistoryItem(payment: Map<String, Any>) {
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pedido #${payment["pedidoId"]}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Text(
                        text = "$${payment["monto"]}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Método: ${payment["metodoPago"]}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    val estadoColor = when (payment["estado"]) {
                        "COMPLETADO" -> MaterialTheme.colorScheme.primary
                        "REEMBOLSADO" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.outline
                    }
                    
                    Text(
                        text = "${payment["estado"]}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = estadoColor
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Fecha: ${payment["fechaPago"]}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Estado del pedido: ${payment["estadoPedido"]}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = { /* View order details */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Ver Detalles")
                }
            }
        }
    }
}
