package com.shoptest.api.price.service

import com.shoptest.api.price.dto.*
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class PriceService(
    private val productRepository: ProductRepository,
) {

    fun getCheapestPriceBrandPerCategory(): CheapestPriceByCategoryResponse {
        val data = productRepository.findLowestPriceBrandPerCategory()

        val items = data
            .sortedWith(compareBy({ it.first.name }, { it.second }))
            .map { (categoryType, brand, price) ->
                CheapestPriceDto(
                    category = categoryType.displayName,
                    brand = brand,
                    price = price
                )
            }

        val totalPrice = items.sumOf { it.price }

        return CheapestPriceByCategoryResponse(
            items = items,
            totalPrice = totalPrice
        )
    }


    fun getCheapestTotalPriceBrand(): CheapestTotalPriceByBrandResponse {
        val requiredCategoryCount = CategoryType.entries.size
        val brandIds = productRepository.findBrandIdsHavingAllCategories(requiredCategoryCount)

        val rawResults = productRepository.findCheapestPricesByBrandIds(brandIds)

        val groupedByBrand = rawResults.groupBy { it.brandId }
            .mapValues { entry ->
                entry.value.map { it.categoryType to it.price }
            }

        val (brandId, categoryPrices, totalPrice) = groupedByBrand
            .filter { it.value.size == requiredCategoryCount }
            .mapValues { it.value to it.value.sumOf { it.second } }
            .minByOrNull { it.value.second }
            ?.let { Triple(it.key, it.value.first, it.value.second) }
            ?: throw RuntimeException("모든 카테고리에 상품을 보유한 브랜드가 없습니다.")

        val brandName = productRepository.findBrandNameById(brandId)
            ?: throw RuntimeException("브랜드 이름을 찾을 수 없습니다. ID: $brandId")

        return CheapestTotalPriceByBrandResponse(
            detail = CheapestBrandInfo(
                brand = brandName,
                items = categoryPrices.map { CheapestBrandPriceDto(it.first.displayName, it.second) },
                totalPrice = totalPrice
            )
        )
    }

    fun getMaxMinPriceProducts(categoryType: CategoryType): MaxMinPriceByCategoryResponse {
        val maxProducts = productRepository.findMaxPriceProductsByCategory(categoryType)
        val minProducts = productRepository.findMinPriceProductsByCategory(categoryType)

        return MaxMinPriceByCategoryResponse(
            category = categoryType.displayName,
            max = maxProducts.map {
                BrandPriceDto(it.brand.name, it.price)
            },
            min = minProducts.map {
                BrandPriceDto(it.brand.name, it.price)
            }
        )
    }
}
