package com.keeply.domain.user.repository

import com.keeply.domain.user.entity.User
import com.keeply.domain.user.entity.UserSetting
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSettingRepository : JpaRepository<UserSetting, Long> {
    fun findByUser(user: User): UserSetting?
}