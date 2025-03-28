package com.shoptest.api.price.service

import com.shoptest.api.price.dto.BrandPriceDto
import com.shoptest.api.price.dto.CheapestBrandPriceDto
import com.shoptest.api.price.dto.CheapestPriceDto
import com.shoptest.api.price.dto.CheapestTotalPriceByBrandResponse
import com.shoptest.common.message.MessageProvider
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PriceServiceTest {

    private val categoryRepository = mock(CategoryRepository::class.java)
    private val productRepository = mock(ProductRepository::class.java)
    private val messageProvider = mock(MessageProvider::class.java)
    private val priceService = PriceService(categoryRepository, productRepository, messageProvider)

    @Test
    fun `카테고리별 최저가 브랜드를 조회할 수 있다`() {
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
        )
    }

    @Test
    fun `모든 카테고리를 가진 브랜드 중 최저 총액 브랜드를 조회할 수 있다`() {
        // given
        val brandId = 1L
        val allCategories = CategoryType.entries
        val priceByCategory = allCategories.map { it to 1000 }

        `when`(productRepository.findAllBrandIdsHavingProducts()).thenReturn(listOf(brandId))
        `when`(productRepository.findCheapestPricePerCategoryByBrandId(brandId)).thenReturn(priceByCategory)
        `when`(productRepository.findBrandNameById(brandId)).thenReturn("무신사")

        // when
        val result: CheapestTotalPriceByBrandResponse = priceService.getCheapestTotalPriceBrand()

        // then
        result.detail.brand shouldBe "무신사"
        result.detail.items shouldBe allCategories.map {
            CheapestBrandPriceDto(it.displayName, 1000)
        }
        result.detail.totalPrice shouldBe allCategories.size * 1000
    }

    @Test
    fun `카테고리별 최고가, 최저가 브랜드를 조회할 수 있다`() {
        // given
        val categoryType = CategoryType.TOP

        val maxProducts = listOf(
            Product(brand = Brand(name = "A"), category = Category(type = CategoryType.TOP), price = 30000),
            Product(brand = Brand(name = "B"), category = Category(type = CategoryType.TOP), price = 30000),
        )
        val minProducts = listOf(
            Product(brand = Brand(name = "C"), category = Category(type = CategoryType.TOP), price = 10000),
        )

        `when`(productRepository.findMaxPriceProductsByCategory(categoryType)).thenReturn(maxProducts)
        `when`(productRepository.findMinPriceProductsByCategory(categoryType)).thenReturn(minProducts)

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
