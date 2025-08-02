package com.keeply.domain.user.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(
    @Id
    val id: Long,
    val nickname: String,
    val email: String?,
    val profileImageUrl: String,
    val thumbnailImageUrl: String,
    var usedStorageSize: Long = 0,
    var storageLimit: Long = 1L * 1024 * 1024 * 1024,
    var fcmToken: String? = null,

    //회원 탈퇴
    var isDeleted: Boolean = false,
    var deletedAt: LocalDateTime? = null,
    var scheduledDeleteAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val folders: List<Folder> = mutableListOf(),

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var userSetting: UserSetting? = null

) : BaseTimeEntity()