package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "카테고리별 최고가/최저가 브랜드 및 가격 응답")
data class MaxMinPriceByCategoryResponse(

    @JsonProperty("카테고리")
    @Schema(description = "요청한 카테고리의 한글 이름", example = "상의")
    val category: String,

    @JsonProperty("최고가")
    @Schema(description = "가장 비싼 상품을 가진 브랜드 목록")
    val max: List<BrandPriceDto>,

    @JsonProperty("최저가")
    @Schema(description = "가장 저렴한 상품을 가진 브랜드 목록")
    val min: List<BrandPriceDto>
)

@Schema(description = "브랜드별 가격 정보")
data class BrandPriceDto(

    @JsonProperty("브랜드")
    @Schema(description = "브랜드 이름", example = "브랜드B")
    val brand: String,

    @JsonProperty("가격")
    @JsonSerialize(using = PriceToStringSerializer::class)
    @Schema(description = "상품 가격", example = "\"10,000\"")
    val price: Int
)
