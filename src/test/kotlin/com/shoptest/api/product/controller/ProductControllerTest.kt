package com.shoptest.api.product.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shoptest.api.product.dto.ProductCreateRequest
import com.shoptest.api.product.dto.ProductResponse
import com.shoptest.api.product.dto.ProductUpdateRequest
import com.shoptest.api.product.service.ProductService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*

@WebMvcTest(controllers = [ProductController::class])
@AutoConfigureMockMvc
class ProductControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper  // ObjectMapper 자동 주입
) {

    @MockBean
    lateinit var productService: ProductService

    private val baseUrl = "/api/v1/products"

    @DisplayName("상품 생성 성공")
    @Test
    fun createProduct_success() {
        val request = ProductCreateRequest(
            name = "검정 가방",
            brandId = 1L,
            categoryId = 2L,
            price = 12000
        )

        val response = ProductResponse(
            id = 100L,
            name = request.name,
            brandId = request.brandId,
            categoryId = request.categoryId,
            price = request.price
        )

        whenever(productService.create(eq(request))).thenReturn(response)

        mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(100) }
            jsonPath("$.name") { value("검정 가방") }
            jsonPath("$.brandId") { value(1) }
            jsonPath("$.categoryId") { value(2) }
            jsonPath("$.price") { value("12,000") }  // 가격을 문자열로 비교
        }
    }


    @DisplayName("상품 수정 요청 시 200 OK와 수정된 상품 정보 반환")
    @Test
    fun updateProduct_success() {
        val request = ProductUpdateRequest(
            name = "네이비 백팩",
            price = 18000
        )

        val response = ProductResponse(
            id = 101L,
            name = request.name!!,
            brandId = 1L,
            categoryId = 3L,
            price = request.price!!
        )

        whenever(productService.update(eq(101L), eq(request))).thenReturn(response)

        mockMvc.put("$baseUrl/101") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)  // 객체를 JSON으로 변환해서 요청 본문에 추가
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(101) }
            jsonPath("$.name") { value("네이비 백팩") }
            jsonPath("$.price") { value("18,000") }  // 정수로 비교
        }
    }

    @DisplayName("상품 삭제 성공")
    @Test
    fun deleteProduct_success() {
        val productId = 200L
        doNothing().`when`(productService).delete(productId)

        mockMvc.delete("$baseUrl/$productId")
            .andExpect {
                status { isOk() }
            }
    }

    @DisplayName("상품 삭제 실패 - 존재하지 않음")
    @Test
    fun deleteProduct_notFound() {
        val productId = 9999L
        whenever(productService.delete(productId))
            .thenThrow(NoSuchElementException("상품을 찾을 수 없습니다."))

        mockMvc.delete("$baseUrl/$productId")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.message") { value("상품을 찾을 수 없습니다.") }
            }
    }
}
