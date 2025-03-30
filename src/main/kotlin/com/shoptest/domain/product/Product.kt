package com.shoptest.domain.product
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "products",
    indexes = [
        Index(name = "idx_product_brand_id", columnList = "brand_id"),
        Index(name = "idx_product_category_id_and_price", columnList = "category_id,price"), // 특정 카테고리의 가격 조건으로 조회용
        Index(name = "idx_product_brand_id_and_category_id", columnList = "brand_id,category_id"), // 특정 브랜드의 특정 카테고리 조회용
        Index(name = "idx_product_name", columnList = "name")
    ]
)
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false)
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @Column(nullable = false)
    val price: Int,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()

) {
    fun withUpdated(name: String?, price: Int?): Product {
        return Product(
            id = this.id,
            name = name ?: this.name,
            price = price ?: this.price,
            brand = this.brand,
            category = this.category
        )
    }
}
