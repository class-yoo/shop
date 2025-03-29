package com.shoptest.api.price.controller

import com.shoptest.domain.brand.Brand
import com.shoptest.domain.brand.repository.BrandRepository
import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PriceControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val brandRepository: BrandRepository,
    val productRepository: ProductRepository,
    val categoryRepository: CategoryRepository,
) {

    @BeforeEach
    fun setup() {
        productRepository.deleteAll()
        categoryRepository.deleteAll()

        val savedBrands = mutableMapOf<String, Brand>()
        listOf("브랜드A", "브랜드B", "브랜드C").forEach {
            savedBrands[it] = brandRepository.findByName(it) ?: brandRepository.save(Brand(name = it))
        }

        val categories = CategoryType.entries.mapNotNull { type ->
            categoryRepository.findByType(type) ?: categoryRepository.save(Category(type = type))
        }

        val brandA = savedBrands["브랜드A"]!!
        val brandB = savedBrands["브랜드B"]!!
        val brandC = savedBrands["브랜드C"]!!

        val products = mutableListOf<Product>()

        // 브랜드A: 모든 카테고리 1000원
        categories.forEach {
            products += Product(
                name = "${brandA.name}의 ${it.type.name} 상품",
                brand = brandA,
                category = it,
                price = 1000
            )
        }

        // 브랜드B: 일부만 보유 (첫 번째만 500원)
        products += Product(
            name = "${brandB.name}의 ${categories.first().type.name} 상품",
            brand = brandB,
            category = categories.first(),
            price = 500
        )

        // 브랜드C: 최고가 용
        products += Product(
            name = "${brandC.name}의 ${categories.first().type.name} 상품",
            brand = brandC,
            category = categories.first(),
            price = 10_000
        )
        productRepository.saveAll(products)
    }

    @Test
    @DisplayName("카테고리별 최저가 브랜드 조회")
    fun getCheapestPriceBrandPerCategory() {
        mockMvc.get("/api/v1/price/cheapest")
            .andExpect {
                status { isOk() }
                jsonPath("$.상품목록") { isArray() }
                jsonPath("$.총액") { isString() }
            }
    }

    @Test
    @DisplayName("모든 카테고리를 가진 브랜드 중 최저 총액 브랜드 조회")
    fun getCheapestTotalPriceBrand() {
        mockMvc.get("/api/v1/price/cheapest-brand")
            .andExpect {
                status { isOk() }
                jsonPath("$.최저가.브랜드") { value("브랜드A") }
                jsonPath("$.최저가.총액") { value("8,000") }
                jsonPath("$.최저가.카테고리") { isArray() }
            }
    }

    @Test
    @DisplayName("카테고리별 최고가, 최저가 브랜드 조회")
    fun getMaxMinPriceProducts() {
        mockMvc.get("/api/v1/price/max-min") {
            param("category", "TOP")
        }.andExpect {
            status { isOk() }
            jsonPath("$.카테고리") { value("상의") }
            jsonPath("$.최고가[0].브랜드") { value("브랜드C") }
            jsonPath("$.최고가[0].가격") { value("10,000") }
            jsonPath("$.최저가[0].브랜드") { value("브랜드B") }
            jsonPath("$.최저가[0].가격") { value("500") }
        }
    }
}
