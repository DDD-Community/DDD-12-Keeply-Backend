package com.keeply.domain.image.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.tag.entity.Tag
import com.keeply.domain.user.entity.User
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Image(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        var insight: String? = null,
        var s3Key: String? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        var user: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "folder_id")
        var folder: Folder? = null,

        @ManyToOne
        @JoinColumn(name = "tag_id")
        var tag: Tag? = null,

        var size: Long = 0
) : BaseTimeEntity()
