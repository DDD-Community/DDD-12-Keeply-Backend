package com.keeply.api.image.dto

class ImageRequestDTO {
    data class Save(
        val isCached: Boolean,
        val cachedImageId: String? = null,
        val imageId: Long? = null,
        val imageInsight: String? = null,
        val folderId: Long,
        val tag: String
    )
}
