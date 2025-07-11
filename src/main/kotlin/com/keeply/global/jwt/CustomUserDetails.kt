package com.keeply.global.jwt

data class CustomUserDetails(
    val userId: Long
) : org.springframework.security.core.userdetails.UserDetails {
    override fun getUsername() = userId.toString()
    override fun getPassword() = ""
    override fun getAuthorities() = emptyList<org.springframework.security.core.GrantedAuthority>()
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}