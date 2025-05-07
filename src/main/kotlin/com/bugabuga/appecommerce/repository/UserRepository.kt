package com.bugabuga.appecommerce.repository

import com.bugabuga.appecommerce.model.LoginResponse
import com.bugabuga.appecommerce.model.User
import com.bugabuga.appecommerce.network.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserRepository {
    val authService = AuthService() // Cambiado de private a public

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    suspend fun register(user: User): Result<User> {
        return try {
            val result = authService.register(user)
            Result.success(result)
        } catch (e: Exception) {
            println("Error en registro: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authService.login(email, password)
            _currentUser.value = response.usuario
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserProfile(userId: Int): Result<User> {
        return try {
            val user = authService.getUserProfile(userId)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserProfile(userId: Int, user: User): Result<User> {
        return try {
            val updatedUser = authService.updateUserProfile(userId, user)
            if (_currentUser.value?.id == userId) {
                _currentUser.value = updatedUser
            }
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        _currentUser.value = null
    }
}
