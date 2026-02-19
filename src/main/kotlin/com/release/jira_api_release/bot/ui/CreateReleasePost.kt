package com.release.jira_api_release.bot.ui

import net.exbo.mattermost.client.data.*
import net.exbo.mattermost.client.ws.CreatePostMessage

object CreateReleasePost {


    fun createReleasePost(channelId : String): CreatePostMessage {
        return CreatePostMessage(
            MattermostPost(
                channel_id = channelId,
                props = MattermostPostProps(
                    attachments = listOf(
                        MattermostAttachment(
                            actions = listOf(
                                Action.createReleaseTableButton()
                            )
                        )
                    )
                )
            )
        )
    }
}
