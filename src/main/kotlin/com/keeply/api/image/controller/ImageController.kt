package com.keeply.api.image.controller

import com.keeply.api.image.dto.ImageRequestDTO
import com.keeply.api.image.dto.ImageResponseDTO
import com.keeply.api.image.service.ImageService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/images")
class ImageController (
    private val imageService: ImageService
) {
    @PostMapping
    @Operation(summary = "이미지 저장(폴더o)",
        description =
        "신규로 OCR을 거친 이미지는 cachedImageId가 필요," +
                "미분류이미지에서 OCR을 거친 이미지는 imageId가 필요"
    )
    fun saveImage(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody requestDTO: ImageRequestDTO.SaveRequestDTO
    ): ResponseEntity<ApiResponse<ImageResponseDTO.SaveResponseDTO>> {
        try {
            val apiResponse = if (requestDTO.isCached) {
                imageService.saveCachedImage(userDetails.userId, requestDTO)
            } else {
                imageService.setFolderOfImage(userDetails.userId, requestDTO)
            }
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse< ImageResponseDTO.SaveResponseDTO>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }
    @PostMapping("/uncategorized")
    @Operation(summary = "이미지 s3에 저장(이미지 미분류로 저장)")
    fun saveImageWithoutFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<ApiResponse<ImageResponseDTO.SaveResponseDTO>> {
        try{
            val apiResponse = imageService.saveUncategorizedImage(userDetails.userId, file)
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse<ImageResponseDTO.SaveResponseDTO>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @PatchMapping("/{imageId}/move")
    @Operation(summary = "이미지를 다른 폴더로 이동")
    fun moveImageToAnotherFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable imageId: Long,
        @RequestBody requestDTO: ImageRequestDTO.MoveImageRequestDTO
    ): ResponseEntity<ApiResponse<ImageResponseDTO.MoveImageResponseDTO>> {
        try {
            val apiResponse = imageService.moveImage(userDetails.userId, imageId, requestDTO)
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse<ImageResponseDTO.MoveImageResponseDTO>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }
}