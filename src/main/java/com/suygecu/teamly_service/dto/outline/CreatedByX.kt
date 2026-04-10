package com.suygecu.teamly_service.dto.outline

data class CreatedByX(
    val avatarUrl: String,
    val createdAt: String,
    val email: String,
    val id: String,
    val isSuspended: Boolean,
    val lastActiveAt: String,
    val name: String,
    val role: String
)