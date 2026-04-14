package com.suygecu.teamly_service.outline.service

import OutlineDocumentResponse
import com.suygecu.teamly_service.dto.outline.OutlineDocumentUpdateRequest
import com.suygecu.teamly_service.dto.outline.OutlineUpdateDataAttribute
import com.suygecu.teamly_service.service.jira.ContentType
import org.springframework.stereotype.Service

@Service
class OutlineDocumentService(
    private val outlineClient: OutlineClient
) {

    fun getDocumentByContentType(contentType: ContentType): OutlineDocumentResponse {
        return outlineClient.getDocumentInfo(contentType.uuidPage)
    }

    fun updateDocumentTable(contentType: ContentType): OutlineDocumentResponse {
        val document = getDocumentByContentType(contentType)
        val data = document.data

        val oldText = data.text ?: ""
        val newText = rebuildTable(oldText)

        val request = OutlineDocumentUpdateRequest(
            id = data.id,
            collectionId = requireNotNull(data.collectionId) { "collectionId is null for document ${data.id}" },
            title = data.title ?: "Untitled",
            text = newText,
            publish = true,
            fullWidth = data.fullWidth ?: false,
            templateId = data.templateId,
            parentDocumentId = data.parentDocumentId,
            icon = data.emoji,
            dataAttributes = data.dataAttributes.orEmpty()
                .mapNotNull { attr ->
                    val attrId = attr.dataAttributeId ?: return@mapNotNull null
                    OutlineUpdateDataAttribute(
                        dataAttributeId = attrId,
                        value = attr.value
                    )
                }
        )

        return outlineClient.updateDocument(request)
    }

    private fun rebuildTable(oldText: String): String {
        return oldText.replace(
            "| AAA | Done |",
            "| AAA | Updated |"
        )
    }
}