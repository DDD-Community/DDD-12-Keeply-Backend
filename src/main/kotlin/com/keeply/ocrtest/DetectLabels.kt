package com.keeply.ocrtest

import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import java.io.FileInputStream
import java.io.IOException

object DetectLabels {
    @JvmStatic
    @Throws(IOException::class)
    fun main(args: Array<String>) {
        detectLabels()
    }

    @Throws(IOException::class)
    fun detectLabels() {
        val home = System.getProperty("user.home")
        val filePath = "$home/Documents/boostcamp/Mission_1/test2.jpeg"
        detectLabels(filePath)
    }

    @Throws(IOException::class)
    fun detectLabels(filePath: String) {
        val requests = mutableListOf<AnnotateImageRequest>()

        val imgBytes = ByteString.readFrom(FileInputStream(filePath))
        val img = Image.newBuilder().setContent(imgBytes).build()
        val feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build()
        val request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build()
        requests.add(request)

        ImageAnnotatorClient.create().use { client ->
            val response = client.batchAnnotateImages(requests)
            val responses = response.responsesList

            for (res in responses) {
                if (res.hasError()) {
                    println("Error: ${res.error.message}")
                    return
                }
                println("=== Detected Labels ===")
                for (annotation in res.labelAnnotationsList) {
                    println("Label: ${annotation.description}, Confidence: ${"%.2f".format(annotation.score * 100)}%")
                }
                println("=======================")
            }
        }
    }
}
