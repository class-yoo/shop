package com.shoptest.api.brand.service

import com.shoptest.api.brand.dto.*
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.brand.repository.BrandRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BrandService(
    private val brandRepository: BrandRepository
) {

    @Transactional
    fun createBrand(request: BrandCreateRequest): BrandResponse {
        if (brandRepository.existsByName(request.name)) {
            throw IllegalArgumentException("이미 존재하는 브랜드입니다.")
        }

        val brand = Brand(name = request.name)
        val savedBrand = brandRepository.save(brand)

        return BrandResponse(savedBrand.id, savedBrand.name)
    }

    @Transactional
    fun updateBrand(id: Long, request: BrandUpdateRequest): BrandResponse {
        val brand = brandRepository.findById(id)
            .orElseThrow { NoSuchElementException("브랜드를 찾을 수 없습니다.") }

        brand.name = request.name

        return BrandResponse(brand.id, brand.name)
    }

    @Transactional
    fun deleteBrand(id: Long) {
        if (!brandRepository.existsById(id)) {
            throw NoSuchElementException("브랜드를 찾을 수 없습니다.")
        }
        brandRepository.deleteById(id)
    }

    fun getBrandById(id: Long): BrandResponse {
        val brand = brandRepository.findById(id)
            .orElseThrow { NoSuchElementException("브랜드를 찾을 수 없습니다.") }

        return BrandResponse(brand.id, brand.name)
    }
}
