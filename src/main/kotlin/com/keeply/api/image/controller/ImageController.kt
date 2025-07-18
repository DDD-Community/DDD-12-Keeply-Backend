package com.keeply.api.image.controller

import com.keeply.api.image.dto.ImageRequestDTO
import com.keeply.api.image.dto.ImageResponseDTO
import com.keeply.api.image.service.ImageService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.security.CustomUserDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement

@RestController
@RequestMapping("/api/images")
class ImageController (
    private val imageService: ImageService
) {

    @PostMapping
    fun saveImage(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody requestDTO: ImageRequestDTO.Save
    ): ResponseEntity<ApiResponse<ImageResponseDTO.Save>> {
        println(requestDTO)
        try {
            val apiResponse = if (requestDTO.isCached) {
                imageService.saveCachedImage(userDetails.userId, requestDTO)
            } else {
                imageService.setFolderOfImage(userDetails.userId, requestDTO)
            }
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse< ImageResponseDTO.Save>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }
}