package com.shoptest.domain.product.repository

import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import com.shoptest.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findByBrandAndCategory(brand: Brand, category: Category): Product?
    fun findAllByCategory(category: Category): List<Product>
}
