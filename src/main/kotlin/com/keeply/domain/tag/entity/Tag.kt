package com.keeply.domain.tag.entity

import com.keeply.domain.image.entity.Image
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Tag(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Column(nullable = false)
        val name: String
) : BaseTimeEntity()
