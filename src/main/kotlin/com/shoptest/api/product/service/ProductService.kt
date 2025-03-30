package com.shoptest.api.product.service

import com.shoptest.api.product.dto.ProductCreateRequest
import com.shoptest.api.product.dto.ProductResponse
import com.shoptest.api.product.dto.ProductUpdateRequest
import com.shoptest.domain.brand.repository.BrandRepository
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val brandRepository: BrandRepository,
    private val categoryRepository: CategoryRepository
) {

    @Transactional
    fun create(request: ProductCreateRequest): ProductResponse {
        val brand = brandRepository.findById(request.brandId)
            .orElseThrow { NoSuchElementException("브랜드를 찾을 수 없습니다.") }

        val category = categoryRepository.findById(request.categoryId)
            .orElseThrow { NoSuchElementException("카테고리를 찾을 수 없습니다.") }

        val product = Product(
            name = request.name,
            price = request.price,
            brand = brand,
            category = category
        )

        val savedProduct = productRepository.save(product)

        return ProductResponse(
            id = savedProduct.id,
            name = savedProduct.name,
            brandId = savedProduct.brand.id,
            categoryId = savedProduct.category.id,
            price = savedProduct.price
        )
    }

    @Transactional
    fun update(id: Long, request: ProductUpdateRequest): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("상품을 찾을 수 없습니다.") }

        val updated = product.withUpdated(
            name = request.name,
            price = request.price
        )

        return productRepository.save(updated).toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("상품을 찾을 수 없습니다.") }

        productRepository.delete(product)
    }

    private fun Product.toResponse(): ProductResponse {
        return ProductResponse(
            id = id,
            name = name,
            brandId = brand.id,
            categoryId = category.id,
            price = price
        )
    }
}
