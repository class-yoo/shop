package com.shoptest.database.init

import com.shoptest.domain.brand.Brand
import com.shoptest.domain.brand.repository.BrandRepository
import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class TestDataInitializer(
    private val categoryRepository: CategoryRepository,
    private val brandRepository: BrandRepository,
    private val productRepository: ProductRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val categories = CategoryType.entries.map { type ->
            categoryRepository.save(Category(type = type))
        }

        val brands = listOf("A", "B", "C", "D", "E", "F", "G", "H", "I").map { name ->
            brandRepository.save(Brand(name = name))
        }

        // 가격 테이블: 브랜드별 카테고리별 가격
        val priceTable: Map<String, List<Int>> = mapOf(
            "A" to listOf(11200, 5500, 4200, 9000, 2000, 1700, 1800, 2300),
            "B" to listOf(10500, 5900, 3800, 9100, 2100, 2000, 2000, 2200),
            "C" to listOf(10000, 6200, 3300, 9200, 2200, 1900, 2200, 2100),
            "D" to listOf(10100, 5100, 3000, 9500, 2500, 1500, 2400, 2000),
            "E" to listOf(10700, 5000, 3800, 9900, 2300, 1800, 2100, 2100),
            "F" to listOf(11200, 7200, 4000, 9300, 2100, 1600, 2300, 1900),
            "G" to listOf(10500, 5800, 3900, 9000, 2200, 1700, 2100, 2000),
            "H" to listOf(10800, 6300, 3100, 9700, 2100, 1600, 2000, 2000),
            "I" to listOf(11400, 6700, 3200, 9500, 2400, 1700, 1700, 2400),
        )

        brands.forEach { brand ->
            val prices = priceTable[brand.name]!!
            prices.forEachIndexed { idx, price ->
                productRepository.save(
                    Product(
                        brand = brand,
                        category = categories[idx],
                        price = price
                    )
                )
            }
        }
    }
}
