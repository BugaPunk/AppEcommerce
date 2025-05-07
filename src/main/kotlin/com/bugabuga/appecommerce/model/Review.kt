package com.bugabuga.appecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: Int? = null,
    val calificacion: Int,
    val comentario: String,
    val usuario: UserSummary? = null,
    val producto: ProductSummary? = null,
    val fechaCreacion: String? = null
)

@Serializable
data class UserSummary(
    val id: Int,
    val nombre: String? = null,
    val apellido: String? = null
)

@Serializable
data class ProductSummary(
    val id: Int,
    val nombre: String? = null
)

@Serializable
data class ReviewRequest(
    val calificacion: Int,
    val comentario: String,
    val usuario: UserSummary,
    val producto: ProductSummary
)

@Serializable
data class ReviewResponse(
    val reseñas: List<Review>,
    val currentPage: Int,
    val totalItems: Int,
    val totalPages: Int,
    val calificacionPromedio: Double? = null
)
