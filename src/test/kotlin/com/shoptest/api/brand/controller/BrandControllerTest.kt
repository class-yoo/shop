package com.shoptest.api.brand.controller

import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandResponse
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.api.brand.service.BrandService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

@WebMvcTest(controllers = [BrandController::class])
@AutoConfigureMockMvc
class BrandControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {

    @MockBean
    lateinit var brandService: BrandService

    @DisplayName("브랜드 생성 성공")
    @Test
    fun createBrand_success() {
        val brandName = "나이키"
        val request = BrandCreateRequest(name = brandName)
        val brandResponse = BrandResponse(id = 1L, name = brandName)

        whenever(brandService.createBrand(request)).thenReturn(brandResponse)

        mockMvc.perform(post("/api/v1/brand")
            .contentType("application/json")
            .content("""{"name": "$brandName"}"""))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(brandName))
            .andExpect(jsonPath("$.id").value(1L))
    }

    @DisplayName("브랜드 수정 성공")
    @Test
    fun updateBrand_success() {
        val updatedName = "아디다스"
        val updatedRequest = BrandUpdateRequest(name = updatedName)
        val updatedBrand = BrandResponse(id = 1L, name = updatedName)

        whenever(brandService.updateBrand(1L, updatedRequest)).thenReturn(updatedBrand)

        mockMvc.perform(put("/api/v1/brand/1")
            .contentType("application/json")
            .content("""{"name": "$updatedName"}"""))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(updatedName))
            .andExpect(jsonPath("$.id").value(1L))
    }

    @DisplayName("브랜드 삭제 성공")
    @Test
    fun deleteBrand_success() {
        val brandId = 1L
        val responseMessage = "브랜드가 성공적으로 삭제되었습니다."

        doNothing().`when`(brandService).deleteBrand(brandId)

        mockMvc.perform(delete("/api/v1/brand/$brandId"))
            .andExpect(status().isOk)
            .andExpect(content().string(responseMessage))
    }

    @DisplayName("브랜드 삭제 실패 - 존재하지 않는 브랜드")
    @Test
    fun deleteBrand_notFound() {
        val brandId = 99999999L

        whenever(brandService.deleteBrand(brandId))
            .thenThrow(NoSuchElementException("브랜드를 찾을 수 없습니다."))

        mockMvc.perform(delete("/api/v1/brand/$brandId"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("브랜드를 찾을 수 없습니다."))
    }
}
