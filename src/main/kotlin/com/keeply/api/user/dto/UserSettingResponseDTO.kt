package com.keeply.api.user.dto

import io.swagger.v3.oas.annotations.media.Schema

data class UserSettingResponseDTO(
    @Schema(description = "저장공간 알림 동의 여부")
    val allowStorageNotification: Boolean? = null,
    @Schema(description = "마케팅 알림 동의 여부")
    val allowMarketingNotification: Boolean? = null
)
