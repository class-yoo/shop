package com.shoptest.domain.brand.repository

import com.shoptest.domain.brand.Brand
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BrandRepository : JpaRepository<Brand, Long> {
    fun findByName(name: String): Brand? // 이 부분 중요!
    fun existsByName(name: String): Boolean
}
