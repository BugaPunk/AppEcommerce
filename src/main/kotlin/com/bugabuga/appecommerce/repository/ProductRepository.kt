package com.bugabuga.appecommerce.repository

import com.bugabuga.appecommerce.model.Product
import com.bugabuga.appecommerce.model.ProductResponse
import com.bugabuga.appecommerce.network.CatalogService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductRepository {
    private val catalogService = CatalogService()
    
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()
    
    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()
    
    suspend fun getAllProducts(page: Int = 0, size: Int = 10, sort: String = "id"): Result<ProductResponse> {
        return try {
            val response = catalogService.getAllProducts(page, size, sort)
            _products.value = response.productos
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProductById(id: Int): Result<Product> {
        return try {
            val product = catalogService.getProductById(id)
            _selectedProduct.value = product
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun searchProducts(keyword: String, page: Int = 0, size: Int = 10): Result<ProductResponse> {
        return try {
            val response = catalogService.searchProducts(keyword, page, size)
            _products.value = response.productos
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProductsByStore(storeId: Int, page: Int = 0, size: Int = 10): Result<ProductResponse> {
        return try {
            val response = catalogService.getProductsByStore(storeId, page, size)
            _products.value = response.productos
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getProductsByCategory(categoryId: Int, page: Int = 0, size: Int = 10): Result<ProductResponse> {
        return try {
            val response = catalogService.getProductsByCategory(categoryId, page, size)
            _products.value = response.productos
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getRecentProducts(): Result<List<Product>> {
        return try {
            println("Obteniendo productos recientes...")
            val products = catalogService.getRecentProducts()
            println("Productos recientes obtenidos: ${products.size}")
            _products.value = products
            Result.success(products)
        } catch (e: Exception) {
            println("Error al obtener productos recientes: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    // Método para cargar productos de prueba (solo para desarrollo)
    fun loadDummyProducts() {
        println("Cargando productos de prueba...")
        val dummyProducts = listOf(
            Product(
                id = 1,
                nombre = "Smartphone XYZ",
                descripcion = "El último smartphone con características avanzadas",
                precio = 599.99,
                stock = 10,
                categoriaId = 1,
                categoriaNombre = "Electrónica",
                imagenUrl = "https://via.placeholder.com/300"
            ),
            Product(
                id = 2,
                nombre = "Laptop Pro",
                descripcion = "Laptop potente para profesionales",
                precio = 1299.99,
                stock = 5,
                categoriaId = 1,
                categoriaNombre = "Computadoras",
                imagenUrl = "https://via.placeholder.com/300"
            ),
            Product(
                id = 3,
                nombre = "Auriculares Inalámbricos",
                descripcion = "Auriculares con cancelación de ruido",
                precio = 149.99,
                stock = 20,
                categoriaId = 2,
                categoriaNombre = "Audio",
                imagenUrl = "https://via.placeholder.com/300"
            )
        )
        _products.value = dummyProducts
        println("Productos de prueba cargados: ${dummyProducts.size}")
    }
}
