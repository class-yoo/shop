package com.shoptest.api.product.service

import com.shoptest.api.product.dto.ProductCreateRequest
import com.shoptest.api.product.dto.ProductUpdateRequest
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.brand.repository.BrandRepository
import com.shoptest.domain.category.Category
import com.shoptest.domain.category.CategoryType
import com.shoptest.domain.category.repository.CategoryRepository
import com.shoptest.domain.product.Product
import com.shoptest.domain.product.repository.ProductRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.assertEquals

class ProductServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var brandRepository: BrandRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var productService: ProductService

    @BeforeEach
    fun setUp() {
        productRepository = mockk()
        brandRepository = mockk()
        categoryRepository = mockk()
        productService = ProductService(productRepository, brandRepository, categoryRepository)
    }

    @Test
    @DisplayName("상품 생성 성공")
    fun createProduct_success() {
        val brand = Brand(name = "A", id = 1L)
        val category = Category(type = CategoryType.BAG, id = 2L)
        val request = ProductCreateRequest(name = "흰색 네모 가방", brandId = 1L, categoryId = 2L, price = 15000)

        every { brandRepository.findById(1L) } returns Optional.of(brand)
        every { categoryRepository.findById(2L) } returns Optional.of(category)
        every { productRepository.save(any()) } answers {
            val arg = firstArg<Product>()
            Product(
                name = arg.name,
                brand = arg.brand,
                category = arg.category,
                price = arg.price,
                id = 99L
            )
        }

        val response = productService.create(request)

        assertEquals(99L, response.id)
        assertEquals("흰색 네모 가방", response.name)
        assertEquals(15000, response.price)
        assertEquals(1L, response.brandId)
        assertEquals(2L, response.categoryId)

        verify { productRepository.save(any()) }
    }

    @Test
    @DisplayName("상품 생성 실패 - 브랜드가 존재하지 않음")
    fun createProduct_fail_whenBrandNotFound() {
        val request = ProductCreateRequest(name = "신발", brandId = 999L, categoryId = 1L, price = 10000)
        every { brandRepository.findById(999L) } returns Optional.empty()

        assertThrows<NoSuchElementException> {
            productService.create(request)
        }
    }

    @Test
    @DisplayName("상품 생성 실패 - 카테고리가 존재하지 않음")
    fun createProduct_fail_whenCategoryNotFound() {
        val brand = Brand(name = "Nike", id = 1L)
        val request = ProductCreateRequest(name = "신발", brandId = 1L, categoryId = 999L, price = 10000)

        every { brandRepository.findById(1L) } returns Optional.of(brand)
        every { categoryRepository.findById(999L) } returns Optional.empty()

        assertThrows<NoSuchElementException> {
            productService.create(request)
        }
    }

    @Test
    @DisplayName("상품 수정에 성공한다")
    fun updateProduct_success() {
        val originalProduct = Product(
            id = 1L,
            name = "흰색 가방",
            price = 10000,
            brand = Brand(name = "A", id = 1L),
            category = Category(type = CategoryType.BAG, id = 2L)
        )

        val updateRequest = ProductUpdateRequest(name = "검정 가방", price = 12000)

        every { productRepository.findById(1L) } returns Optional.of(originalProduct)
        every { productRepository.save(any()) } answers {
            val p = firstArg<Product>()
            Product(
                name = p.name,
                brand = p.brand,
                category = p.category,
                price = p.price,
                id = p.id
            )
        }

        val response = productService.update(1L, updateRequest)

        assertEquals("검정 가방", response.name)
        assertEquals(12000, response.price)
        assertEquals(1L, response.brandId)
        assertEquals(2L, response.categoryId)
    }

    @Test
    @DisplayName("상품 수정 실패 - 상품이 존재하지 않음")
    fun updateProduct_fail_whenNotFound() {
        every { productRepository.findById(999L) } returns Optional.empty()

        assertThrows<NoSuchElementException> {
            productService.update(999L, ProductUpdateRequest(name = "가방", price = 5000))
        }
    }

    @Test
    @DisplayName("상품 삭제 성공")
    fun deleteProduct_success() {
        val product = Product(
            id = 1L,
            name = "삭제할 상품",
            price = 1000,
            brand = Brand(name = "B", id = 3L),
            category = Category(type = CategoryType.SNEAKERS, id = 4L)
        )

        every { productRepository.findById(1L) } returns Optional.of(product)
        every { productRepository.delete(product) } just Runs

        productService.delete(1L)

        verify { productRepository.delete(product) }
    }

    @Test
    @DisplayName("상품 삭제 실패 - 상품이 존재하지 않음")
    fun deleteProduct_fail_whenNotFound() {
        every { productRepository.findById(999L) } returns Optional.empty()

        assertThrows<NoSuchElementException> {
            productService.delete(999L)
        }
    }
}
