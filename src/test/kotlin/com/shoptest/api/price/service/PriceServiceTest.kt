package com.shoptest.api.price.service

import com.shoptest.api.price.dto.BrandPriceDto
import com.shoptest.api.price.dto.CheapestBrandPriceDto
import com.shoptest.api.price.dto.CheapestPriceDto
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import com.shoptest.domain.product.repository.dto.CheapestPriceResult
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever

class PriceServiceTest {

    private val productRepository = mock(ProductRepository::class.java)
    private val categoryRepository = mock(CategoryRepository::class.java)
    private val priceService = PriceService(productRepository, categoryRepository)

    @Test
    @DisplayName("카테고리별 최저가 브랜드를 조회")
    fun getCheapestPriceBrandPerCategory() {
        // given
        val mockData = listOf(
            Triple(CategoryType.TOP, "A", 1000),
            Triple(CategoryType.OUTER, "B", 2000)
        )

        `when`(productRepository.findLowestPriceBrandPerCategory()).thenReturn(mockData)

        // when
        val result = priceService.getCheapestPriceBrandPerCategory()

        // then
        result.totalPrice shouldBe 3000
        result.items shouldBe listOf(
            CheapestPriceDto("상의", "A", 1000),
            CheapestPriceDto("아우터", "B", 2000)
        ).sortedWith(compareBy {
            CategoryType.entries.indexOf(CategoryType.fromDisplayName(it.category))
        })
    }

    @Test
    @DisplayName("모든 카테고리를 가진 브랜드 중 최저 총액 브랜드를 조회")
    fun getCheapestTotalPriceBrand() {
        // given
        val brandId = 1L
        val brandName = "무신사"
        val price = 1000

        val allCategories = CategoryType.entries
        val categoryList = allCategories.mapIndexed { index, type ->
            Category(id = (index + 1).toLong(), type = type)
        }

        val cheapestPriceResults = categoryList.map {
            CheapestPriceResult(
                brandId = brandId,
                categoryId = it.id,
                price = price
            )
        }

        whenever(categoryRepository.findAll()).thenReturn(categoryList)
        whenever(categoryRepository.count()).thenReturn(categoryList.size.toLong())

        whenever(productRepository.findBrandIdsHavingAllCategories(categoryList.size))
            .thenReturn(listOf(brandId))

        whenever(productRepository.findCheapestPricesByBrandIds(listOf(brandId)))
            .thenReturn(cheapestPriceResults)

        whenever(productRepository.findBrandNameById(brandId))
            .thenReturn(brandName)

        // when
        val result = priceService.getCheapestTotalPriceBrand()

        // then
        result.detail.brand shouldBe brandName
        result.detail.items shouldBe allCategories.map {
            CheapestBrandPriceDto(it.displayName, price)
        }
        result.detail.totalPrice shouldBe allCategories.size * price
    }

    @Test
    @DisplayName("카테고리별 최고가, 최저가 브랜드를 조회")
    fun getMaxMinPriceProducts() {
        // given
        val categoryType = CategoryType.TOP
        val categoryId = 10L
        val category = Category(id = categoryId, type = categoryType)

        val maxProducts = listOf(
            Product(name = "상품A", brand = Brand(name = "A"), category = category, price = 30000),
            Product(name = "상품B", brand = Brand(name = "B"), category = category, price = 30000),
        )
        val minProducts = listOf(
            Product(name = "상품C", brand = Brand(name = "C"), category = category, price = 10000),
        )

        // 카테고리 조회 mock
        whenever(categoryRepository.findByType(categoryType)).thenReturn(category)

        // 상품 조회 mock
        whenever(productRepository.findMaxPriceProductsByCategoryId(categoryId)).thenReturn(maxProducts)
        whenever(productRepository.findMinPriceProductsByCategoryId(categoryId)).thenReturn(minProducts)

        // when
        val result = priceService.getMaxMinPriceProducts(categoryType)

        // then
        result.category shouldBe categoryType.displayName
        result.max shouldContainExactlyInAnyOrder listOf(
            BrandPriceDto("A", 30000),
            BrandPriceDto("B", 30000)
        )
        result.min shouldContainExactlyInAnyOrder listOf(
            BrandPriceDto("C", 10000)
        )
    }
}
