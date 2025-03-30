package com.shoptest.domain.category

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "categories")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    val type: CategoryType,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)