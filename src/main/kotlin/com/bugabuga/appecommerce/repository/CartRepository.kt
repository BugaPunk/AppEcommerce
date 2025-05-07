package com.bugabuga.appecommerce.repository

import com.bugabuga.appecommerce.model.Cart
import com.bugabuga.appecommerce.network.CartService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartRepository {
    private val cartService = CartService()
    
    private val _cart = MutableStateFlow<Cart?>(null)
    val cart: StateFlow<Cart?> = _cart.asStateFlow()
    
    suspend fun getUserCart(userId: Int): Result<Cart> {
        return try {
            val cart = cartService.getUserCart(userId)
            _cart.value = cart
            Result.success(cart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun addProductToCart(userId: Int, productId: Int, quantity: Int): Result<Cart> {
        return try {
            val updatedCart = cartService.addProductToCart(userId, productId, quantity)
            _cart.value = updatedCart
            Result.success(updatedCart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updateProductQuantity(userId: Int, productId: Int, quantity: Int): Result<Cart> {
        return try {
            val updatedCart = cartService.updateProductQuantity(userId, productId, quantity)
            _cart.value = updatedCart
            Result.success(updatedCart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun removeProductFromCart(userId: Int, productId: Int): Result<Cart> {
        return try {
            val updatedCart = cartService.removeProductFromCart(userId, productId)
            _cart.value = updatedCart
            Result.success(updatedCart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun clearCart(userId: Int): Result<Cart> {
        return try {
            val emptyCart = cartService.clearCart(userId)
            _cart.value = emptyCart
            Result.success(emptyCart)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
