package com.shoptest.api.product.controller

import com.shoptest.api.product.dto.ProductCreateRequest
import com.shoptest.api.product.dto.ProductResponse
import com.shoptest.api.product.dto.ProductUpdateRequest
import com.shoptest.api.product.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    fun createProduct(
        @RequestBody request: ProductCreateRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }

    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.update(id, request)
        return ResponseEntity.ok(product)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        productService.delete(id)
        return ResponseEntity.ok().build()
    }
}
