package com.suygecu.teamly_service.controller

import com.suygecu.teamly_service.outline.service.OutlineJiraSyncService
import com.suygecu.teamly_service.outline.service.SyncResult
import com.suygecu.teamly_service.service.jira.ContentType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/debug/outline-sync")
class OutlineSyncController(
    private val outlineJiraSyncService: OutlineJiraSyncService
) {

    @PostMapping("/all")
    fun syncAll(): List<SyncResult> {
        return outlineJiraSyncService.syncAll()
    }

    @PostMapping("/{contentType}")
    fun syncOne(@PathVariable contentType: ContentType): SyncResult {
        return outlineJiraSyncService.syncOne(contentType)
    }
}