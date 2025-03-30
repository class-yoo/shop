package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "각 카테고리별로 가장 저렴한 상품을 가진 브랜드와 가격, 전체 총액 응답")
data class CheapestPriceByCategoryResponse(

    @JsonProperty("상품목록")
    @Schema(description = "카테고리별 최저가 상품 목록")
    val items: List<CheapestPriceDto>,

    @JsonProperty("총액")
    @JsonSerialize(using = PriceToStringSerializer::class)
    @Schema(description = "총합 가격", example = "34,100")
    val totalPrice: Int
)

@Schema(description = "카테고리별 최저가 상품 정보")
data class CheapestPriceDto(

    @JsonProperty("카테고리")
    @Schema(description = "카테고리 이름", example = "상의")
    val category: String,

    @JsonProperty("브랜드")
    @Schema(description = "브랜드 이름", example = "A")
    val brand: String,

    @JsonProperty("가격")
    @JsonSerialize(using = PriceToStringSerializer::class)
    @Schema(description = "상품 가격", example = "\"10,000\"")
    val price: Int
)
