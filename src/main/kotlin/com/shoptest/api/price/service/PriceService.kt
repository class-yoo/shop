package com.shoptest.api.price.service

import com.shoptest.api.price.dto.*
import com.shoptest.common.message.MessageProvider
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class PriceService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val messageProvider: MessageProvider
) {

    fun getCheapestPriceBrandPerCategory(): CheapestPriceByCategoryResponse {
        val data = productRepository.findLowestPriceBrandPerCategory()

        val items = data.map { (categoryType, brand, price) ->
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
        val brandIds = productRepository.findAllBrandIdsHavingProducts()

        var cheapestBrandId: Long? = null
        var cheapestCategoryPrices: List<Pair<CategoryType, Int>> = emptyList()
        var minTotal = Int.MAX_VALUE

        for (brandId in brandIds) {
            val prices = productRepository.findCheapestPricePerCategoryByBrandId(brandId)

            if (prices.size != requiredCategoryCount) continue

            val total = prices.sumOf { it.second }
            if (total < minTotal) {
                cheapestBrandId = brandId
                cheapestCategoryPrices = prices
                minTotal = total
            }
        }

        if (cheapestBrandId == null) {
            throw RuntimeException("모든 카테고리에 상품을 보유한 브랜드가 없습니다.")
        }

        val brandName = productRepository.findBrandNameById(cheapestBrandId)
            ?: throw RuntimeException("브랜드 이름을 찾을 수 없습니다. ID: $cheapestBrandId")

        return CheapestTotalPriceByBrandResponse(
            detail = CheapestBrandInfo(
                brand = brandName,
                items = cheapestCategoryPrices.map {
                    CheapestBrandPriceDto(it.first.displayName, it.second)
                },
                totalPrice = minTotal
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
