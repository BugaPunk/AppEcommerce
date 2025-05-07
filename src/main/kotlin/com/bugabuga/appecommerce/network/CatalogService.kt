package com.bugabuga.appecommerce.network

import com.bugabuga.appecommerce.model.Product
import com.bugabuga.appecommerce.model.ProductResponse
import io.ktor.client.call.*
import io.ktor.client.request.*

class CatalogService {
    private val client = ApiClient.client
    
    suspend fun getAllProducts(page: Int = 0, size: Int = 10, sort: String = "id"): ProductResponse {
        return client.get("${ApiClient.BASE_URL}/catalogo/productos") {
            parameter("page", page)
            parameter("size", size)
            parameter("sort", sort)
        }.body()
    }
    
    suspend fun getProductById(id: Int): Product {
        return client.get("${ApiClient.BASE_URL}/catalogo/productos/$id").body()
    }
    
    suspend fun searchProducts(keyword: String, page: Int = 0, size: Int = 10): ProductResponse {
        return client.get("${ApiClient.BASE_URL}/catalogo/productos/buscar") {
            parameter("keyword", keyword)
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
    
    suspend fun getProductsByStore(storeId: Int, page: Int = 0, size: Int = 10): ProductResponse {
        return client.get {
            url("/catalogo/tiendas/$storeId/productos")
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
    
    suspend fun getProductsByCategory(categoryId: Int, page: Int = 0, size: Int = 10): ProductResponse {
        return client.get {
            url("/catalogo/categorias/$categoryId/productos")
            parameter("page", page)
            parameter("size", size)
        }.body()
    }
    
    suspend fun getRecentProducts(): List<Product> {
        return client.get("${ApiClient.BASE_URL}/catalogo/productos/recientes").body()
    }
}
