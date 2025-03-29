package com.shoptest.api.brand.controller

import com.shoptest.api.brand.dto.BrandCreateRequest
import com.shoptest.api.brand.dto.BrandResponse
import com.shoptest.api.brand.dto.BrandUpdateRequest
import com.shoptest.api.brand.service.BrandService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/brand")
@Tag(name = "Brand", description = "브랜드 관리 API")
class BrandController(
    private val brandService: BrandService
) {

    @Operation(summary = "브랜드 생성", description = "새로운 브랜드를 등록합니다.")
    @PostMapping
    fun createBrand(@RequestBody request: BrandCreateRequest): ResponseEntity<BrandResponse> {
        val brand = brandService.createBrand(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(brand)
    }

    @Operation(summary = "브랜드 수정", description = "브랜드 이름을 수정합니다.")
    @PutMapping("/{id}")
    fun updateBrand(@PathVariable id: Long, @RequestBody request: BrandUpdateRequest): ResponseEntity<BrandResponse> {
        val updatedBrand = brandService.updateBrand(id, request)
        return ResponseEntity.ok(updatedBrand)
    }

    @Operation(summary = "브랜드 삭제", description = "브랜드를 삭제합니다.")
    @DeleteMapping("/{id}")
    fun deleteBrand(@PathVariable id: Long): ResponseEntity<String> {
        brandService.deleteBrand(id)
        return ResponseEntity.ok("브랜드가 성공적으로 삭제되었습니다.")
    }

    @Operation(summary = "브랜드 단일 조회", description = "ID로 브랜드 정보를 조회합니다.")
    @GetMapping("/{id}")
    fun getBrand(@PathVariable id: Long): ResponseEntity<BrandResponse> {
        val brand = brandService.getBrandById(id)
        return ResponseEntity.ok(brand)
    }
}
