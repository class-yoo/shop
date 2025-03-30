package com.shoptest.domain.category.repository

import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Long> {
    fun findByType(type: CategoryType): Category?
}
