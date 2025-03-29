package com.shoptest.domain.brand.repository

import com.shoptest.domain.brand.Brand
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BrandRepository : JpaRepository<Brand, Long> {
    fun findByName(name: String): Brand?
    fun existsByName(name: String): Boolean
}
