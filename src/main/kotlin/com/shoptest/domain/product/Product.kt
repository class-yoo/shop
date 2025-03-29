package com.shoptest.domain.product
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import jakarta.persistence.*

@Entity
@Table(name = "products")
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
    val price: Int

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
