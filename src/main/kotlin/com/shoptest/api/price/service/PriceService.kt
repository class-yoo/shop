package com.shoptest.api.price.service

import com.shoptest.api.price.dto.*
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class PriceService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {

    fun getCheapestPriceBrandPerCategory(): CheapestPriceByCategoryResponse {
        val rawData = productRepository.findLowestPriceBrandPerCategory()

        val grouped = rawData
            .groupBy { it.first } // CategoryType
            .mapValues { (_, list) ->
                list.sortedBy { it.second }.last()
            }

        val items = grouped.entries
            .sortedBy { CategoryType.entries.indexOf(it.key) }
            .map { (categoryType, triple) ->
                CheapestPriceDto(
                    category = categoryType.displayName,
                    brand = triple.second,
                    price = triple.third
                )
            }

        val totalPrice = items.sumOf { it.price }

        return CheapestPriceByCategoryResponse(
            items = items,
            totalPrice = totalPrice
        )
    }


    fun getCheapestTotalPriceBrand(): CheapestTotalPriceByBrandResponse {
        val requiredCategoryCount = categoryRepository.count().toInt()
        val brandIds = productRepository.findBrandIdsHavingAllCategories(requiredCategoryCount)
        val rawResults = productRepository.findCheapestPricesByBrandIds(brandIds)

        val categoryMap = categoryRepository.findAll()
            .associateBy { it.id }

        val groupedByBrand = rawResults.groupBy { it.brandId }
            .mapValues { entry ->
                entry.value.mapNotNull { result ->
                    val categoryType = categoryMap[result.categoryId]?.type
                    categoryType?.let { it to result.price }
                }
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
        val category = categoryRepository.findByType(categoryType)
            ?: throw RuntimeException("해당 카테고리를 찾을 수 없습니다: $categoryType")

        val maxProducts = productRepository.findMaxPriceProductsByCategoryId(category.id)
        val minProducts = productRepository.findMinPriceProductsByCategoryId(category.id)

        return MaxMinPriceByCategoryResponse(
            category = categoryType.displayName,
            max = maxProducts.map { BrandPriceDto(it.brand.name, it.price) },
            min = minProducts.map { BrandPriceDto(it.brand.name, it.price) }
        )
    }

}
