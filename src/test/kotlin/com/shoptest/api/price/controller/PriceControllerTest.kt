package com.shoptest.api.price.controller

import com.shoptest.api.price.dto.*
import com.shoptest.api.price.service.PriceService
import com.shoptest.domain.category.CategoryType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [PriceController::class])
class PriceControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
) {

    @MockBean
    lateinit var priceService: PriceService

    @Test
    @DisplayName("카테고리별 최저가 브랜드 조회 테스트")
    fun getCheapestPriceBrandPerCategory() {
        // given
        val response = CheapestPriceByCategoryResponse(
            items = listOf(
                CheapestPriceDto("상의", "A", 1000),
                CheapestPriceDto("아우터", "B", 2000)
            ),
            totalPrice = 3000
        )
        given(priceService.getCheapestPriceBrandPerCategory()).willReturn(response)

        // when & then
        mockMvc.get("/api/v1/price/cheapest")
            .andExpect {
                status { isOk() }
                jsonPath("$.상품목록").isArray
                jsonPath("$.상품목록[0].카테고리").value("상의")
                jsonPath("$.상품목록[0].브랜드").value("A")
                jsonPath("$.상품목록[0].가격").value("1,000")  // 가격을 문자열로 비교
                jsonPath("$.총액").value("3,000")  // 총액도 문자열로 비교
            }
    }

    @Test
    @DisplayName("최저 총액 브랜드 조회 테스트")
    fun getCheapestTotalPriceBrand() {
        // given
        val categoryItems = CategoryType.entries.map {
            CheapestBrandPriceDto(it.displayName, 1000)
        }
        val response = CheapestTotalPriceByBrandResponse(
            detail = CheapestBrandInfo(
                brand = "무신사",
                items = categoryItems,
                totalPrice = categoryItems.size * 1000
            )
        )
        given(priceService.getCheapestTotalPriceBrand()).willReturn(response)

        // when & then
        mockMvc.get("/api/v1/price/cheapest-brand")
            .andExpect {
                status { isOk() }
                jsonPath("$.최저가.브랜드").value("무신사")
                jsonPath("$.최저가.카테고리").isArray
                jsonPath("$.최저가.카테고리[0].카테고리").value(CategoryType.entries[0].displayName)
                jsonPath("$.최저가.카테고리[0].가격").value("1,000")  // 가격을 문자열로 비교
                jsonPath("$.최저가.총액").value((categoryItems.size * 1000).toString())  // 총액을 문자열로 비교
            }
    }

    @Test
    @DisplayName("카테고리별 최고가, 최저가 브랜드 조회 테스트")
    fun getMaxMinPriceProducts() {
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
            jsonPath("$.최고가[0].가격").value("30,000")  // 가격을 문자열로 비교
            jsonPath("$.최고가[1].브랜드").value("B")
            jsonPath("$.최고가[1].가격").value("30,000")  // 가격을 문자열로 비교
            jsonPath("$.최저가[0].브랜드").value("C")
            jsonPath("$.최저가[0].가격").value("10,000")  // 가격을 문자열로 비교
        }
    }
}
