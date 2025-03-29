package com.shoptest.api.brand.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "브랜드 응답 DTO")
data class BrandResponse(
    @Schema(description = "브랜드 ID", example = "1")
    val id: Long,

    @Schema(description = "브랜드 이름", example = "무신사 스탠다드")
    val name: String
)
