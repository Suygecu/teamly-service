package com.release.jira_api_release.bot.handlers.dialog

import kotlinx.serialization.json.Json
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.data.MattermostPost
import net.exbo.mattermost.client.ws.CreatePostMessage
import net.exbo.mattermost.client.ws.DialogSubmissionMessage
import org.springframework.stereotype.Component

@Component
class ReleaseDialogHandler(
    private val client: MattermostClient
) {


    private fun parseState(state: String?): Map<String, String> {
        return try {
            Json.decodeFromString(state ?: "{}")
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun notifyUserDirect(
        channelId: String,
        message: String
    ) {
        try {
            client.sendMessage(
                CreatePostMessage(
                    MattermostPost(
                        channel_id = channelId,
                        message = message
                    )
                )
            )
            println("✅ Сообщение отправлено в канал $channelId")
        } catch (e: Exception) {
            println("❌ Ошибка при отправке сообщения: ${e.message}")
            e.printStackTrace()
        }
    }
}
