package com.shoptest.domain.product.repository

import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.dto.CheapestPriceResult

interface ProductQueryRepository {
    fun findBrandIdsHavingAllCategories(requiredCategoryCount: Int): List<Long>
    fun findCheapestPricesByBrandIds(brandIds: List<Long>): List<CheapestPriceResult>
    fun findBrandNameById(brandId: Long): String?
    fun findLowestPriceBrandPerCategory(): List<Triple<CategoryType, String, Int>>
    fun findMaxPriceProductsByCategory(categoryType: CategoryType): List<Product>
    fun findMinPriceProductsByCategory(categoryType: CategoryType): List<Product>
}
