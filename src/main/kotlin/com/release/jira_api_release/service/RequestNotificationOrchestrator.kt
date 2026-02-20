package com.release.jira_api_release.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RequestNotificationOrchestrator(
    private val mattermostNotifyService: MattermostNotifyService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun onNewRequest(event: NewRequestEvent) {
        val titlePart = event.title.trim()
        require(titlePart.isNotBlank()) { "title is blank" }

        val author = event.authorTag?.trim().orEmpty()
        val suffix = if (author.isNotBlank()) " $author" else ""

        val msgTitle = "$titlePart$suffix"

        log.info("Mattermost notify: new request title='{}'", msgTitle)

        mattermostNotifyService.sendNewRequest(msgTitle)
    }
}