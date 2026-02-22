package com.release.jira_api_release.service

import com.fasterxml.jackson.databind.JsonNode
import com.suygecu.teamly_service.teamly.PendingIdsStore
import com.suygecu.teamly_service.teamly.ProcessedIdsStore
import com.suygecu.teamly_service.teamly.TeamlyQueryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Service
class TeamlyRequestsPollingJob(
    private val teamlyQueryService: TeamlyQueryService,
    private val processedIdsStore: ProcessedIdsStore,
    private val pendingIdsStore: PendingIdsStore,
    private val orchestrator: RequestNotificationOrchestrator,
    @Value("\${teamly.requests.contentDatabaseId}") private val contentDatabaseId: String,
    @Value("\${teamly.requests.parentId}") private val parentId: String,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val processed = ConcurrentHashMap.newKeySet<String>()
    private val pending = ConcurrentHashMap.newKeySet<String>()

    @EventListener(ApplicationReadyEvent::class)
    fun load() {
        processed.addAll(processedIdsStore.read())
        pending.addAll(pendingIdsStore.read())
        log.info("Loaded processed={}, pending={}", processed.size, pending.size)
    }

    @Scheduled(fixedDelayString = "\${teamly.requests.poll-ms:1000}", initialDelayString = "\${teamly.requests.initial-delay-ms:15000}")
    fun poll() {
        try {
            val response = teamlyQueryService.fetchSchemaProperties(contentDatabaseId, parentId)
            val content = response.path("content")
            if (!content.isArray) return

            var processedChanged = false
            var pendingChanged = false

            for (i in 0 until content.size()) {
                val row = content[i]
                val id = row.path("article").path("id").asText("").trim()
                if (id.isBlank()) continue
                if (processed.contains(id)) continue

                val isComplete = teamlyQueryService.isComplete(row) // вынеси isComplete в public метод
                if (isComplete) {
                    val title = row.path("article").path("properties").path("properties")
                        .path("title").path("text").asText("").trim()
                    if (title.isBlank()) continue

                    orchestrator.onNewRequest(NewRequestEvent(title = title, requestId = id, authorTag = null))

                    if (processed.add(id)) processedChanged = true
                    if (pending.remove(id)) pendingChanged = true
                } else {
                    // видели, но ещё не complete
                    if (pending.add(id)) pendingChanged = true
                }
            }

            if (pendingChanged) pendingIdsStore.write(pending)
            if (processedChanged) processedIdsStore.write(processed)

        } catch (e: Exception) {
            log.warn("Teamly polling failed: {}", e.message, e)
        }
    }
}