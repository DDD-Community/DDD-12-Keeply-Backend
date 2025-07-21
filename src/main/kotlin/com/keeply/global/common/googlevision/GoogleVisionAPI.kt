package com.keeply.global.common.googlevision

import com.google.cloud.language.v2.ClassifyTextRequest
import com.google.cloud.language.v2.Document
import com.google.cloud.language.v2.Document.Type
import com.google.cloud.language.v2.LanguageServiceClient
import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import kotlin.use

@Component
class GoogleVisionAPI {

    fun extractTextFromImage(file: MultipartFile): String {
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

    fun classifyText(text: String) : List<String>? {
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