package com.shoptest.api.product.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 수정 요청 DTO")
data class ProductUpdateRequest(
    @Schema(description = "상품 이름", example = "네이비 백팩", required = false)
    val name: String?,

    @Schema(description = "상품 가격", example = "18000", required = false)
    val price: Int?
)
