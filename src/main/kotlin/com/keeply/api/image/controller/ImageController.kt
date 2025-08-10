package com.keeply.api.image.controller

import com.keeply.api.image.dto.ImageRequestDTO
import com.keeply.api.image.dto.ImageResponseDTO
import com.keeply.api.image.service.ImageService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import com.keeply.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
        val apiResponse = if (requestDTO.isCached) {
            imageService.saveCachedImage(userDetails.userId, requestDTO)
        } else {
            imageService.setFolderOfImage(userDetails.userId, requestDTO)
        }
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }
    @PostMapping("/save")
    @Operation(summary = "ocr을 거치지 않은 이미지 저장",
        description = """
            미분류 이미지 folderId = null
            폴더에 이미지 저장 folderId = {folderId}
        """)
    fun saveImageWithoutFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestPart("file") file: MultipartFile,
        @RequestParam("folderId", required = false) folderId: Long?,
    ): ResponseEntity<ApiResponse<ImageResponseDTO.SaveResponseDTO>> {
        val apiResponse = if(folderId==null) imageService.saveUncategorizedImage(userDetails.userId, file)
        else imageService.saveImage(userDetails.userId, file, folderId)
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }

    @PatchMapping("/{imageId}")
    @Operation(summary = "이미지를 다른 폴더로 이동")
    fun moveImageToAnotherFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable imageId: Long,
        @RequestBody requestDTO: ImageRequestDTO.MoveImageRequestDTO
    ): ResponseEntity<ApiResponse<ImageResponseDTO.MoveImageResponseDTO>> {
        val apiResponse = imageService.moveImage(userDetails.userId, imageId, requestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }

    @GetMapping("/{imageId}")
    @Operation(summary = "단일 이미지 조회 API")
    fun getImage(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable imageId: Long,
    ): ResponseEntity<ApiResponse<ImageResponseDTO.ImageInfoDTO>> {
        val apiResponse = imageService.getImageInfo(userDetails.userId, imageId)
        return ResponseEntity.ok(apiResponse)
    }

    @DeleteMapping("/{imageId}")
    @Operation(summary = "이미지 삭제 API")
    fun deleteImage(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable imageId: Long
    ): ResponseEntity<ApiResponse<Message>> {
        val apiResponse = imageService.deleteImage(userDetails.userId, imageId)
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }
}