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

    @OneToMany(mappedBy = "folder", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: MutableList<Image> = mutableListOf()
) : BaseTimeEntity() {
    companion object {
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var name: String = ""
        private var color: String = ""
        private lateinit var user: User
        private var images: MutableList<Image> = mutableListOf()

        fun name(name: String) = apply { this.name = name }
        fun color(color: String) = apply { this.color = color }
        fun user(user: User) = apply { this.user = user }
        fun images(images: MutableList<Image>) = apply { this.images = images }

        fun build(): Folder {
            return Folder(
                name = name,
                color = color,
                user = user,
                images = images
            )
        }
    }
}
