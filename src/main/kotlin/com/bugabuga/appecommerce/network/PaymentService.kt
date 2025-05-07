package com.bugabuga.appecommerce.network

import com.bugabuga.appecommerce.model.Payment
import com.bugabuga.appecommerce.model.PaymentRequest
import com.bugabuga.appecommerce.model.PaymentResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class PaymentService {
    private val client = ApiClient.client
    
    suspend fun processPayment(
        userId: Int, 
        orderId: Int, 
        paymentMethod: String, 
        paymentRequest: PaymentRequest
    ): PaymentResponse {
        return client.post {
            url("/pagos/procesar")
            parameter("usuarioId", userId)
            parameter("pedidoId", orderId)
            parameter("metodoPago", paymentMethod)
            contentType(ContentType.Application.Json)
            setBody(paymentRequest)
        }.body()
    }
    
    suspend fun getPaymentInfo(paymentId: Int): Payment {
        return client.get {
            url("/pagos/$paymentId")
        }.body()
    }
    
    suspend fun getUserPaymentHistory(userId: Int): Map<String, Any> {
        return client.get {
            url("/pagos/usuario/$userId")
        }.body()
    }
    
    suspend fun processRefund(paymentId: Int, reason: String): Map<String, Any> {
        return client.post {
            url("/pagos/reembolso/$paymentId")
            contentType(ContentType.Application.Json)
            setBody(mapOf("motivo" to reason))
        }.body()
    }
}
