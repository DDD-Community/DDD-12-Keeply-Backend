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
import org.springframework.web.bind.annotation.*

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
        val apiResponse = folderService.createFolder(userDetails.userId, requestDTO)
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse)
    }

    @GetMapping
    @Operation(summary = "유저별 폴더 목록 조회 검색 API")
    fun getFolders(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestParam(required = false) keyword: String?,
        @RequestParam(required = false, defaultValue = "updatedAt") sortBy: String,
        @RequestParam(required = false, defaultValue = "desc") orderBy: String,
    ): ResponseEntity<ApiResponse<FolderResponseDTO.FolderList>> {
        val requestDTO = FolderRequestDTO.GetFoldersRequestDTO(
            keyword = keyword,
            sortBy = sortBy,
            orderBy = orderBy
        )
        val apiResponse = folderService.getFolders(userDetails.userId, requestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }

    @GetMapping("/{folderId}")
    @Operation(summary = "folderId로 폴더의 이미지 리스트 검색, 미분류 이미지 리스트 검색 API",
        description = "폴더 검색시 folderId, 미분류 이미지 검색시, folderId = \"uncategorized\"")
    fun getFolderImages(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: String
    ): ResponseEntity<ApiResponse<FolderResponseDTO.FolderImages>> {
        val apiResponse = if (folderId == "uncategorized") {
            folderService.getUncategorizedImages(userDetails.userId)
        } else {
            val parsedFolderId = folderId.toLongOrNull()
                ?: throw Exception("유효하지 않은 folderId입니다.")
            folderService.getFolderImages(userDetails.userId, parsedFolderId)
        }
        return ResponseEntity.ok(apiResponse)
    }


    @PutMapping("/{folderId}")
    @Operation(summary = "폴더 수정 API")
    fun updateFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long,
        @RequestBody requestDTO : FolderRequestDTO.UpdateRequestDTO
    ): ResponseEntity<ApiResponse<FolderResponseDTO.Folder>> {
        val apiResponse = folderService.updateFolder(userDetails.userId, folderId, requestDTO)
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }

    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제 API")
    fun deleteFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long
    ): ResponseEntity<ApiResponse<Message>> {
        val apiResponse = folderService.deleteFolder(userDetails.userId, folderId)
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
    }
}