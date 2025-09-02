package com.keeply.global.fcm.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream

@Configuration
class FirebaseConfig {
    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        if (FirebaseApp.getApps().isEmpty()) {
            val path = System.getenv("FIREBASE_CREDENTIAL_JSON_PATH")
                ?: throw IllegalStateException("FIREBASE_CREDENTIAL_JSON_PATH is not set")

            val serviceAccount = FileInputStream(path)

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()
            FirebaseApp.initializeApp(options)
        }

        return FirebaseMessaging.getInstance()
    }
}