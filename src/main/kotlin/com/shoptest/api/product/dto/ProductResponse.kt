package com.shoptest.api.product.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 응답 DTO")
data class ProductResponse(
    @Schema(description = "상품 ID", example = "1")
    val id: Long,

    @Schema(description = "상품 이름", example = "검정 가방")
    val name: String,

    @Schema(description = "브랜드 ID", example = "1")
    val brandId: Long,

    @Schema(description = "카테고리 ID", example = "2")
    val categoryId: Long,

    @Schema(description = "상품 가격", example = "18000")
    val price: Int
)
