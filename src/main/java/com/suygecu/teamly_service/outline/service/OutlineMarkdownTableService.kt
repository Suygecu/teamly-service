package com.suygecu.teamly_service.outline.service
import com.suygecu.teamly_service.dto.jira.JiraTaskRow
import com.suygecu.teamly_service.dto.outline.ExistingTableRow
import com.suygecu.teamly_service.dto.outline.MarkdownTableBlock
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import kotlin.collections.filter

@Service
class OutlineMarkdownTableService(
    @Value("\${jira.base.url}") private val jiraBaseUrl: String
) {

    fun syncTable(text: String, jiraTasks: List<JiraTaskRow>): String {
        val tableBlock = extractFirstTable(text)
            ?: error("Не нашел таблицу в тексте страницы")

        val existingRows = parseExistingRows(tableBlock)
        val jiraByKey = jiraTasks.associateBy { it.getKey() ?: "" }

        val allowedStatuses = setOf(
            "TO DO",
            "PAUSE",
            "PREPARING",
            "BACKLOG",
            "IN DEVELOPMENT"
        )

        val keptRows = existingRows.filter { existing ->
            val jiraTask = jiraByKey[existing.key] ?: return@filter false

            val jiraStatus = normalizeStatus(jiraTask.getStatus())

            jiraStatus in allowedStatuses &&
                    normalizeStatus(existing.status) == jiraStatus
        }

        val keptKeys = keptRows.map { it.key }.toSet()

        val newLines = jiraTasks
            .filter { task ->
                val key = task.getKey() ?: ""
                val status = normalizeStatus(task.getStatus())

                key.isNotBlank() &&
                        key !in keptKeys &&
                        status in allowedStatuses
            }
            .map { buildNewRow(it) }

        val finalTableLines = mutableListOf<String>()
        finalTableLines += tableBlock.headerLine
        finalTableLines += tableBlock.separatorLine
        finalTableLines += keptRows.map { it.rawLine }
        finalTableLines += newLines

        return replaceTable(text, tableBlock, finalTableLines)
    }

    private fun extractFirstTable(text: String): MarkdownTableBlock? {
        val lines = text.lines()

        var start = -1
        var end = -1

        for (i in lines.indices) {
            if (lines[i].trim().startsWith("|")) {
                if (start == -1) start = i
                end = i
            } else if (start != -1) {
                break
            }
        }

        if (start == -1 || end - start < 1) return null

        val tableLines = lines.subList(start, end + 1)
        if (tableLines.size < 2) return null

        return MarkdownTableBlock(
            startLineIndex = start,
            endLineIndex = end,
            headerLine = tableLines[0],
            separatorLine = tableLines[1],
            rowLines = if (tableLines.size > 2) tableLines.drop(2) else emptyList()
        )
    }

    private fun parseExistingRows(tableBlock: MarkdownTableBlock): List<ExistingTableRow> {
        return tableBlock.rowLines
            .filter { it.trim().startsWith("|") }
            .mapNotNull { parseExistingRow(it) }
    }

    private fun parseExistingRow(line: String): ExistingTableRow? {
        val cells = splitMarkdownRow(line)

        // Финальная схема:
        // 0 Приоритет
        // 1 ссылка(ключ)
        // 2 Название
        // 3 Статус задачи
        // 4 Прогресс по задаче
        // 5 Исполнитель
        // 6 Дедлайн
        // 7 LeadTime

        val keyCell = cells.getOrElse(1) { "" }
        val statusCell = cells.getOrElse(3) { "" }

        val key = extractMarkdownLinkText(keyCell).trim()
        if (key.isBlank()) return null

        return ExistingTableRow(
            rawLine = line,
            key = key,
            status = statusCell
        )
    }

    private fun buildNewRow(task: JiraTaskRow): String {
        val key = task.getKey() ?: ""
        val keyLink = buildIssueLink(key)

        val priority = escapeCell(task.getPriority())
        val summary = escapeCell(task.getSummary())
        val status = escapeCell(task.getStatus())
        val progress = "" // всегда пусто для новых строк
        val assignee = escapeCell(task.getAssignee())
        val targetEnd = escapeCell(task.getTargetEnd())
        val leadTime = escapeCell(task.getLeadTime())

        return "| $priority | $keyLink | $summary | $status | $progress | $assignee | $targetEnd | $leadTime |"
    }

    private fun buildIssueLink(issueKey: String): String {
        val cleanKey = issueKey.trim()
        if (cleanKey.isBlank()) return ""
        return "[$cleanKey](${jiraBaseUrl.trimEnd('/')}/browse/$cleanKey)"
    }

    private fun replaceTable(
        originalText: String,
        tableBlock: MarkdownTableBlock,
        newTableLines: List<String>
    ): String {
        val lines = originalText.lines()

        val before = lines.subList(0, tableBlock.startLineIndex)
        val after = lines.subList(tableBlock.endLineIndex + 1, lines.size)

        return (before + newTableLines + after).joinToString("\n")
    }

    private fun splitMarkdownRow(line: String): List<String> {
        return line
            .trim()
            .removePrefix("|")
            .removeSuffix("|")
            .split("|")
            .map { it.trim() }
    }

    private fun extractMarkdownLinkText(value: String): String {
        val regex = Regex("""\[(.*?)]\((.*?)\)""")
        val match = regex.find(value)
        return match?.groupValues?.get(1) ?: value
    }

    private fun normalizeStatus(status: String?): String {
        return status?.trim()?.uppercase() ?: ""
    }

    private fun escapeCell(value: String?): String {
        return value
            ?.replace("|", "\\|")
            ?.replace("\n", " ")
            ?.trim()
            ?: ""
    }
}