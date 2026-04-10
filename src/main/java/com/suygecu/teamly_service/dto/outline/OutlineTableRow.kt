package com.suygecu.teamly_service.dto.outline

data class OutlineTableRow(
    val priority: String,
    val project: String,
    val key: String,
    val summary: String,
    val status: String,
    val assignee: String,
    val targetEnd: String,
    val epicKey: String,
    val leadTime: String
)