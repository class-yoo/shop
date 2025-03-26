package com.shoptest.api.price.service

import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PriceServiceTest {

    @Mock
    private lateinit var categoryRepository: CategoryRepository


    @Mock
    private lateinit var productRepository: ProductRepository

    @InjectMocks
    private lateinit var priceService: PriceService

    @Test
    fun `카테고리별 최저가 조회 - 정상 동작`() {
        // given
        val category = Category(type = CategoryType.TOP)
        val brandA = Brand(name = "A")
        val product = Product(category = category, brand = brandA, price = 1000)

        `when`(categoryRepository.findAll()).thenReturn(listOf(category))
        `when`(productRepository.findAllByCategory(category)).thenReturn(listOf(product))

        // when
        val result = priceService.getLowestPriceByCategory()

        // then
        assertEquals(1, result.items.size)
        assertEquals("A", result.items[0].brand)
        assertEquals(1000, result.items[0].price)
        assertEquals(1000, result.totalPrice)
    }

    @Test
    fun `카테고리별 최저가 조회 - 상품 없음`() {
        val category = Category(type = CategoryType.OUTER)
        `when`(categoryRepository.findAll()).thenReturn(listOf(category))
        `when`(productRepository.findAllByCategory(category)).thenReturn(emptyList())

        val exception = assertThrows<IllegalStateException> {
            priceService.getLowestPriceByCategory()
        }

        assertEquals("카테고리 OUTER 에 해당하는 상품이 없습니다.", exception.message)
    }
}
