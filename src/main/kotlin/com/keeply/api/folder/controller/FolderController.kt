package com.keeply.api.folder.controller

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.api.folder.dto.FolderResponseDTO
import com.keeply.api.folder.service.FolderService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import com.keeply.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/folders")
class FolderController (
    private val folderService: FolderService
) {
    @PostMapping
    @Operation(summary = "폴더 생성 API")
    fun createFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody requestDTO: FolderRequestDTO.CreateRequestDTO
    ): ResponseEntity<ApiResponse<FolderResponseDTO.Folder>> {
        try {
            val apiResponse = folderService.createFolder(userDetails.userId, requestDTO)

            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse<FolderResponseDTO.Folder>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @GetMapping
    @Operation(summary = "유저별 폴더 목록 검색 API")
    fun getFolders(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<ApiResponse<FolderResponseDTO.FolderList>> {
        try {
            val apiResponse = folderService.getFolders(userDetails.userId)

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse<FolderResponseDTO.FolderList>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @GetMapping("/{folderId}")
    @Operation(summary = "folderId로 폴더의 이미지 리스트 검색 API",
        description = "폴더 검색시 folderId, 미분류 이미지 검색시, folderId = \"uncategorized\"")
    fun getFolderImages(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long
    ): ResponseEntity<ApiResponse<FolderResponseDTO.FolderImages>> {
        try {
            val apiResponse = folderService.getFolderImages(userDetails.userId, folderId)

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse<FolderResponseDTO.FolderImages>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @PutMapping("/{folderId}")
    @Operation(summary = "폴더 수정 API")
    fun updateFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long,
        @RequestBody requestDTO : FolderRequestDTO.UpdateRequestDTO
    ): ResponseEntity<ApiResponse<FolderResponseDTO.Folder>> {
        try {
            val apiResponse = folderService.updateFolder(userDetails.userId, folderId, requestDTO)

            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        }catch (e: Exception) {
            val apiResponse = ApiResponse<FolderResponseDTO.Folder>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제 API")
    fun deleteFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long
    ): ResponseEntity<ApiResponse<Message>> {
        try {
            val apiResponse = folderService.deleteFolder(userDetails.userId, folderId)
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } catch (e: Exception) {
            val apiResponse = ApiResponse<Message>(
                success = false,
                message = e.message
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }
}