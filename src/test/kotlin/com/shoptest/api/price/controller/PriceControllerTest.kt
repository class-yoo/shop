package com.shoptest.api.price.controller

import com.shoptest.api.price.dto.CategoryPriceDetail
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.get
import org.hamcrest.Matchers.*
import org.springframework.http.MediaType

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")  // seed 데이터 사용하는 profile
class PriceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    fun performGet(path: String) =
        mockMvc.get(path) {
            accept = MediaType.APPLICATION_JSON
        }

    @Test
    fun `카테고리별 최저가 조회 - 성공`() {
        mockMvc.get("/api/v1/price/lowest") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.items", hasSize<Any>(8))
            jsonPath("$.totalPrice", greaterThan(0))
            jsonPath("$.items[0].category", notNullValue())
            jsonPath("$.items[0].brand", notNullValue())
            jsonPath("$.items[0].price", greaterThan(0))
        }
    }

    @Test
    fun `단일 브랜드 최저가 코디 - 성공`() {
        mockMvc.get("/api/v1/price/lowest-brand") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.최저가.brand", notNullValue())
            jsonPath("$.최저가.categories", hasSize<CategoryPriceDetail>(8))
            jsonPath("$.최저가.totalPrice", greaterThan(0))
            jsonPath("$.최저가.categories[0].category", notNullValue())
            jsonPath("$.최저가.categories[0].price", greaterThan(0))
        }
    }

}
