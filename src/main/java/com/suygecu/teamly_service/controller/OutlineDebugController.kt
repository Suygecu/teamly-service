package com.suygecu.teamly_service.controller

import OutlineDocumentResponse
import com.suygecu.teamly_service.outline.service.OutlineDocumentService
import com.suygecu.teamly_service.service.jira.ContentType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/debug/outline")
class OutlineDebugController(
    private val outlineDocumentService: OutlineDocumentService
) {

    @GetMapping("/{contentType}")
    fun getDocument(@PathVariable contentType: ContentType): OutlineDocumentResponse {
        return outlineDocumentService.getDocumentByContentType(contentType)
    }

    @PostMapping("/{contentType}/update-table")
    fun updateTable(@PathVariable contentType: ContentType): OutlineDocumentResponse {
        return outlineDocumentService.updateDocumentTable(contentType)
    }
}