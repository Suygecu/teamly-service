package com.suygecu.teamly_service.dto.outline

data class OutlineTaskRow(
    val priority: String?,
    val key: String,
    val summary: String?,
    val status: String?,
    val assignee: String?,
    val deadline: String?,
    val leadTime: String?
)