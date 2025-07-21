package com.keeply.api.image.dto

class ImageResponseDTO {
    data class SaveResponseDTO(
        val imageId: Long
    )
    data class MoveImageResponseDTO(
        val imageId: Long,
        val folderId: Long
    )
}