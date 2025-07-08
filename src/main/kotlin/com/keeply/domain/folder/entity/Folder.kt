package com.keeply.domain.folder.entity

import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Folder(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Column(nullable = false)
        val name: String
) : BaseTimeEntity()
