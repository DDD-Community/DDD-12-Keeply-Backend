package com.keeply.global.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class CustomUserDetails(
    val userId: Long
) : UserDetails {
    override fun getUsername() = userId.toString()
    override fun getPassword() = ""
    override fun getAuthorities() = emptyList<GrantedAuthority>()
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}