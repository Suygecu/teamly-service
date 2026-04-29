package com.suygecu.teamly_service.outline.service

import com.suygecu.teamly_service.dto.jira.JiraTaskRow
import com.suygecu.teamly_service.dto.outline.OutlineDocumentResponse
import com.suygecu.teamly_service.dto.outline.OutlineDocumentUpdateRequest
import com.suygecu.teamly_service.dto.outline.OutlineUpdateDataAttribute
import com.suygecu.teamly_service.service.jira.ContentType
import com.suygecu.teamly_service.service.jira.JiraContentTypeTasksService
import org.springframework.stereotype.Service

@Service
class OutlineJiraSyncService(
    private val jiraContentTypeTasksService: JiraContentTypeTasksService,
    private val outlineClient: OutlineClient,
    private val outlineMarkdownTableService: OutlineMarkdownTableService,
    private val outlineTableFilterService: OutlineTableFilterService
) {

    fun syncAll(): List<SyncResult> {
        return ContentType.values().map { syncOne(it) }
    }

    fun syncOne(contentType: ContentType): SyncResult {
        val jiraTasks: List<JiraTaskRow> =
            jiraContentTypeTasksService.loadTasksByContentType(contentType)

        val document: OutlineDocumentResponse =
            outlineClient.getDocumentInfo(contentType.uuidPage)

        val data = document.data
        val oldText = data.text ?: ""
        val newText = outlineMarkdownTableService.syncTable(oldText, jiraTasks)

        var updated = false
        var filtersApplied = false

        if (oldText != newText) {
            val request = OutlineDocumentUpdateRequest(
                id = data.id,
                collectionId = requireNotNull(data.collectionId) { "collectionId is null for ${data.id}" },
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

            outlineClient.updateDocument(request)
            updated = true
        }

        // Даже если markdown не поменялся, фильтров могло ещё не быть.
        filtersApplied = outlineTableFilterService.enableFiltersForDocument(data.id)

        return SyncResult(
            contentType = contentType.name,
            pageId = contentType.uuidPage,
            jiraTasksCount = jiraTasks.size,
            updated = updated,
            filtersApplied = filtersApplied
        )
    }
}

data class SyncResult(
    val contentType: String,
    val pageId: String,
    val jiraTasksCount: Int,
    val updated: Boolean,
    val filtersApplied: Boolean
)