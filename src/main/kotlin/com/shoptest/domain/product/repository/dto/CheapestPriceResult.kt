package com.shoptest.domain.product.repository.dto

import com.shoptest.domain.category.CategoryType

data class CheapestPriceResult(
    val brandId: Long,
    val categoryType: CategoryType,
    val price: Int
)