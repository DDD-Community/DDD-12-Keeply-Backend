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
) : BaseTimeEntity() {
        companion object {
                fun builder(): Builder = Builder()
        }
        class Builder {
                private var name: String = ""

                fun name(name: String) = apply { this.name = name }

                fun build(): Tag = Tag(name = name)
        }
}
