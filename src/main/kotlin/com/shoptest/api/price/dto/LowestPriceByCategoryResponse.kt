package com.shoptest.api.price.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer

data class LowestPriceByCategoryResponse(
    @JsonProperty("상품목록")
    val items: List<CategoryPriceInfo>,

    @JsonProperty("총액")
    @JsonSerialize(using = PriceToStringSerializer::class)
    val totalPrice: Int
)

data class CategoryPriceInfo(
    @JsonProperty("카테고리")
    val category: String,

    @JsonProperty("브랜드")
    val brand: String,

    @JsonProperty("가격")
    @JsonSerialize(using = PriceToStringSerializer::class)
    val price: Int
)
