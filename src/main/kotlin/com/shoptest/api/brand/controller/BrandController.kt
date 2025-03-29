package com.shoptest.api.brand.controller

import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandResponse
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.api.brand.service.BrandService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/brand")
class BrandController(
    private val brandService: BrandService
) {

    // 브랜드 생성
    @PostMapping
    fun createBrand(@RequestBody request: BrandCreateRequest): ResponseEntity<BrandResponse> {
        val brand = brandService.createBrand(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(brand)
    }

    // 브랜드 수정
    @PutMapping("/{id}")
    fun updateBrand(@PathVariable id: Long, @RequestBody request: BrandUpdateRequest): ResponseEntity<BrandResponse> {
        val updatedBrand = brandService.updateBrand(id, request)
        return ResponseEntity.ok(updatedBrand)
    }

    @DeleteMapping("/{id}")
    fun deleteBrand(@PathVariable id: Long): ResponseEntity<String> {
        brandService.deleteBrand(id)
        return ResponseEntity.ok("브랜드가 성공적으로 삭제되었습니다.")
    }

    // 브랜드 단일 조회
    @GetMapping("/{id}")
    fun getBrand(@PathVariable id: Long): ResponseEntity<BrandResponse> {
        val brand = brandService.getBrandById(id)
        return ResponseEntity.ok(brand)
    }
}
