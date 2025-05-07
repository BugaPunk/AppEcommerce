package com.bugabuga.appecommerce.network

import com.bugabuga.appecommerce.model.LoginRequest
import com.bugabuga.appecommerce.model.LoginResponse
import com.bugabuga.appecommerce.model.User
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthService {
    private val client = ApiClient.client

    suspend fun register(user: User): User {
        try {
            val response = client.post("${ApiClient.BASE_URL}/auth-simple/registro") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            println("Respuesta del servidor (registro): ${response.status}")
            return response.body()
        } catch (e: Exception) {
            println("Error en AuthService.register: ${e.message}")
            throw e
        }
    }

    suspend fun login(email: String, password: String): LoginResponse {
        try {
            println("Intentando login con email: $email")
            val requestBody = LoginRequest(email, password)
            println("Cuerpo de la solicitud: $requestBody")
            
            // Usar la URL completa para asegurarnos de que es correcta
            val response = client.post("${ApiClient.BASE_URL}/auth-simple/login") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            
            println("Respuesta del servidor (login): ${response.status}")
            
            if (response.status.isSuccess()) {
                val responseBody = response.body<LoginResponse>()
                println("Login exitoso para usuario: ${responseBody.usuario.nombre}")
                return responseBody
            } else {
                val errorText = response.body<String>()
                println("Error del servidor: $errorText")
                throw Exception("Error del servidor: ${response.status} - $errorText")
            }
        } catch (e: Exception) {
            println("Error en AuthService.login: ${e.javaClass.name} - ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun getUserProfile(userId: Int): User {
        try {
            val response = client.get("${ApiClient.BASE_URL}/auth-simple/perfil") {
                parameter("usuarioId", userId)
            }
            println("Respuesta del servidor (perfil): ${response.status}")
            return response.body()
        } catch (e: Exception) {
            println("Error en AuthService.getUserProfile: ${e.message}")
            throw e
        }
    }

    suspend fun updateUserProfile(userId: Int, user: User): User {
        try {
            val response = client.put("${ApiClient.BASE_URL}/auth-simple/perfil/$userId") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }
            println("Respuesta del servidor (actualizar perfil): ${response.status}")
            return response.body()
        } catch (e: Exception) {
            println("Error en AuthService.updateUserProfile: ${e.message}")
            throw e
        }
    }

    suspend fun debugLogin(email: String, password: String) {
        try {
            println("=== INICIO DEPURACIÓN LOGIN ===")
            // Usar la URL completa para depuración
            val fullUrl = "${ApiClient.BASE_URL}/auth-simple/login"
            println("URL completa: $fullUrl")
            println("Email: $email")
            
            val requestBody = LoginRequest(email, password)
            println("Request body: $requestBody")
            
            val response = client.post(fullUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            
            println("Status: ${response.status}")
            println("Status code: ${response.status.value}")
            
            try {
                val responseText = response.body<String>()
                println("Response body: $responseText")
            } catch (e: Exception) {
                println("No se pudo leer el cuerpo de la respuesta como texto: ${e.message}")
            }
            
            println("=== FIN DEPURACIÓN LOGIN ===")
        } catch (e: Exception) {
            println("=== ERROR EN DEPURACIÓN LOGIN ===")
            println("Tipo de excepción: ${e.javaClass.name}")
            println("Mensaje: ${e.message}")
            e.printStackTrace()
            println("=== FIN ERROR DEPURACIÓN LOGIN ===")
        }
    }
}
