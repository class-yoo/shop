package com.shoptest.api.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shoptest.api.product.dto.ProductCreateRequest
import com.shoptest.api.product.dto.ProductUpdateRequest
import com.shoptest.domain.brand.repository.BrandRepository
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.repository.ProductRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var brandRepository: BrandRepository

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @AfterEach
    fun tearDown() {
        productRepository.deleteAll()
        brandRepository.deleteAll()
        categoryRepository.deleteAll()
    }

    @Test
    @DisplayName("상품 생성 성공")
    fun createProduct_success() {
        val brand = brandRepository.save(com.shoptest.domain.brand.Brand(name = "Nike"))
        val category = categoryRepository.save(com.shoptest.domain.category.Category(type = com.shoptest.domain.category.CategoryType.TOP))

        val request = ProductCreateRequest(
            name = "검정 가방",
            brandId = brand.id,
            categoryId = category.id,
            price = 12000
        )

        mockMvc.post("/api/v1/products") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { exists() }
            jsonPath("$.name") { value("검정 가방") }
            jsonPath("$.brandId") { value(brand.id) }
            jsonPath("$.categoryId") { value(category.id) }
            jsonPath("$.price") { value("12,000") }
        }
    }

    @Test
    @DisplayName("상품 수정 성공")
    fun updateProduct_success() {
        val brand = brandRepository.save(com.shoptest.domain.brand.Brand(name = "Nike"))
        val category = categoryRepository.save(com.shoptest.domain.category.Category(type = com.shoptest.domain.category.CategoryType.TOP))
        val savedProduct = productRepository.save(com.shoptest.domain.product.Product(name = "검정 가방", price = 12000, brand = brand, category = category))

        val updateRequest = ProductUpdateRequest(
            name = "네이비 백팩",
            price = 18000
        )

        mockMvc.put("/api/v1/products/${savedProduct.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(updateRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(savedProduct.id) }
            jsonPath("$.name") { value("네이비 백팩") }
            jsonPath("$.price") { value("18,000") }
        }
    }

    @Test
    @DisplayName("상품 삭제 성공")
    fun deleteProduct_success() {
        val brand = brandRepository.save(com.shoptest.domain.brand.Brand(name = "Nike"))
        val category = categoryRepository.save(com.shoptest.domain.category.Category(type = com.shoptest.domain.category.CategoryType.TOP))
        val savedProduct = productRepository.save(com.shoptest.domain.product.Product(name = "검정 가방", price = 12000, brand = brand, category = category))

        mockMvc.delete("/api/v1/products/${savedProduct.id}")
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    @DisplayName("존재하지 않는 상품 삭제 시 404")
    fun deleteProduct_notFound() {
        mockMvc.delete("/api/v1/products/9999")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.message") { value("상품을 찾을 수 없습니다.") }
            }
    }
}
