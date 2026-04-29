package com.suygecu.teamly_service.outline.service

import tools.jackson.databind.JsonNode
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.ObjectNode
import com.suygecu.teamly_service.dto.outline.ProsemirrorUpdateRequest
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OutlineTableFilterService(
    private val outlineClient: OutlineClient,
    private val objectMapper: ObjectMapper
) {

    fun enableFiltersForDocument(documentId: String): Boolean {
        val info = outlineClient.getProsemirrorInfo(documentId)
        val sourceContent = info.data.content

        val changed = ChangeFlag()
        val updatedContent = addFiltersToTables(sourceContent, changed)

        if (!changed.changed) {
            return false
        }

        outlineClient.updateProsemirrorDocument(
            ProsemirrorUpdateRequest(
                id = documentId,
                content = updatedContent
            )
        )

        return true
    }

    private fun addFiltersToTables(node: JsonNode, changed: ChangeFlag): JsonNode {
        if (!node.isObject) return node

        val obj = node.deepCopy() as ObjectNode
        val type = obj.path("type").asText()

        if (type == "table") {
            val attrs = if (obj.has("attrs") && obj["attrs"].isObject) {
                obj["attrs"].deepCopy() as ObjectNode
            } else {
                objectMapper.createObjectNode()
            }

            val currentFilterEnabled = attrs.path("filterEnabled").asBoolean(false)
            val currentFilters = attrs.path("filters")

            val hasExistingFilters = currentFilters.isArray && currentFilters.size() > 0

            if (!currentFilterEnabled || !hasExistingFilters) {
                attrs.put("filterEnabled", true)

                val filters = objectMapper.createArrayNode()

                // Текущая схема колонок:
                // 0 Приоритет
                // 1 ссылка(ключ)
                // 2 Название
                // 3 Статус задачи
                // 4 Прогресс по задаче
                // 5 Исполнитель
                // 6 Дедлайн
                // 7 LeadTime

                filters.add(createFilter("text", 1))    // поиск по ключу
                filters.add(createFilter("text", 2))    // поиск по названию
                filters.add(createFilter("select", 3))  // выбор статуса
                filters.add(createFilter("text", 5))    // поиск по исполнителю
                filters.add(createFilter("date", 6))    // дедлайн

                attrs.set("filters", filters)
                obj.set("attrs", attrs)
                changed.changed = true
            }
        }

        if (obj.has("content") && obj["content"].isArray) {
            val oldContent = obj["content"] as ArrayNode
            val newContent = objectMapper.createArrayNode()

            oldContent.forEach { child ->
                newContent.add(addFiltersToTables(child, changed))
            }

            obj.set("content", newContent)
        }

        return obj
    }

    private fun createFilter(type: String, columnIndex: Int): ObjectNode {
        return objectMapper.createObjectNode().apply {
            put("id", UUID.randomUUID().toString())
            put("type", type)
            put("columnIndex", columnIndex)
        }
    }

    private class ChangeFlag(
        var changed: Boolean = false
    )
}