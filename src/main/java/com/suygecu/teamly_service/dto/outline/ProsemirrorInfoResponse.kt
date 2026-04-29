package com.suygecu.teamly_service.dto.outline

import tools.jackson.databind.JsonNode

data class ProsemirrorInfoResponse(
    val data: Data
) {
    data class Data(
        val id: String,
        val content: JsonNode
    )
}