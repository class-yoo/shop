package com.shoptest.api.price.controller

import com.shoptest.api.price.dto.LowestPriceByCategoryResponse
import com.shoptest.api.price.dto.LowestTotalPriceByBrandResponse
import com.shoptest.api.price.service.PriceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/price")
class PriceController(
    private val priceService: PriceService
) {

    @GetMapping("/lowest")
    fun getLowestPriceByCategory(): LowestPriceByCategoryResponse {
        return priceService.getLowestPriceByCategory()
    }

    @GetMapping("/lowest-brand")
    fun getLowestTotalPriceBrand(): Map<String, LowestTotalPriceByBrandResponse> {
        val result = priceService.getLowestTotalPriceBrand()
        return mapOf("최저가" to result)
    }

}
