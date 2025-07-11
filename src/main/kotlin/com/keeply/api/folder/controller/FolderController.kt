package com.keeply.api.folder.controller

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.api.folder.dto.FolderResponseDTO
import com.keeply.api.folder.service.FolderService
import com.keeply.global.dto.ApiResponse
import com.keeply.global.dto.Message
import com.keeply.global.jwt.CustomUserDetails
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
    fun createFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @RequestBody requestDTO: FolderRequestDTO.Create
    ): ResponseEntity<ApiResponse<FolderResponseDTO.Folder>> {
        val apiResponse = folderService.createFolder(userDetails.userId, requestDTO.folderName)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @GetMapping
    fun getFolders(
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<ApiResponse<FolderResponseDTO.FolderList>> {
        val apiResponse = folderService.getFolders(userDetails.userId)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @GetMapping("/{folderId}")
    fun getFolderImages(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long
    ): ResponseEntity<ApiResponse<FolderResponseDTO.FolderImages>> {
        val apiResponse = folderService.getFolderImages(userDetails.userId, folderId)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @PutMapping("/{folderId}")
    fun updateFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long,
        @RequestBody requestDTO : FolderRequestDTO.Update
    ): ResponseEntity<ApiResponse<FolderResponseDTO.Folder>> {
        val apiResponse = folderService.updateFolder(userDetails.userId, folderId, requestDTO.folderName)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

    @DeleteMapping("/{folderId}")
    fun deleteFolder(
        @AuthenticationPrincipal userDetails: CustomUserDetails,
        @PathVariable folderId: Long
    ): ResponseEntity<ApiResponse<Message>> {
        val apiResponse = folderService.deleteFolder(userDetails.userId, folderId)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

}