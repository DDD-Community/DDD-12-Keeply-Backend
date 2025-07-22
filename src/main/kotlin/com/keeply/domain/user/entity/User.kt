package com.keeply.domain.user.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Id
    val id: Long,
    val nickname: String,
    val email: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String,
    var usedStorageSize: Long = 0,
    var storageLimit: Long = 1L * 1024 * 1024 * 1024,
    var fcmToken: String? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val folders: List<Folder> = mutableListOf()

) : BaseTimeEntity()