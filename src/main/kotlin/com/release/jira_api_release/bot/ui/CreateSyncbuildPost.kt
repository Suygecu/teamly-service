package com.release.jira_api_release.bot.ui

import net.exbo.mattermost.client.data.MattermostAttachment
import net.exbo.mattermost.client.data.MattermostPost
import net.exbo.mattermost.client.data.MattermostPostProps
import net.exbo.mattermost.client.ws.CreatePostMessage

object CreateSyncbuildPost {
    fun createSyncbuildPost(channelId : String): CreatePostMessage {
        return CreatePostMessage(
            MattermostPost(
                channel_id = channelId,
                props = MattermostPostProps(
                    attachments = listOf(
                        MattermostAttachment(
                            actions = listOf(
                                Action.syncBuild()
                            )
                        )
                    )
                )
            )
        )
    }
}