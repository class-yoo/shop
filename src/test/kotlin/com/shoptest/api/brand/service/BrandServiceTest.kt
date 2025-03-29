package com.shoptest.api.brand.service

import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandResponse
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.brand.repository.BrandRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class BrandServiceTest {

    private fun brandService(
        brandRepository: BrandRepository = mock(),
    ) = BrandService(brandRepository)

    @Test
    fun `브랜드 생성`() {
        // given
        val brandRepository = mock<BrandRepository>()
        val service = brandService(brandRepository)

        val brandName = "나이키"
        val request = BrandCreateRequest(name = brandName)

        val savedBrand = Brand(id = 1L, name = request.name)
        whenever(brandRepository.save(any())).thenReturn(savedBrand)

        // when
        val createdBrand = service.createBrand(request)

        // then
        createdBrand shouldBe BrandResponse(id = 1L, name = brandName)
    }

    @Test
    fun `브랜드 수정`() {
        // given
        val brandRepository = mock<BrandRepository>()
        val service = brandService(brandRepository)

        val savedBrand = Brand(id = 1L, name = "나이키")

        whenever(brandRepository.findById(savedBrand.id)).thenReturn(Optional.of(savedBrand))

        val updatedRequest = BrandUpdateRequest(name = "아디다스")
        val updatedBrand = service.updateBrand(savedBrand.id, updatedRequest)

        // then: 수정된 브랜드의 이름이 아디다스로 변경되었는지 확인
        updatedBrand.name shouldBe "아디다스"
    }


    @Test
    fun `브랜드 삭제`() {
        // given
        val brandRepository = mock<BrandRepository>()
        val service = brandService(brandRepository)
        val savedBrand = Brand(id = 1L, name = "나이키")

        whenever(brandRepository.findById(savedBrand.id)).thenReturn(Optional.of(savedBrand))
        whenever(brandRepository.existsById(savedBrand.id)).thenReturn(true)

        // when
        service.deleteBrand(savedBrand.id)  // 실제로 deleteBrand 메소드 호출

        // then
        verify(brandRepository).deleteById(savedBrand.id)
    }
}
