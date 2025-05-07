package com.bugabuga.appecommerce.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int? = null,
    val email: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val roles: List<String> = listOf("CLIENTE"),
    val password: String? = null
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val usuario: User = User(),
    val mensaje: String = ""
)
