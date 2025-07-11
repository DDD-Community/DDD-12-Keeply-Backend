package com.keeply.domain.folder.repository

import com.keeply.domain.folder.entity.Folder
import org.springframework.data.jpa.repository.JpaRepository

interface FolderRepository: JpaRepository<Folder, Long> {
    fun findByUserIdAndName(userId: Long, name: String): Folder?
    fun findAllByUserId(userId: Long): List<Folder>
    fun findByUserIdAndId(userId: Long, folderId: Long): Folder?
}