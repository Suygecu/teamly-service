package com.suygecu.teamly_service.outline.service

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "outline")
data class OutlineProperties(
    var baseUrl: String = "",
    var token: String = "",
    var documentsInfoPath: String = "/api/documents.info",
    var documentsUpdatePath: String = "/api/documents.update"
)