package com.shoptest.domain.product.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import com.shoptest.domain.brand.QBrand.brand
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.QCategory.category
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.QProduct
import com.shoptest.domain.product.QProduct.product
import com.shoptest.domain.product.repository.dto.CheapestPriceResult

class ProductQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ProductQueryRepository {

    override fun findLowestPriceBrandPerCategory(): List<Triple<CategoryType, String, Int>> {
        val subProduct = QProduct("sub")

        return queryFactory
            .select(product.category.type, product.brand.name, product.price)
            .from(product)
            .join(product.category, category)
            .join(product.brand, brand)
            .where(
                Expressions.list(product.category.id, product.price)
                    .`in`(
                        JPAExpressions
                            .select(subProduct.category.id, subProduct.price.min())
                            .from(subProduct)
                            .groupBy(subProduct.category.id)
                    )
            )
            .fetch()
            .mapNotNull {
                val categoryType = it.get(product.category.type)
                val brandName = it.get(product.brand.name)
                val price = it.get(product.price)
                if (categoryType != null && brandName != null && price != null) {
                    Triple(categoryType, brandName, price)
                } else null
            }
    }

    override fun findBrandIdsHavingAllCategories(requiredCategoryCount: Int): List<Long> {
        return queryFactory
            .select(product.brand.id)
            .from(product)
            .groupBy(product.brand.id)
            .having(product.category.id.countDistinct().eq(requiredCategoryCount.toLong()))
            .fetch()
    }

    override fun findCheapestPricesByBrandIds(brandIds: List<Long>): List<CheapestPriceResult> {
        return queryFactory
            .select(
                Projections.constructor(
                    CheapestPriceResult::class.java,
                    product.brand.id,
                    product.category.id,
                    product.price.min()
                )
            )
            .from(product)
            .where(product.brand.id.`in`(brandIds))
            .groupBy(product.brand.id, product.category.id)
            .fetch()
    }

    override fun findBrandNameById(brandId: Long): String? {
        return queryFactory
            .select(brand.name)
            .from(brand)
            .where(brand.id.eq(brandId))
            .fetchOne()
    }

    override fun findMaxPriceProductsByCategoryId(categoryId: Long): List<Product> {
        val subquery = JPAExpressions
            .select(product.price.max())
            .from(product)
            .where(product.category.id.eq(categoryId))

        return queryFactory
            .selectFrom(product)
            .where(
                product.category.id.eq(categoryId),
                product.price.eq(subquery)
            )
            .fetch()
    }

    override fun findMinPriceProductsByCategoryId(categoryId: Long): List<Product> {
        val subquery = JPAExpressions
            .select(product.price.min())
            .from(product)
            .where(product.category.id.eq(categoryId))

        return queryFactory
            .selectFrom(product)
            .where(
                product.category.id.eq(categoryId),
                product.price.eq(subquery)
            )
            .fetch()
    }
}