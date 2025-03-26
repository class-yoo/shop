package com.shoptest.domain.product
import com.shoptest.domain.brand.Brand
import com.shoptest.domain.category.Category
import jakarta.persistence.*

@Entity
@Table(
    name = "products",
    uniqueConstraints = [UniqueConstraint(columnNames = ["brand_id", "category_id"])]
)
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    val brand: Brand,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,

    @Column(nullable = false)
    val price: Int
)
