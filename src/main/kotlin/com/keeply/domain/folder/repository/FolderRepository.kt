package com.keeply.domain.folder.repository

import com.keeply.domain.folder.entity.Folder
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FolderRepository: JpaRepository<Folder, Long> {
    fun findByUserIdAndName(userId: Long, name: String): Folder?
    fun findAllByUserId(userId: Long): List<Folder>
    fun findByUserIdAndId(userId: Long, folderId: Long): Folder?
    fun findAllByUserIdOrderByUpdatedAtDesc(userId: Long): List<Folder>
    fun findAllByUserIdOrderByUpdatedAtAsc(userId: Long): List<Folder>

    @Modifying
    @Query(
        """
        SELECT f.name FROM Folder f 
            WHERE f.user.id = :userId 
                AND f.name LIKE CONCAT(:folderName, '%')
    """
    )
    fun findAllNamesByUserIdAndFolderName(userId: Long, folderName: String): List<String>
}