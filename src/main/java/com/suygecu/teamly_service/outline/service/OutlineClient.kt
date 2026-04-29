package com.suygecu.teamly_service.outline.service

import com.suygecu.teamly_service.dto.outline.OutlineDocumentResponse
import com.suygecu.teamly_service.dto.outline.OutlineDocumentUpdateRequest
import com.suygecu.teamly_service.dto.outline.ProsemirrorInfoRequest
import com.suygecu.teamly_service.dto.outline.ProsemirrorInfoResponse
import com.suygecu.teamly_service.dto.outline.ProsemirrorUpdateRequest
import com.suygecu.teamly_service.dto.outline.ProsemirrorUpdateResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

@Component
class OutlineClient(
    private val restTemplate: RestTemplate,
    private val outlineProperties: OutlineProperties
) {

    fun getDocumentInfo(documentId: String): OutlineDocumentResponse {
        val url = outlineProperties.baseUrl.trimEnd('/') + outlineProperties.documentsInfoPath
        val cleanId = documentId.trim()
        val body = mapOf("id" to cleanId)
        println("OUTLINE documents.info url=$url")
        println("OUTLINE documents.info body=$body")

        try {
            return restTemplate.postForObject(
                url,
                HttpEntity(body, buildHeaders()),
                OutlineDocumentResponse::class.java
            ) ?: error("Empty response from documents.info")
        } catch (e: HttpClientErrorException) {
            println("OUTLINE documents.info status=${e.statusCode}")
            println("OUTLINE documents.info response=${e.responseBodyAsString}")
            throw e
        }

    }

    fun updateDocument(request: OutlineDocumentUpdateRequest): OutlineDocumentResponse {
        val url = outlineProperties.baseUrl.trimEnd('/') + outlineProperties.documentsUpdatePath

        return restTemplate.postForObject(
            url,
            HttpEntity(request, buildHeaders()),
            OutlineDocumentResponse::class.java
        ) ?: error("Empty response from documents.update")
    }

    fun getProsemirrorInfo(documentId: String): ProsemirrorInfoResponse {
        val url = outlineProperties.baseUrl.trimEnd('/') + outlineProperties.documentsProsemirrorInfoPath
        val request = ProsemirrorInfoRequest(id = documentId)

        return restTemplate.postForObject(
            url,
            HttpEntity(request, buildHeaders()),
            ProsemirrorInfoResponse::class.java
        ) ?: error("Empty response from documents.prosemirror_info")
    }

    fun updateProsemirrorDocument(request: ProsemirrorUpdateRequest): ProsemirrorUpdateResponse {
        val url = outlineProperties.baseUrl.trimEnd('/') + outlineProperties.documentsProsemirrorUpdatePath

        return restTemplate.postForObject(
            url,
            HttpEntity(request, buildHeaders()),
            ProsemirrorUpdateResponse::class.java
        ) ?: error("Empty response from documents.prosemirror_update")
    }

    private fun buildHeaders(): HttpHeaders {
        return HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(outlineProperties.token)
        }
    }
}