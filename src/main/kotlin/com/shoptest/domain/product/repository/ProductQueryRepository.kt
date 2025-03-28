package com.shoptest.domain.product.repository

import com.shoptest.domain.category.CategoryType

interface ProductQueryRepository {
    fun findAllBrandIdsHavingProducts(): List<Long>
    fun findCheapestPricePerCategoryByBrandId(brandId: Long): List<Pair<CategoryType, Int>>
    fun findBrandNameById(brandId: Long): String?
    fun findLowestPriceBrandPerCategory(): List<Triple<CategoryType, String, Int>>
}
