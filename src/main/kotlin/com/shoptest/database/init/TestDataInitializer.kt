package com.shoptest.database.init

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
            brandRepository.save(com.shoptest.domain.brand.Brand(name = name))
        }

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

        val dummyProductNames: Map<CategoryType, List<String>> = mapOf(
            CategoryType.TOP to listOf("린넨 셔츠", "기본 흰 티셔츠", "스트라이프 셔츠", "크롭 니트"),
            CategoryType.OUTER to listOf("오버핏 자켓", "숏 패딩", "롱 트렌치코트", "데님 재킷"),
            CategoryType.PANTS to listOf("슬랙스", "크롭 청바지", "조거 팬츠", "와이드 팬츠"),
            CategoryType.SNEAKERS to listOf("화이트 스니커즈", "가죽 로퍼", "첼시 부츠", "슬리퍼"),
            CategoryType.BAG to listOf("흰색 네모 가방", "에코백", "레더 크로스백", "백팩"),
            CategoryType.ACCESSORY to listOf("미니 실버링", "블랙 시계", "볼드 귀걸이", "테니스 목걸이"),
            CategoryType.HAT to listOf("버킷햇", "볼캡", "니트 비니", "페도라"),
            CategoryType.SOCKS to listOf("줄무늬 양말", "무지 발목양말", "캐릭터 중목양말", "니삭스")
        )

        brands.forEach { brand ->
            val prices = priceTable[brand.name]!!

            prices.forEachIndexed { idx, price ->
                val category = categories[idx]
                val categoryType = category.type

                val nameList = dummyProductNames[categoryType] ?: listOf("기본 상품")
                val name = nameList.random()

                productRepository.save(
                    Product(
                        name = name,
                        brand = brand,
                        category = category,
                        price = price
                    )
                )
            }
        }
    }
}
