package com.keeply.domain.image.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Image(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Column(nullable = false)
        val url: String,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "folder_id")
        val folder: Folder
) : BaseTimeEntity()
