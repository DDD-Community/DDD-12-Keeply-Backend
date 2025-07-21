package com.keeply.api.ocr.service

import com.keeply.api.ocr.dto.OcrRequestDTO
import com.keeply.api.ocr.dto.OcrResponseDTO
import com.keeply.api.ocr.validator.OcrValidator
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.global.common.googlevision.GoogleVisionAPI
import com.keeply.global.dto.ApiResponse
import com.keeply.global.redis.RedisService
import com.keeply.global.s3.S3Service
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class OcrService(
    private val redisService: RedisService,
    private val imageRepository: ImageRepository,
    private val ocrValidator: OcrValidator,
    private val s3Service: S3Service,
    private val googleVisionAPI: GoogleVisionAPI
) {
    fun analyzeNewImage(requestDTO: OcrRequestDTO.Analyze, file: MultipartFile?): ApiResponse<OcrResponseDTO> {

        ocrValidator.validateImageFile(file)
        ocrValidator.validateAnalyzeRequest(requestDTO)
        val cachedImageId = UUID.randomUUID().toString()
        val imageBytes = file!!.bytes

        val detectedText = googleVisionAPI.extractTextFromImage(file)

        val recommendedTags = googleVisionAPI.classifyText(detectedText)

        redisService.cacheImage(cachedImageId, imageBytes, detectedText)

        return ApiResponse(
            success = true,
            data = OcrResponseDTO(cachedImageId, detectedText, recommendedTags)
        )
    }

    fun analyzeSavedImage(userId: Long, requestDTO: OcrRequestDTO.Analyze, file: MultipartFile?): ApiResponse<OcrResponseDTO> {
        ocrValidator.validateAnalyzeRequest(requestDTO)
        val imageId = requestDTO.imageId
        val image = imageRepository.findImageByIdAndUserId(imageId!!,userId)
            ?: throw Exception("imgaeId에 해당하는 이미지를 찾을 수 없습니다.")

        val file = s3Service.getMultipartFileFromS3(image.s3Key!!)

        val detectedText = googleVisionAPI.extractTextFromImage(file)
        val recommendedTags = googleVisionAPI.classifyText(detectedText)

        return ApiResponse<OcrResponseDTO>(
            success = true,
            data = OcrResponseDTO(
                detectedText = detectedText,
                recommendedTags = recommendedTags
            )
        )
    }

}