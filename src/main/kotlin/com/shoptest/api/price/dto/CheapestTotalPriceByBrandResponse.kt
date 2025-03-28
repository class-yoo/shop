package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer

data class CheapestTotalPriceByBrandResponse(
    @JsonProperty("최저가")
    val detail: CheapestBrandInfo
)

data class CheapestBrandInfo(
    @JsonProperty("브랜드")
    val brand: String,

    @JsonProperty("카테고리")
    val items: List<CheapestBrandPriceDto>,

    @JsonProperty("총액")
    @JsonSerialize(using = PriceToStringSerializer::class)
    val totalPrice: Int
)

data class CheapestBrandPriceDto(
    @JsonProperty("카테고리")
    val category: String,

    @JsonProperty("가격")
    @JsonSerialize(using = PriceToStringSerializer::class)
    val price: Int
)
