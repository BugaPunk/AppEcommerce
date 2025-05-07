package com.bugabuga.appecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class Store(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String,
    val logoUrl: String,
    val propietario: UserSummary? = null,
    val activa: Boolean = true
)

@Serializable
data class StoreResponse(
    val tiendas: List<Store>,
    val currentPage: Int,
    val totalItems: Int,
    val totalPages: Int
)
