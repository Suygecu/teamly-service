package com.release.jira_api_release.service

import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.data.MattermostPost
import net.exbo.mattermost.client.ws.CreatePostMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class MattermostNotifyService(
    private val client: MattermostClient,
    @Value("\${mattermost.channel}") private val channelId: String,
    @Value("\${teamly.requests.page-url}") private val requestsPageUrl: String,
) {

    fun sendNewRequest(title: String) {
        val message = buildString {
            appendLine("📩 **Новая заявка на синхрон:** $title")
            appendLine("👥 @moonshy @suygecu")
            append("👉 [Открыть страницу с заявками]($requestsPageUrl)")

        }

        client.sendMessage(
            CreatePostMessage(
                MattermostPost(
                    channel_id = channelId,
                    message = message
                )
            )
        )
    }
}