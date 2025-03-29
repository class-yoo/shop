package com.shoptest.api.brand.controller

import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandResponse
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.api.brand.service.BrandService
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

    // 브랜드 생성 테스트
    @Test
    fun `브랜드 생성 테스트`() {
        val brandName = "나이키"
        val request = BrandCreateRequest(name = brandName)

        val brandResponse = BrandResponse(id = 1L, name = brandName)

        // 서비스에서 브랜드 생성 반환
        whenever(brandService.createBrand(request)).thenReturn(brandResponse)

        mockMvc.perform(post("/api/v1/brand")
            .contentType("application/json")
            .content("""{"name": "$brandName"}"""))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value(brandName))
            .andExpect(jsonPath("$.id").value(1L))
    }

    // 브랜드 수정 테스트
    @Test
    fun `브랜드 수정 테스트`() {
        val updatedName = "아디다스"
        val updatedRequest = BrandUpdateRequest(name = updatedName)

        val updatedBrand = BrandResponse(id = 1L, name = updatedName)

        // 수정된 브랜드 반환
        whenever(brandService.updateBrand(1L, updatedRequest)).thenReturn(updatedBrand)

        mockMvc.perform(put("/api/v1/brand/1")
            .contentType("application/json")
            .content("""{"name": "$updatedName"}"""))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(updatedName))
            .andExpect(jsonPath("$.id").value(1L))
    }

    // 브랜드 삭제 테스트
    @Test
    fun `브랜드 삭제 테스트`() {
        // 브랜드가 존재한다고 가정하고 삭제 처리
        val brandId = 1L
        val responseMessage = "브랜드가 성공적으로 삭제되었습니다."

        // 서비스에서 삭제 확인
        doNothing().`when`(brandService).deleteBrand(brandId)

        mockMvc.perform(delete("/api/v1/brand/$brandId"))
            .andExpect(status().isOk)
            .andExpect(content().string(responseMessage))
    }

    @Test
    fun `브랜드 삭제 실패 테스트 - 존재하지 않는 브랜드`() {
        val brandId = 99999999L  // 존재하지 않는 브랜드 ID

        // 서비스에서 존재하지 않는 브랜드로 예외 발생
        whenever(brandService.deleteBrand(brandId)).thenThrow(NoSuchElementException("브랜드를 찾을 수 없습니다."))

        mockMvc.perform(delete("/api/v1/brand/$brandId"))
            .andExpect(status().isNotFound)
            .andExpect(content().string("브랜드를 찾을 수 없습니다."))
    }
}
