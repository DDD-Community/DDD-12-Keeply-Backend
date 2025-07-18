package com.keeply.domain.image.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.tag.entity.Tag
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Image(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        var insight: String? = null,
        var s3Key: String? = null,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "folder_id")
        var folder: Folder? = null,

        @ManyToOne
        @JoinColumn(name = "tag_id")
        var tag: Tag
) : BaseTimeEntity()
