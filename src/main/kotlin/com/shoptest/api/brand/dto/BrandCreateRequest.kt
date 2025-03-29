package com.shoptest.api.brand.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "브랜드 생성 요청 DTO")
data class BrandCreateRequest(
    @Schema(description = "브랜드 이름", example = "무신사 스탠다드")
    val name: String
)
