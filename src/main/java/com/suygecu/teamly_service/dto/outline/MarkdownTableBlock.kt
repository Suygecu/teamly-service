package com.suygecu.teamly_service.dto.outline

data class MarkdownTableBlock(
    val startLineIndex: Int,
    val endLineIndex: Int,
    val headerLine: String,
    val separatorLine: String,
    val rowLines: List<String>
)