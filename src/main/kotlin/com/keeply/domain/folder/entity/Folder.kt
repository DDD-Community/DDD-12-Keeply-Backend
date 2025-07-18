package com.keeply.domain.folder.entity

import com.keeply.domain.image.entity.Image
import com.keeply.domain.user.entity.User
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
class Folder(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @Column(nullable = false)
        var name: String,

        @Column(nullable = false)
        var color: String,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @OneToMany(mappedBy = "folder", cascade = [CascadeType.ALL])
        val images: List<Image> = mutableListOf()
) : BaseTimeEntity() {
        fun updateFolderName(folderName: String) {
                this.name = folderName
        }
}
