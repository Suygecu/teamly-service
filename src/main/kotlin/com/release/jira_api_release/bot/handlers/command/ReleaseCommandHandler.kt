package com.release.jira_api_release.bot.handlers.command

import com.release.jira_api_release.bot.ui.CreateReleasePost
import com.release.jira_api_release.config.MattermostProperties
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.ws.NewPostMessage

class ReleaseCommandHandler(
    private val props: MattermostProperties
) : CommandHandler {
    override fun canHandle(message: NewPostMessage): Boolean {
        return message.post.message?.trim()?.lowercase() == "release"
    }

    override fun handle(message: NewPostMessage, client: MattermostClient) {
        client.sendMessage(
            CreateReleasePost.createReleasePost(props.channel)
        )
    }
}

