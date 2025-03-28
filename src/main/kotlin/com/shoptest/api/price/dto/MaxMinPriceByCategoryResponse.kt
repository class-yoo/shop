package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer

data class MaxMinPriceByCategoryResponse(
    @JsonProperty("카테고리")
    val category: String,

    @JsonProperty("최고가")
    val max: List<BrandPriceDto>,

    @JsonProperty("최저가")
    val min: List<BrandPriceDto>
)

data class BrandPriceDto(
    @JsonProperty("브랜드")
    val brand: String,

    @JsonProperty("가격")
    @JsonSerialize(using = PriceToStringSerializer::class)
    val price: Int
)
