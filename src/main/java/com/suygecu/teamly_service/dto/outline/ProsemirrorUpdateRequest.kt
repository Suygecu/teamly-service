package com.suygecu.teamly_service.dto.outline

import com.fasterxml.jackson.annotation.JsonInclude
import tools.jackson.databind.JsonNode

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProsemirrorUpdateRequest(
    val id: String,
    val content: JsonNode,
    val lastModifiedById: String? = null
)