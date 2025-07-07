package com.keeply.domain.user.repository

import com.keeply.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByKakaoId(lng: Long): User?
}