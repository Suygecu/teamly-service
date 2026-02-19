package com.release.jira_api_release.bot.handlers.dialog

import com.release.jira_api_release.service.ReleaseTableOperator
import kotlinx.serialization.json.Json
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.data.MattermostPost
import net.exbo.mattermost.client.ws.CreatePostMessage
import net.exbo.mattermost.client.ws.DialogSubmissionMessage
import org.springframework.stereotype.Component

@Component
class ReleaseDialogHandler(
    private val releaseTableOperator: ReleaseTableOperator,
    private val client: MattermostClient
) {

    fun releaseInfo(dialogMessage: DialogSubmissionMessage) {
        val submission = dialogMessage.dialog.submission
        val projectId = submission?.get("projectID")
        val pageId = submission?.get("pageID")
        val stateJson = dialogMessage.dialog.state
        val channelId = parseState(stateJson)["channelId"] ?: return

        projectId?.let { pid ->
            pageId?.let { pgid ->
                releaseTableOperator.updateReleaseTable(pid, pgid)
                notifyUserDirect(
                    channelId = channelId,
                    message = "✅ Таблица релиза успешно обновлена!\nhttps://wiki.xexbo.ru/pages/viewpage.action?pageId=$pgid"
                )
            }
        }
    }

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
