// ProductQueryRepositoryImpl.kt
package com.shoptest.domain.product.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.shoptest.domain.brand.QBrand.brand
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.QProduct.product

class ProductQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ProductQueryRepository {

    override fun findLowestPriceBrandPerCategory(): List<Triple<CategoryType, String, Int>> {
        return CategoryType.entries.mapNotNull { categoryType ->
            val minPrice = queryFactory
                .select(product.price.min())
                .from(product)
                .where(product.category.type.eq(categoryType))
                .fetchOne() ?: return@mapNotNull null

            val result = queryFactory
                .select(product.category.type, product.brand.name, product.price)
                .from(product)
                .where(
                    product.category.type.eq(categoryType),
                    product.price.eq(minPrice)
                )
                .limit(1)
                .fetchFirst()

            result?.let {
                val type = it.get(product.category.type)
                val brand = it.get(product.brand.name)
                val price = it.get(product.price)
                if (type != null && brand != null && price != null) {
                    Triple(type, brand, price)
                } else {
                    null
                }
            }
        }
    }


    override fun findAllBrandIdsHavingProducts(): List<Long> {
        return queryFactory
            .select(product.brand.id)
            .distinct()
            .from(product)
            .join(product.brand, brand)
            .fetch()
    }

    override fun findCheapestPricePerCategoryByBrandId(brandId: Long): List<Pair<CategoryType, Int>> {
        return CategoryType.entries.mapNotNull { categoryType ->
            val cheapestPrice = queryFactory
                .select(product.price.min())
                .from(product)
                .where(
                    product.brand.id.eq(brandId),
                    product.category.type.eq(categoryType)
                )
                .fetchOne()

            if (cheapestPrice != null) categoryType to cheapestPrice else null
        }
    }

    override fun findBrandNameById(brandId: Long): String? {
        return queryFactory
            .select(brand.name)
            .from(brand)
            .where(brand.id.eq(brandId))
            .fetchOne()
    }

    override fun findMaxPriceProductsByCategory(categoryType: CategoryType): List<Product> {
        val maxPrice = queryFactory
            .select(product.price.max())
            .from(product)
            .where(product.category.type.eq(categoryType))
            .fetchOne() ?: return emptyList()

        return queryFactory
            .selectFrom(product)
            .where(
                product.category.type.eq(categoryType),
                product.price.eq(maxPrice)
            )
            .fetch()
    }

    override fun findMinPriceProductsByCategory(categoryType: CategoryType): List<Product> {
        val minPrice = queryFactory
            .select(product.price.min())
            .from(product)
            .where(product.category.type.eq(categoryType))
            .fetchOne() ?: return emptyList()

        return queryFactory
            .selectFrom(product)
            .where(
                product.category.type.eq(categoryType),
                product.price.eq(minPrice)
            )
            .fetch()
    }
}