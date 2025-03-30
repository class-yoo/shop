package com.shoptest.api.product.controller

import com.shoptest.api.product.dto.ProductCreateRequest
import com.shoptest.api.product.dto.ProductResponse
import com.shoptest.api.product.dto.ProductUpdateRequest
import com.shoptest.api.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product", description = "상품 관리 API")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @Operation(
        summary = "상품 생성",
        description = "새로운 상품을 생성합니다."
    )
    fun createProduct(
        @RequestBody @Parameter(description = "상품 생성 정보") request: ProductCreateRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.create(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "상품 수정",
        description = "기존 상품의 정보를 수정합니다."
    )
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest
    ): ResponseEntity<ProductResponse> {
        val product = productService.update(id, request)
        return ResponseEntity.ok(product)
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "상품 삭제",
        description = "상품을 삭제합니다."
    )
    fun deleteProduct(
        @PathVariable id: Long
    ): ResponseEntity<Unit> {
        productService.delete(id)
        return ResponseEntity.ok().build()
    }
}
