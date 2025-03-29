package com.shoptest.api.product.dto

data class ProductCreateRequest(
    val name: String,
    val brandId: Long,
    val categoryId: Long,
    val price: Int
)
