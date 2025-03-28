package com.shoptest.domain.product.repository

import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.product.Product

interface ProductQueryRepository {
    fun findAllBrandIdsHavingProducts(): List<Long>
    fun findCheapestPricePerCategoryByBrandId(brandId: Long): List<Pair<CategoryType, Int>>
    fun findBrandNameById(brandId: Long): String?
    fun findLowestPriceBrandPerCategory(): List<Triple<CategoryType, String, Int>>
    fun findMaxPriceProductsByCategory(categoryType: CategoryType): List<Product>
    fun findMinPriceProductsByCategory(categoryType: CategoryType): List<Product>
}
