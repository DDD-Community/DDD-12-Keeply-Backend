package com.keeply.domain.user.repository

import com.keeply.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findUserById(id: Long): User?
    @Modifying
    @Query("""
        DELETE FROM User u 
        WHERE u.scheduledDeleteAt <= :now 
          AND u.isDeleted = true
    """)
    fun deleteAllScheduledBefore(time: LocalDateTime): Int
}