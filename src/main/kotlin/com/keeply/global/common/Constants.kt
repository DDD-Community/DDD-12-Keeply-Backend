package com.keeply.global.common

object Constants {
    object WhiteList {
        val SWAGGER_PATHS = arrayOf(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/swagger-ui.html"
        )

        val H2_PATHS = arrayOf(
            "/h2-console/**"
        )

        val APIS = arrayOf(
            "/api/login"
        )

        val ALL = SWAGGER_PATHS + H2_PATHS + APIS
    }

    object Colors{
        val FOLDER_COLORS = arrayOf(
            "#FF4400",
            "#FFC453",
            "#D4BCA1",
            "#7AB9F2",
            "#BBA6F4",
            "#BDBDBF",
            "#000000"
        )
    }
}