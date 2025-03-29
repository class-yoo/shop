package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "카테고리별 최저가 상품 목록과 총합 응답")
data class CheapestPriceByCategoryResponse(

    @field:JsonProperty("상품목록")
    @field:Schema(description = "카테고리별 최저가 상품 목록")
    val items: List<CheapestPriceDto>,

    @field:JsonProperty("총액")
    @field:JsonSerialize(using = PriceToStringSerializer::class)
    @field:Schema(description = "총합 가격", example = "34,100")
    val totalPrice: Int
)

@Schema(description = "카테고리별 최저가 상품 정보")
data class CheapestPriceDto(

    @field:JsonProperty("카테고리")
    @field:Schema(description = "카테고리 이름", example = "상의")
    val category: String,

    @field:JsonProperty("브랜드")
    @field:Schema(description = "브랜드 이름", example = "A")
    val brand: String,

    @field:JsonProperty("가격")
    @field:JsonSerialize(using = PriceToStringSerializer::class)
    @field:Schema(description = "상품 가격", example = "10,000")
    val price: Int
)
