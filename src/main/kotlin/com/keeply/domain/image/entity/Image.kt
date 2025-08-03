package com.keeply.domain.image.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.domain.tag.entity.Tag
import com.keeply.domain.user.entity.User
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

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

    var size: Long = 0,

    var isCategorized: Boolean = true,
    var scheduledDeleteAt: LocalDateTime? = null

) : BaseTimeEntity() {
    companion object {
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var insight: String? = null
        private var s3Key: String? = null
        private lateinit var user: User
        private lateinit var folder: Folder
        private lateinit var tag: Tag
        private var size: Long = 0
        private var isCategorized: Boolean = true
        private var scheduledDeleteAt: LocalDateTime? = null

        fun insight(insight: String) = apply { this.insight = insight }
        fun s3Key(s3Key: String) = apply { this.s3Key = s3Key }
        fun user(user: User) = apply { this.user = user }
        fun folder(folder: Folder) = apply { this.folder = folder }
        fun tag(tag: Tag) = apply { this.tag = tag }
        fun size(size: Long) = apply { this.size = size }
        fun isCategorized(isCategorized: Boolean) = apply {this.isCategorized = isCategorized }
        fun scheduledDeleteAt(date: LocalDateTime) = apply { this.scheduledDeleteAt = date}

        fun build(): Image = Image(
            insight = insight,
            s3Key = s3Key,
            user = user,
            folder = folder,
            tag = tag,
            size = size,
            isCategorized = isCategorized,
            scheduledDeleteAt = scheduledDeleteAt
        )
    }
}
