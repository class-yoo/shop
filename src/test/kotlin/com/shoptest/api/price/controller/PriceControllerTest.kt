package com.shoptest.api.price.controller

import com.shoptest.api.price.service.PriceService
import org.junit.jupiter.api.Test
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
    fun `카테고리별 최저가 브랜드 조회 API`() {
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
    fun `최저 총액 브랜드 조회 API`() {
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
}
