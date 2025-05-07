package com.bugabuga.appecommerce.network

import com.bugabuga.appecommerce.model.Review
import com.bugabuga.appecommerce.model.ReviewRequest
import com.bugabuga.appecommerce.model.ReviewResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class ReviewService {
    private val client = ApiClient.client
    
    suspend fun getProductReviews(
        productId: Int, 
        page: Int = 0, 
        size: Int = 10, 
        sort: String = "fechaCreacion", 
        direction: String = "desc"
    ): ReviewResponse {
        return client.get {
            url("/reseñas/producto/$productId")
            parameter("page", page)
            parameter("size", size)
            parameter("sort", sort)
            parameter("direction", direction)
        }.body()
    }
    
    suspend fun getUserReviews(userId: Int, page: Int = 0, size: Int = 10): ReviewResponse {
        return client.get {
            url("/reseñas/usuario/$userId")
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
    
    suspend fun createReview(reviewRequest: ReviewRequest): Review {
        return client.post {
            url("/reseñas")
            contentType(ContentType.Application.Json)
            setBody(reviewRequest)
        }.body()
    }
    
    suspend fun updateReview(reviewId: Int, rating: Int, comment: String): Review {
        return client.put {
            url("/reseñas/$reviewId")
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "calificacion" to rating,
                "comentario" to comment
            ))
        }.body()
    }
    
    suspend fun deleteReview(reviewId: Int) {
        client.delete {
            url("/reseñas/$reviewId")
        }
    }
}
