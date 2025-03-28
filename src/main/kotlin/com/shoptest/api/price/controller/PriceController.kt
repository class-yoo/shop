package com.shoptest.api.price.controller

import com.shoptest.api.price.dto.CheapestPriceByCategoryResponse
import com.shoptest.api.price.dto.CheapestTotalPriceByBrandResponse
import com.shoptest.api.price.service.PriceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/price")
class PriceController(
    private val priceService: PriceService
) {

    @GetMapping("/cheapest")
    fun getLowestPriceByCategory(): CheapestPriceByCategoryResponse {
        return priceService.getCheapestPriceBrandPerCategory()
    }

    @GetMapping("/cheapest-brand")
    fun getCheapestBrandDetail(): ResponseEntity<CheapestTotalPriceByBrandResponse> {
        val response = priceService.getCheapestTotalPriceBrand()
        return ResponseEntity.ok(response)
    }
}
