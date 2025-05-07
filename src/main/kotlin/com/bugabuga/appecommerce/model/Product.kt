package com.bugabuga.appecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagenUrl: String,
    val tiendaId: Int? = null,
    val tiendaNombre: String? = null,
    val categoriaId: Int? = null,
    val categoriaNombre: String? = null,
    val calificacionPromedio: Double = 0.0,
    val cantidadReseñas: Int = 0
)

@Serializable
data class ProductResponse(
    val productos: List<Product>,
    val currentPage: Int,
    val totalItems: Int,
    val totalPages: Int
)
