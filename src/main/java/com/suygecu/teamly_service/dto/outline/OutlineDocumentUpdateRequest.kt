package com.suygecu.teamly_service.dto.outline

data class OutlineDocumentUpdateRequest(
    val id: String,
    val collectionId: String,
    val title: String,
    val text: String,
    val publish: Boolean = true,
    val fullWidth: Boolean = false,
    val templateId: String? = null,
    val parentDocumentId: String? = null,
    val icon: String? = null,
    val dataAttributes: List<OutlineUpdateDataAttribute> = emptyList()
)

data class OutlineUpdateDataAttribute(
    val dataAttributeId: String,
    val value: String? = null
)