package com.release.jira_api_release.bot.listeners

import com.release.jira_api_release.dto.confluence.NewSyncRequestEvent
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.data.MattermostPost
import net.exbo.mattermost.client.ws.CreatePostMessage
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Component
class SyncRequestListener(
    private val mattermostClient: MattermostClient
) {
    private val channelId = "ihuizrtrtj8g9eq3ohab7phoow" // канал маттермоста мой
    private val channelIdTread = "umgo9riryjycdpgneqgygz511h" // любой на выбор
    private val rootPostId = "hujw9i3p9pdutgn8bgpqxc4rwr"// Треб выбранного канала

    private val syncRequestPool: ExecutorService = Executors.newSingleThreadExecutor()

    @EventListener
    fun onNewSyncRequest(event: NewSyncRequestEvent) {
        val message = buildString {
            append("📥 *Новый запрос на синхрон:*\n")
            event.pages.forEach { page ->
                append("- [Тыкни на меня](https://base.xexbo.ru/space/dba6aec9-a010-40b9-afc8-074467d5d0cb/database/6f39f073-928d-4deb-bb20-4dd994e6334a?viewId=663fba82-0ed7-40de-a1a7-21c542ec3e6a)\n")
            }
            append("\n🔔 Уведомлены: @moonshy  @suygecu \n")
        }
        syncRequestPool.submit {

            mattermostClient.sendMessage(
                CreatePostMessage(
                    MattermostPost(
                        channel_id = channelId,
                        message = message,
                    )
                )

            )
            val threadPost = MattermostPost(
                channel_id = channelIdTread,
                message = message,
                root_id = rootPostId
            )

            mattermostClient.sendMessage(CreatePostMessage(threadPost))


        }

    }
}