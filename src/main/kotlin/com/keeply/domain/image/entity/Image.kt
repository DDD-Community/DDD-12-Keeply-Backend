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
) : BaseTimeEntity() {
//        fun setInsight(insight: String?) {
//                this.insight = insight
//        }
//        fun setS3Key(s3Key: String?) {
//                this.s3Key = s3Key
//        }
//        fun setFolder(folder: Folder?) {
//                this.folder = folder
//        }
//        fun setTag(tag: Tag) {
//                this.tag = tag
//        }
}
