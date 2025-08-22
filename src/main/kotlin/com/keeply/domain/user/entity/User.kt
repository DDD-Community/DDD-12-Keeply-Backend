package com.keeply.domain.user.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.*
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

) : BaseTimeEntity() {
    companion object {
        fun builder(): Builder = Builder()
    }
    class Builder {
        var id: Long = 0
        var nickname: String = ""
        var email: String? = null
        var profileImageUrl: String = ""
        var thumbnailImageUrl: String = ""
        var usedStorageSize: Long = 0
        var storageLimit: Long = 1L * 1024 * 1024 * 1024
        var fcmToken: String? = null

        fun id(id: Long) = apply { this.id = id }
        fun nickname(nickname: String) = apply { this.nickname = nickname }
        fun email(email: String?) = apply { this.email = email }
        fun profileImageUrl(profileImageUrl: String) = apply { this.profileImageUrl = profileImageUrl }
        fun thumbnailImageUrl(thumbnailImageUrl: String) = apply { this.thumbnailImageUrl = thumbnailImageUrl }
        fun usedStorageSize(usedStorageSize: Long) = apply { this.usedStorageSize = usedStorageSize }
        fun storageLimit(storageLimit: Long) = apply { this.storageLimit = storageLimit }
        fun fcmToken(fcmToken: String?) = apply { this.fcmToken = fcmToken }
        fun build(): User = User(
            id = id,
            nickname = nickname,
            email = email,
            profileImageUrl = profileImageUrl,
            thumbnailImageUrl = thumbnailImageUrl,
            usedStorageSize = usedStorageSize,
            storageLimit = storageLimit,
            fcmToken = fcmToken
        )
    }
}