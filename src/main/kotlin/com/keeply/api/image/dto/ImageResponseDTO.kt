package com.keeply.api.image.dto

import com.keeply.domain.tag.entity.Tag

class ImageResponseDTO {
    data class SaveResponseDTO(
        val imageId: Long
    )
    data class MoveImageResponseDTO(
        val imageId: Long,
        val folderId: Long
    )
    data class ImageInfoDTO(
        val imageId: Long,
        val presignedUrl: String,
        val insight: String?,
        val tag: String?
    )
}