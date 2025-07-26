package com.keeply.api.user.dto

data class UserSettingRequestDTO(
    val allowStorageNotification: Boolean? = null,
    val allowMarketingNotification: Boolean? = null
)
