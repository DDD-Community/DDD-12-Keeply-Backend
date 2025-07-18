package com.keeply.api.ocr.service

import com.google.cloud.language.v2.ClassifyTextRequest
import com.google.cloud.language.v2.Document
import com.google.cloud.language.v2.Document.Type
import com.google.cloud.language.v2.LanguageServiceClient
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import com.keeply.api.ocr.dto.OcrRequestDTO
import com.keeply.api.ocr.dto.OcrResponseDTO
import com.keeply.domain.image.repository.ImageRepository
import com.keeply.global.dto.ApiResponse
import com.keeply.global.redis.RedisService
import com.keeply.global.s3.S3Service
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import kotlin.use

@Service
class OcrService(
    private val redisService: RedisService,
    private val imageRepository: ImageRepository,
    private val s3Service: S3Service
) {
    fun analyzeNewImage(requestDTO: OcrRequestDTO.Analyze): ApiResponse<OcrResponseDTO> {
        val file = requestDTO.file
            ?: throw Exception("Image 파일을 찾을 수 없습니다.")

        val cachedImageId = UUID.randomUUID().toString()
        val imageBytes = file.bytes

        val detectedText = extractTextFromImage(file)

        val recommendedTags = classifyText(detectedText)

        redisService.cacheImage(cachedImageId, imageBytes, detectedText)

        return ApiResponse(
            success = true,
            data = OcrResponseDTO(cachedImageId, detectedText, recommendedTags)
        )
    }

    fun analyzeSavedImage(requestDTO: OcrRequestDTO.Analyze): ApiResponse<OcrResponseDTO> {
        val imageId = requestDTO.imageId
            ?: throw Exception("imageId가 존재하지 않습니다.")
        val image = imageRepository.findById(imageId)
            .orElseThrow { Exception("imgaeId에 해당하는 이미지를 찾을 수 없습니다.") }

        val file = s3Service.getMultipartFileFromS3(image.s3Key!!)

        val detectedText = extractTextFromImage(file)
        val recommendedTags = classifyText(detectedText)

        return ApiResponse<OcrResponseDTO>(
            success = true,
            data = OcrResponseDTO(
                detectedText = detectedText,
                recommendedTags = recommendedTags
            )
        )
    }

    private fun extractTextFromImage(file: MultipartFile): String {
        val imgBytes = ByteString.readFrom(file.inputStream)
        val image = Image.newBuilder().setContent(imgBytes).build()
        val feature = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder()
            .addFeatures(feature)
            .setImage(image)
            .build()

        ImageAnnotatorClient.create().use { client ->
            val response = client.batchAnnotateImages(listOf(request))
            val res = response.responsesList.firstOrNull()
                ?: throw Exception("Vision API 응답이 없습니다.")

            if (res.hasError()) {
                throw Exception("Vision API 오류: ${res.error.message}")
            }

            return res.textAnnotationsList.firstOrNull()?.description ?: ""
        }
    }

    private fun classifyText(text: String) : List<String>? {
        LanguageServiceClient.create().use { language ->
            val doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build()
            val request = ClassifyTextRequest.newBuilder().setDocument(doc).build()
            val response = language.classifyText(request)

            val categories = response.categoriesList
            if (categories.isEmpty()) {
                return null
            }

            return categories
                .sortedByDescending { it.confidence }
                .map { it.name }
        }
    }
}