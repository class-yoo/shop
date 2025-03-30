package com.shoptest.api.brand.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.domain.brand.repository.BrandRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BrandControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var brandRepository: BrandRepository

    @AfterEach
    fun tearDown() {
        brandRepository.deleteAll()
    }

    @Test
    @DisplayName("브랜드 생성 성공")
    fun createBrand_success() {
        val request = BrandCreateRequest(name = "Nike")

        mockMvc.post("/api/v1/brand") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { exists() }
            jsonPath("$.name") { value("Nike") }
        }
    }

    @Test
    @DisplayName("브랜드 수정 성공")
    fun updateBrand_success() {
        // Given
        val saved = brandRepository.save(com.shoptest.domain.brand.Brand(name = "Adidas"))
        val request = BrandUpdateRequest(name = "Adidas Updated")

        // When + Then
        mockMvc.put("/api/v1/brand/${saved.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(saved.id.toInt()) }
            jsonPath("$.name") { value("Adidas Updated") }
        }
    }

    @Test
    @DisplayName("브랜드 삭제 성공")
    fun deleteBrand_success() {
        // Given
        val saved = brandRepository.save(com.shoptest.domain.brand.Brand(name = "Puma"))

        // When + Then
        mockMvc.delete("/api/v1/brand/${saved.id}")
            .andExpect {
                status { isOk() }
                content { string("브랜드가 성공적으로 삭제되었습니다.") }
            }
    }

    @Test
    @DisplayName("존재하지 않는 브랜드 삭제 시 404")
    fun deleteBrand_notFound() {
        mockMvc.delete("/api/v1/brand/9999")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.message") { value("브랜드를 찾을 수 없습니다.") }
                jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
            }
    }

    @Test
    @DisplayName("중복된 이름으로 브랜드 생성 시 예외 발생")
    fun createBrand_duplicateName() {
        brandRepository.save(com.shoptest.domain.brand.Brand(name = "Fila"))
        val request = BrandCreateRequest(name = "Fila")

        mockMvc.post("/api/v1/brand") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
            jsonPath("$.message") { value("이미 존재하는 브랜드입니다.") }
            jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
        }
    }

    @Test
    @DisplayName("존재하지 않는 브랜드 조회 시 404")
    fun getBrand_notFound() {
        mockMvc.get("/api/v1/brand/9999")
            .andExpect {
                status { isNotFound() }
                jsonPath("$.message") { value("브랜드를 찾을 수 없습니다.") }
                jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
            }
    }
}
