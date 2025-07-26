package com.keeply.api.user.dto

data class UserSettingResponseDTO(
    val allowStorageNotification: Boolean? = null,
    val allowMarketingNotification: Boolean? = null
)
