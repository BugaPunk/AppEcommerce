package com.bugabuga.appecommerce.network

import com.bugabuga.appecommerce.model.Cart
import io.ktor.client.call.*
import io.ktor.client.request.*

class CartService {
    private val client = ApiClient.client
    
    suspend fun getUserCart(userId: Int): Cart {
        return client.get {
            url("/carrito/$userId")
        }.body()
    }
    
    suspend fun addProductToCart(userId: Int, productId: Int, quantity: Int): Cart {
        return client.post {
            url("/carrito/agregar")
            parameter("usuarioId", userId)
            parameter("productoId", productId)
            parameter("cantidad", quantity)
        }.body()
    }
    
    suspend fun updateProductQuantity(userId: Int, productId: Int, quantity: Int): Cart {
        return client.put {
            url("/carrito/actualizar")
            parameter("usuarioId", userId)
            parameter("productoId", productId)
            parameter("cantidad", quantity)
        }.body()
    }
    
    suspend fun removeProductFromCart(userId: Int, productId: Int): Cart {
        return client.delete {
            url("/carrito/eliminar")
            parameter("usuarioId", userId)
            parameter("productoId", productId)
        }.body()
    }
    
    suspend fun clearCart(userId: Int): Cart {
        return client.delete {
            url("/carrito/vaciar/$userId")
        }.body()
    }
}
