package com.shoptest.api.price.controller

import com.shoptest.api.price.dto.CheapestPriceByCategoryResponse
import com.shoptest.api.price.dto.CheapestTotalPriceByBrandResponse
import com.shoptest.api.price.dto.MaxMinPriceByCategoryResponse
import com.shoptest.api.price.service.PriceService
import com.shoptest.domain.category.CategoryType
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/price")
@Tag(name = "Price", description = "가격 관련 API")
class PriceController(
    private val priceService: PriceService
) {

    @Operation(
        summary = "카테고리별 최저가 브랜드 및 총액 조회",
        description = "각 카테고리별로 가장 저렴한 상품을 가진 브랜드와 가격, 전체 총액을 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "정상 응답",
                content = [Content(schema = Schema(implementation = CheapestPriceByCategoryResponse::class))])
        ]
    )
    @GetMapping("/cheapest")
    fun getLowestPriceByCategory(): ResponseEntity<CheapestPriceByCategoryResponse> {
        val response = priceService.getCheapestPriceBrandPerCategory()
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "모든 카테고리를 가진 브랜드 중 최저 총액 브랜드 조회",
        description = "모든 카테고리에 상품을 보유한 브랜드 중 총액이 가장 낮은 브랜드와 상품 목록을 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "정상 응답",
                content = [Content(schema = Schema(implementation = CheapestTotalPriceByBrandResponse::class))])
        ]
    )
    @GetMapping("/cheapest-brand")
    fun getCheapestBrandDetail(): ResponseEntity<CheapestTotalPriceByBrandResponse> {
        val response = priceService.getCheapestTotalPriceBrand()
        return ResponseEntity.ok(response)
    }

    @Operation(
        summary = "카테고리별 최고가/최저가 브랜드 및 가격 조회",
        description = "특정 카테고리에서 최고가 및 최저가 브랜드 정보를 조회합니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "정상 응답",
                content = [Content(schema = Schema(implementation = MaxMinPriceByCategoryResponse::class))])
        ]
    )
    @GetMapping("/max-min")
    fun getMaxMinPriceByCategory(
        @Parameter(description = "카테고리명", example = "상의")
        @RequestParam("category") categoryName: String
    ): ResponseEntity<MaxMinPriceByCategoryResponse> {
        val response = priceService.getMaxMinPriceProducts(
            CategoryType.fromDisplayName(categoryName)
        )
        return ResponseEntity.ok(response)
    }
}