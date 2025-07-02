package com.keeply.ocrtest

import com.google.cloud.vision.v1.*
import com.google.cloud.language.v2.*
import com.google.cloud.language.v2.Document.Type
import com.google.protobuf.ByteString
import java.io.FileInputStream
import java.io.IOException

object DetectText {
    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {
        detectAndClassifyText()
    }

    @Throws(IOException::class)
    fun detectAndClassifyText() {
        val home = System.getProperty("user.home")
        val filePath = "$home/Documents/boostcamp/Mission_1/image22.png"

        // 1. Vision API로 텍스트 추출
        val extractedText = extractTextFromImage(filePath)
        if (extractedText.isBlank()) {
            println("⚠️ 이미지에서 텍스트를 추출하지 못했습니다.")
            return
        }

        println("\n================ 📄 추출된 텍스트 ================\n")
        println(extractedText)

        // 2. Natural Language API로 분류
        println("\n================ 🏷️  추천 태그 (카테고리) ================\n")
        classifyText(extractedText)
    }

    @Throws(IOException::class)
    private fun extractTextFromImage(filePath: String): String {
        val imgBytes = ByteString.readFrom(FileInputStream(filePath))
        val img = Image.newBuilder().setContent(imgBytes).build()
        val feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build()
        val requests = listOf(request)

        ImageAnnotatorClient.create().use { client ->
            val response = client.batchAnnotateImages(requests)
            val res = response.responsesList.firstOrNull()
            if (res == null || res.hasError()) {
                println("❌ Vision API 오류: ${res?.error?.message}")
                return ""
            }
            return res.textAnnotationsList.firstOrNull()?.description ?: ""
        }
    }

    @Throws(IOException::class)
    private fun classifyText(text: String) {
        LanguageServiceClient.create().use { language ->
            val doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build()
            val request = ClassifyTextRequest.newBuilder().setDocument(doc).build()
            val response = language.classifyText(request)

            val categories = response.categoriesList
            if (categories.isEmpty()) {
                println("⚠️ 텍스트 분류 결과가 없습니다.")
                return
            }

            println(String.format("%-50s | %s", "📚 카테고리", "🔍 신뢰도"))
            println("-".repeat(65))
            for (category in categories) {
                println(String.format("%-50s | %.3f", category.name, category.confidence*100) + "%")
            }
        }
    }
}
