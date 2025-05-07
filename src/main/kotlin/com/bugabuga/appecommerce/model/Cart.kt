package com.bugabuga.appecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val id: Int? = null,
    val productoId: Int,
    val productoNombre: String,
    val productoImagen: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val subtotal: Double
)

@Serializable
data class Cart(
    val id: Int? = null,
    val usuarioId: Int,
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0
)
