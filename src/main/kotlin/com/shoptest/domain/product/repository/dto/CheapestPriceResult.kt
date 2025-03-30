package com.shoptest.domain.product.repository.dto

data class CheapestPriceResult(
    val brandId: Long,
    val categoryId: Long,
    val price: Int
)