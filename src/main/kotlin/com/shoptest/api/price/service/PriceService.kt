package com.shoptest.api.price.service

import com.shoptest.api.price.dto.CategoryPriceDetail
import com.shoptest.api.price.dto.CategoryPriceInfo
import com.shoptest.api.price.dto.LowestPriceByCategoryResponse
import com.shoptest.api.price.dto.LowestTotalPriceByBrandResponse
import com.shoptest.common.message.MessageProvider
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PriceService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository,
    private val messageProvider: MessageProvider
) {

    @Transactional(readOnly = true)
    fun getLowestPriceByCategory(): LowestPriceByCategoryResponse {
        val categories = categoryRepository.findAll()

        val items = categories.map { category ->
            val lowestProduct = productRepository.findAllByCategory(category)
                .minByOrNull { it.price }
                ?: throw IllegalStateException(
                    messageProvider.get(
                        "error.category.product.not.found", category.type.displayName
                    )
                )

            CategoryPriceInfo(
                category = category.type.displayName,
                brand = lowestProduct.brand.name,
                price = lowestProduct.price
            )
        }.sortedBy { CategoryType.entries.indexOf(CategoryType.fromDisplayName(it.category)) }


    val totalPrice = items.sumOf { it.price }

        return LowestPriceByCategoryResponse(
            items = items,
            totalPrice = totalPrice
        )
    }

    fun getLowestTotalPriceBrand(): LowestTotalPriceByBrandResponse {
        val products = productRepository.findAll()
        if (products.isEmpty()) throw IllegalStateException(
            messageProvider.get("error.product.all.missing")
        )

        // 브랜드별 상품 그룹핑
        val groupedByBrand = products.groupBy { it.brand }

        // 모든 브랜드가 8개 카테고리 갖추고 있는지 확인 & 총액 계산
        val brandToTotalPrice = groupedByBrand
            .filterValues { it.size == CategoryType.entries.size }
            .mapValues { (_, products) -> products.sumOf { it.price } }

        val lowestBrandEntry = brandToTotalPrice.minByOrNull { it.value }
            ?: throw IllegalStateException(
                messageProvider.get("error.brand.missing.all.categories")
            )

        val lowestBrand = lowestBrandEntry.key
        val totalPrice = lowestBrandEntry.value

        val categoryDetails = groupedByBrand[lowestBrand]!!
            .sortedBy { it.category.id }
            .map {
                CategoryPriceDetail(
                    category = it.category.type.displayName,
                    price = it.price
                )
            }

        return LowestTotalPriceByBrandResponse(
            brand = lowestBrand.name,
            categories = categoryDetails,
            totalPrice = totalPrice
        )
    }
}
