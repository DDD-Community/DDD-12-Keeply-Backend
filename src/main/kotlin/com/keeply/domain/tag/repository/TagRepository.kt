package com.keeply.domain.tag.repository

import com.keeply.domain.tag.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?
}