package com.suygecu.teamly_service.outline.service

import com.suygecu.teamly_service.dto.outline.OutlineDocumentInfoRequest
import com.suygecu.teamly_service.dto.outline.OutlineDocumentResponse
import com.suygecu.teamly_service.dto.outline.OutlineDocumentUpdateRequest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class OutlineClient(
    private val outlineProperties: OutlineProperties
) {
    private val restTemplate = RestTemplate()

    fun getDocumentInfo(documentId: String): OutlineDocumentResponse {
        val url = outlineProperties.baseUrl + outlineProperties.documentsInfoPath
        val request = OutlineDocumentInfoRequest(id = documentId)

        val response = restTemplate.postForEntity(
            url,
            HttpEntity(request, buildHeaders()),
            OutlineDocumentResponse::class.java
        )

        return requireNotNull(response.body) {
            "Outline returned empty body for document info. documentId=$documentId"
        }
    }

    fun updateDocument(request: OutlineDocumentUpdateRequest): OutlineDocumentResponse {
        val url = outlineProperties.baseUrl + outlineProperties.documentsUpdatePath

        val response = restTemplate.postForEntity(
            url,
            HttpEntity(request, buildHeaders()),
            OutlineDocumentResponse::class.java
        )

        return requireNotNull(response.body) {
            "Outline returned empty body for document update. documentId=${request.id}"
        }
    }

    private fun buildHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.accept = listOf(MediaType.APPLICATION_JSON)
        headers.setBearerAuth(outlineProperties.token)
        return headers
    }

}