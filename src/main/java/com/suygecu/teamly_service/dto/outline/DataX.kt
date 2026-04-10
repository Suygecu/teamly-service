package com.suygecu.teamly_service.dto.outline

data class DataX(
    val archivedAt: String,
    val collaborators: List<CollaboratorX>,
    val collectionId: String,
    val createdAt: String,
    val createdBy: CreatedByX,
    val dataAttributes: List<DataAttributeX>,
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
    val updatedBy: UpdatedByX,
    val urlId: String
)