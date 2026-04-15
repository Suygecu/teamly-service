package com.suygecu.teamly_service.outline.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutlineSyncScheduler(
    private val outlineJiraSyncService: OutlineJiraSyncService
) {

    /*@Scheduled(fixedDelayString = "\${outline.sync.delay-ms:900000}")*/
    fun syncAllTables() {
        println("=== OUTLINE SYNC START ===")

        try {
            val results = outlineJiraSyncService.syncAll()

            results.forEach {
                println(
                    "contentType=${it.contentType}, pageId=${it.pageId}, jiraTasksCount=${it.jiraTasksCount}, updated=${it.updated}"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        println("=== OUTLINE SYNC END ===")
    }
}