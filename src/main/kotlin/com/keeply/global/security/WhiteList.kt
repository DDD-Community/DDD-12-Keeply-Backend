package com.keeply.global.security

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

    val ALL = SWAGGER_PATHS + H2_PATHS
}