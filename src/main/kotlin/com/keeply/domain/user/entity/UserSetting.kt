package com.keeply.domain.user.entity

import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "user_settings")
class UserSetting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: User,
    var storageNotificationEnabled: Boolean = false,
    var marketingNotificationEnabled: Boolean = false,
) : BaseTimeEntity() {
    companion object{
        fun builder(): Builder = Builder()
    }
    class Builder {
        private lateinit var user: User
        private var storageNotificationEnabled: Boolean = false
        private var marketingNotificationEnabled: Boolean = false

        fun user(user: User) = apply { this.user = user }
        fun storageNotificationEnabled() = apply { this.storageNotificationEnabled = true }
        fun marketingNotificationEnabled() = apply { this.marketingNotificationEnabled = true }

        fun build(): UserSetting = UserSetting(
            user = user,
            storageNotificationEnabled = storageNotificationEnabled,
            marketingNotificationEnabled = marketingNotificationEnabled
        )
    }
}