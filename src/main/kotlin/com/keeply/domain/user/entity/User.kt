package com.keeply.domain.user.entity

import com.keeply.domain.folder.entity.Folder
import com.keeply.global.entity.BaseTimeEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val kakaoId: Long,
    val nickname: String,
    val email: String,
    val profileImageUrl: String,
    val thumbnailImageUrl: String,
    var usedStorageSize: Long = 0,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
//    @JsonManagedReference
    val folders: List<Folder> = mutableListOf()
) : BaseTimeEntity() {
//    fun addImageInfo(imageInfo: S3ImageInfo) {
//        presignedUrls.add(imageInfo)
//    }
//
//    fun removeImageInfo(imageInfo: S3ImageInfo) {
//        presignedUrls.remove(imageInfo)
//    }

    fun increaseStorage(fileSize: Long) {
        usedStorageSize += fileSize
    }

    fun decreaseStorage(fileSize: Long) {
        usedStorageSize -= fileSize
    }
}