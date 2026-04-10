package com.suygecu.teamly_service.dto.outline

data class Data(
    val archivedAt: String,
    val collaborators: List<Collaborator>,
    val collectionId: String,
    val createdAt: String,
    val createdBy: CreatedBy,
    val dataAttributes: List<DataAttribute>,
    val deletedAt: Any,
    val emoji: String,
    val fullWidth: Boolean,
    val id: String,
    val parentDocumentId: String,
    val pinned: Boolean,
    val publishedAt: Any,
    val revision: Int,
    val templateId: String,
    val text: String,
    val title: String,
    val updatedAt: String,
    val updatedBy: UpdatedBy,
    val urlId: String
)