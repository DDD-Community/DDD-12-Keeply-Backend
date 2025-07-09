package com.keeply.api.folder.controller

import com.keeply.api.folder.dto.FolderRequestDTO
import com.keeply.api.folder.service.FolderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/folders")
class FolderController (
    private val folderService: FolderService
) {
    @PostMapping
    fun createFolder(
        @ModelAttribute request: FolderRequestDTO.Create
    ): ResponseEntity<*> {
        val apiResponse = folderService.createFolder(request)
        if(apiResponse.success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse)
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse)
        }
    }

}