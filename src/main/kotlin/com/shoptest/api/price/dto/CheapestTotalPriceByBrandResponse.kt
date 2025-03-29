package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "모든 카테고리에 상품을 보유한 브랜드 중 총액이 가장 낮은 브랜드와 상품 목록 응답")
data class CheapestTotalPriceByBrandResponse(

    @JsonProperty("최저가")
    @Schema(description = "최저 총액을 가진 브랜드 상세 정보")
    val detail: CheapestBrandInfo
)

@Schema(description = "브랜드별 카테고리 상품 가격 정보 및 총액")
data class CheapestBrandInfo(

    @JsonProperty("브랜드")
    @Schema(description = "브랜드 이름", example = "브랜드A")
    val brand: String,

    @JsonProperty("카테고리")
    @Schema(description = "카테고리별 상품 가격 목록")
    val items: List<CheapestBrandPriceDto>,

    @JsonProperty("총액")
    @JsonSerialize(using = PriceToStringSerializer::class)
    @Schema(description = "카테고리별 상품 가격 총합", example = "34,100")
    val totalPrice: Int
)

@Schema(description = "카테고리별 상품 가격 정보")
data class CheapestBrandPriceDto(

    @JsonProperty("카테고리")
    @Schema(description = "카테고리 이름", example = "모자")
    val category: String,

    @JsonProperty("가격")
    @JsonSerialize(using = PriceToStringSerializer::class)
    @Schema(description = "상품 가격", example = "\"5,000\"")
    val price: Int
)
