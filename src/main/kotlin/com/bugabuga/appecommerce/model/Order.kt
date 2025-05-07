package com.bugabuga.appecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: Int? = null,
    val usuarioId: Int,
    val usuarioNombre: String? = null,
    val items: List<CartItem> = emptyList(),
    val total: Double,
    val fechaCreacion: String? = null,
    val estado: String,
    val direccionEnvio: String,
    val telefonoContacto: String,
    val pago: Payment? = null
)

@Serializable
data class Payment(
    val id: Int? = null,
    val monto: Double,
    val metodoPago: String,
    val fechaPago: String? = null,
    val referenciaPago: String? = null,
    val estado: String,
    val pedidoId: Int? = null
)

@Serializable
data class PaymentRequest(
    val numeroTarjeta: String,
    val cvv: String,
    val fechaExpiracion: String,
    val nombreTitular: String
)

@Serializable
data class PaymentResponse(
    val pedido: Order,
    val mensaje: String,
    val estado: String
)
