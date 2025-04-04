package com.shoptest.domain.product.repository

import com.shoptest.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long>, ProductQueryRepository
