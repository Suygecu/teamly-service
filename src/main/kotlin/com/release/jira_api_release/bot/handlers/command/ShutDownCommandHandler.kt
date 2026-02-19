package com.release.jira_api_release.bot.handlers.command

import com.release.jira_api_release.bot.ui.CreateShutDownPost
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.ws.NewPostMessage


class ShutDownCommandHandler : CommandHandler  {



    override fun canHandle(message: NewPostMessage): Boolean {
        val text = message.post.message?.trim()?.lowercase() ?: return false
        return text.contains("shutdown")
    }

    override fun handle(message: NewPostMessage, client: MattermostClient) {
        client.sendMessage(
            CreateShutDownPost.createShutDownPost(message.post.channel_id!!)
        )



    }

}
