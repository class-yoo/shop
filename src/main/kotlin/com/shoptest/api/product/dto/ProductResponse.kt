package com.shoptest.api.product.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.shoptest.common.serializer.PriceToStringSerializer

data class ProductResponse(
    val id: Long,
    val name: String,
    val brandId: Long,
    val categoryId: Long,
    @JsonSerialize(using = PriceToStringSerializer::class)
    val price: Int
)

