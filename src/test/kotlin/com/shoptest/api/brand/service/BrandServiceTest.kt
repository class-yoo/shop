package com.shoptest.api.brand.service

import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandResponse
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.brand.repository.BrandRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

import java.util.*

class BrandServiceTest {

    private fun brandService(
        brandRepository: BrandRepository = mock(),
    ) = BrandService(brandRepository)

    @DisplayName("브랜드 생성 성공")
    @Test
    fun createBrand_success() {
        // given
        val brandRepository = mock<BrandRepository>()
        val service = BrandService(brandRepository)

        val brandName = "나이키"
        val request = BrandCreateRequest(name = brandName)
        val savedBrand = Brand(id = 1L, name = brandName)

        whenever(brandRepository.existsByName(brandName)).thenReturn(false)

        whenever(brandRepository.save(any<Brand>())).thenReturn(savedBrand)

        // when
        val result = service.createBrand(request)

        // then
        result shouldBe BrandResponse(id = 1L, name = brandName)
    }



    @DisplayName("브랜드 수정 성공")
    @Test
    fun updateBrand_success() {
        // given
        val brandRepository = mock<BrandRepository>()
        val service = brandService(brandRepository)

        val savedBrand = Brand(id = 1L, name = "나이키")
        whenever(brandRepository.findById(savedBrand.id)).thenReturn(Optional.of(savedBrand))

        val updatedRequest = BrandUpdateRequest(name = "아디다스")

        // when
        val updatedBrand = service.updateBrand(savedBrand.id, updatedRequest)

        // then
        updatedBrand.name shouldBe "아디다스"
    }

    @DisplayName("브랜드 삭제 성공")
    @Test
    fun deleteBrand_success() {
        // given
        val brandRepository = mock<BrandRepository>()
        val service = brandService(brandRepository)
        val savedBrand = Brand(id = 1L, name = "나이키")

        whenever(brandRepository.existsById(savedBrand.id)).thenReturn(true)

        // when
        service.deleteBrand(savedBrand.id)

        // then
        verify(brandRepository).deleteById(savedBrand.id)
    }
}
