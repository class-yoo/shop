package com.shoptest.api.price.controller

import com.shoptest.api.price.dto.BrandPriceDto
import com.shoptest.api.price.dto.MaxMinPriceByCategoryResponse
import com.shoptest.api.price.service.PriceService
import com.shoptest.domain.category.CategoryType
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [PriceController::class])
@AutoConfigureMockMvc
class PriceControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
) {

    @MockBean
    lateinit var priceService: PriceService

    @Test
    fun `카테고리별 최저가 브랜드 조회 테스트`() {
        mockMvc.get("/api/v1/price/cheapest")
            .andExpect {
                status { isOk() }
                jsonPath("$.상품목록").isArray
                jsonPath("$.상품목록[0].카테고리").isString
                jsonPath("$.상품목록[0].브랜드").isString
                jsonPath("$.상품목록[0].가격").isString
                jsonPath("$.총액").isString
            }
    }

    @Test
    fun `최저 총액 브랜드 조회 테스트`() {
        mockMvc.get("/api/v1/price/cheapest-brand")
            .andExpect {
                status { isOk() }
                jsonPath("$.최저가.브랜드").isString
                jsonPath("$.최저가.카테고리").isArray
                jsonPath("$.최저가.카테고리[0].카테고리").isString
                jsonPath("$.최저가.카테고리[0].가격").isString
                jsonPath("$.최저가.총액").isString
            }
    }

    @Test
    fun `카테고리별 최고가, 최저가 브랜드를 조회 테스트`() {
        // given
        val categoryType = CategoryType.TOP
        val response = MaxMinPriceByCategoryResponse(
            category = categoryType.displayName,
            max = listOf(BrandPriceDto("A", 30000), BrandPriceDto("B", 30000)),
            min = listOf(BrandPriceDto("C", 10000))
        )

        given(priceService.getMaxMinPriceProducts(categoryType)).willReturn(response)

        // when & then
        mockMvc.get("/api/v1/price/max-min") {
            param("category", categoryType.name)
        }.andExpect {
            status { isOk() }
            jsonPath("$.카테고리").value(categoryType.displayName)
            jsonPath("$.최고가[0].브랜드").value("A")
            jsonPath("$.최고가[0].가격").value("30000")
            jsonPath("$.최고가[1].브랜드").value("B")
            jsonPath("$.최고가[1].가격").value("30000")
            jsonPath("$.최저가[0].브랜드").value("C")
            jsonPath("$.최저가[0].가격").value("10000")
        }
    }
}
